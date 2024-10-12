package builderb0y.bigtech.gui.screenHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public interface IBigTechScreenHandler {

	public abstract Inventory getInventory();

	public abstract PlayerInventory getPlayerInventory();

	public abstract List<Slot> getSlots();

	public abstract Slot addSlot(Slot slot);

	public default SlotGrid slotGrid() {
		return new SlotGrid(this);
	}

	public abstract ShiftClickRules shiftClickRules();

	public default void insertStack(ItemStack stack, Destination destination, boolean onlyWithExisting) {
		int start = destination.range.startIndex;
		int end = destination.range.endIndex;
		if (destination.reversed()) {
			for (int slotIndex = end; --slotIndex >= start && !stack.isEmpty();) {
				this.insertStack(stack, slotIndex, onlyWithExisting);
			}
		}
		else {
			for (int slotIndex = start; slotIndex < end && !stack.isEmpty(); slotIndex++) {
				this.insertStack(stack, slotIndex, onlyWithExisting);
			}
		}
	}

	public default ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = this.getSlots().get(slotIndex);
		ItemStack stack = slot.getStack();
		if (!stack.isEmpty()) while (true) {
			outer:
			for (int onlyWithExisting = 1; onlyWithExisting >= 0; onlyWithExisting--) {
				for (ShiftClickRule rule : this.shiftClickRules()) {
					if (
						rule.input.contains(slotIndex) &&
						rule.slotPredicate.test(player, slot)
					) {
						rule.output.insert(this, stack, onlyWithExisting != 0);
						if (stack.isEmpty()) break outer;
					}
				}
			}
			boolean empty = stack.isEmpty();
			slot.onTakeItem(player, stack);
			stack = slot.getStack();
			if (empty && !stack.isEmpty()) {
				//stack re-filled itself when we took items out of it.
				//try to move the new items too.
				continue;
			}
			else {
				break;
			}
		}
		return ItemStack.EMPTY;
	}

	public default boolean canUse(PlayerEntity player) {
		return this.getInventory().canPlayerUse(player);
	}

	public default void insertStack(ItemStack stack, int slotIndex, boolean onlyWithExisting) {
		Slot slot = this.getSlots().get(slotIndex);
		ItemStack existingStack = slot.getStack();
		if (onlyWithExisting) {
			if (ItemStack.areItemsAndComponentsEqual(stack, existingStack)) {
				int toMove = Math.min(stack.getCount(), slot.getMaxItemCount(stack) - existingStack.getCount());
				if (toMove > 0) {
					existingStack.increment(toMove);
					stack.decrement(toMove);
					slot.markDirty();
				}
			}
		}
		else {
			if (existingStack.isEmpty()) {
				int toMove = slot.getMaxItemCount(stack);
				if (toMove > 0) {
					slot.setStack(stack.split(toMove));
				}
			}
		}
	}

	public static class SlotGrid {

		public final IBigTechScreenHandler screen;
		public int x, y, width, height, spaceX = 18, spaceY = 18, index;
		public Inventory inventory;
		public SlotFactory slotFactory = Slot::new;

		public SlotGrid(IBigTechScreenHandler screen) {
			this.screen = screen;
		}

		public SlotGrid pos(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public SlotGrid size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public SlotGrid space(int spaceX, int spaceY) {
			this.spaceX = spaceX;
			this.spaceY = spaceY;
			return this;
		}

		public SlotGrid inventory(Inventory inventory) {
			this.inventory = inventory;
			this.index = 0;
			return this;
		}

		public SlotGrid slotFactory(SlotFactory slotFactory) {
			this.slotFactory = slotFactory;
			return this;
		}

		public SlotRange add() {
			if (this.inventory == null) throw new IllegalStateException("Did not set inventory");
			List<Slot> slots = this.screen.getSlots();
			int startIndex = slots.size();
			for (int row = 0; row < this.height; row++) {
				for (int column = 0; column < this.width; column++) {
					this.screen.addSlot(this.slotFactory.create(this.inventory, this.index++, this.x + this.spaceX * column, this.y + this.spaceY * row));
				}
			}
			int endIndex = slots.size();
			return new SlotRange(startIndex, endIndex);
		}

	}

	@FunctionalInterface
	public static interface SlotFactory {

		public abstract Slot create(Inventory inventory, int index, int x, int y);
	}

	public static record SlotRange(int startIndex, int endIndex) {

		public boolean contains(int index) {
			return index >= this.startIndex && index < this.endIndex;
		}

		public Destination destination(boolean reversed) {
			return new Destination(this, reversed);
		}

		public Destination forward() {
			return new Destination(this, false);
		}

		public Destination backward() {
			return new Destination(this, true);
		}
	}

	public static record Destination(SlotRange range, boolean reversed) {

		public void insert(IBigTechScreenHandler handler, ItemStack stack, boolean onlyWithExisting) {
			handler.insertStack(stack, this, onlyWithExisting);
		}
	}

	public static record ShiftClickRule(SlotRange input, Destination output, BiPredicate<PlayerEntity, Slot> slotPredicate) {}

	public static class ShiftClickRules extends ArrayList<ShiftClickRule> {

		public ShiftClickRules() {
			super(8);
		}

		public ShiftClickRules simple(BiPredicate<PlayerEntity, Slot> predicate, SlotRange input, Destination output) {
			this.add(new ShiftClickRule(input, output, predicate));
			return this;
		}

		public ShiftClickRules viseVersa(BiPredicate<PlayerEntity, Slot> predicate, Destination input, Destination output) {
			this.add(new ShiftClickRule(input.range, output, predicate));
			this.add(new ShiftClickRule(output.range, input, predicate));
			return this;
		}

		public ShiftClickRules collect(BiPredicate<PlayerEntity, Slot> predicate, Destination output, SlotRange... inputs) {
			for (SlotRange input : inputs) {
				this.add(new ShiftClickRule(input, output, predicate));
			}
			return this;
		}

		public ShiftClickRules distribute(BiPredicate<PlayerEntity, Slot> predicate, SlotRange input, Destination... outputs) {
			for (Destination output : outputs) {
				this.add(new ShiftClickRule(input, output, predicate));
			}
			return this;
		}
	}
}