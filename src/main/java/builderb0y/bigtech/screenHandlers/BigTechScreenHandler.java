package builderb0y.bigtech.screenHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public abstract class BigTechScreenHandler extends ScreenHandler {

	public final Inventory inventory;
	public final List<ShiftClickRule> shiftClickRules;

	public BigTechScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory) {
		super(type, syncId);
		this.inventory = inventory;
		this.shiftClickRules = new ArrayList<>(8);
	}

	public void insertStack(ItemStack stack, Destination destination, boolean onlyWithExisting) {
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

	public void insertStack(ItemStack stack, int slotIndex, boolean onlyWithExisting) {
		Slot slot = this.slots.get(slotIndex);
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

	@Override
	public Slot addSlot(Slot slot) {
		return super.addSlot(slot);
	}

	public SlotGrid slotGrid() {
		return this.new SlotGrid();
	}

	public ShiftClickRules shiftClickRules() {
		return this.new ShiftClickRules();
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = this.slots.get(slotIndex);
		ItemStack stack = slot.getStack();
		if (!stack.isEmpty()) {
			outer:
			for (int onlyWithExisting = 1; onlyWithExisting >= 0; onlyWithExisting--) {
				for (ShiftClickRule rule : this.shiftClickRules) {
					if (
						rule.input.contains(slotIndex) &&
						rule.slotPredicate.test(player, slot)
					) {
						rule.output.insert(this, stack, onlyWithExisting != 0);
						if (stack.isEmpty()) break outer;
					}
				}
			}
			slot.onTakeItem(player, stack);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	public class SlotGrid {

		public int x, y, width, height, spaceX = 18, spaceY = 18, index;
		public Inventory inventory = BigTechScreenHandler.this.inventory;
		public SlotFactory slotFactory = Slot::new;

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
			int startIndex = BigTechScreenHandler.this.slots.size();
			for (int row = 0; row < this.height; row++) {
				for (int column = 0; column < this.width; column++) {
					BigTechScreenHandler.this.addSlot(this.slotFactory.create(this.inventory, this.index++, this.x + this.spaceX * column, this.y + this.spaceY * row));
				}
			}
			int endIndex = BigTechScreenHandler.this.slots.size();
			return new SlotRange(startIndex, endIndex);
		}
	}

	public static BiPredicate<PlayerEntity, Slot> any() {
		return (PlayerEntity player, Slot slot) -> true;
	}

	public static BiPredicate<PlayerEntity, Slot> slot(Predicate<Slot> slotPredicate) {
		return (PlayerEntity player, Slot slot) -> slotPredicate.test(slot);
	}

	public static BiPredicate<PlayerEntity, Slot> stack(Predicate<ItemStack> stackPredicate) {
		return (PlayerEntity player, Slot slot) -> stackPredicate.test(slot.getStack());
	}

	public static BiPredicate<PlayerEntity, Slot> item(Predicate<Item> itemPredicate) {
		return (PlayerEntity player, Slot slot) -> itemPredicate.test(slot.getStack().getItem());
	}

	public static BiPredicate<PlayerEntity, Slot> itemIs(Item item) {
		return (PlayerEntity player, Slot slot) -> slot.getStack().isOf(item);
	}

	public static BiPredicate<PlayerEntity, Slot> itemIn(TagKey<Item> tag) {
		return (PlayerEntity player, Slot slot) -> slot.getStack().isIn(tag);
	}

	public static BiPredicate<PlayerEntity, Slot> player(Predicate<PlayerEntity> playerPredicate) {
		return (PlayerEntity player, Slot slot) -> playerPredicate.test(player);
	}

	public static BiPredicate<PlayerEntity, Slot> playerSlot(BiPredicate<PlayerEntity, Slot> playerSlotPredicate) {
		return playerSlotPredicate;
	}

	public static BiPredicate<PlayerEntity, Slot> playerStack(BiPredicate<PlayerEntity, ItemStack> playerStackPredicate) {
		return (PlayerEntity player, Slot slot) -> playerStackPredicate.test(player, slot.getStack());
	}

	public static BiPredicate<PlayerEntity, Slot> playerItem(BiPredicate<PlayerEntity, Item> playerItemPredicate) {
		return (PlayerEntity player, Slot slot) -> playerItemPredicate.test(player, slot.getStack().getItem());
	}

	@FunctionalInterface
	public static interface SlotFactory {

		public abstract Slot create(Inventory inventory, int index, int x, int y);
	}

	public class ShiftClickRules {

		public ShiftClickRules simple(BiPredicate<PlayerEntity, Slot> predicate, SlotRange input, Destination output) {
			BigTechScreenHandler.this.shiftClickRules.add(new ShiftClickRule(input, output, predicate));
			return this;
		}

		public ShiftClickRules viseVersa(BiPredicate<PlayerEntity, Slot> predicate, Destination input, Destination output) {
			BigTechScreenHandler.this.shiftClickRules.add(new ShiftClickRule(input.range, output, predicate));
			BigTechScreenHandler.this.shiftClickRules.add(new ShiftClickRule(output.range, input, predicate));
			return this;
		}

		public ShiftClickRules collect(BiPredicate<PlayerEntity, Slot> predicate, Destination output, SlotRange... inputs) {
			for (SlotRange input : inputs) {
				BigTechScreenHandler.this.shiftClickRules.add(new ShiftClickRule(input, output, predicate));
			}
			return this;
		}

		public ShiftClickRules distribute(BiPredicate<PlayerEntity, Slot> predicate, SlotRange input, Destination... outputs) {
			for (Destination output : outputs) {
				BigTechScreenHandler.this.shiftClickRules.add(new ShiftClickRule(input, output, predicate));
			}
			return this;
		}
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

		public void insert(BigTechScreenHandler handler, ItemStack stack, boolean onlyWithExisting) {
			handler.insertStack(stack, this, onlyWithExisting);
		}
	}

	public static record ShiftClickRule(SlotRange input, Destination output, BiPredicate<PlayerEntity, Slot> slotPredicate) {}
}