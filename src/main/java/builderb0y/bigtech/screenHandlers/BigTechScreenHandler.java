package builderb0y.bigtech.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public abstract class BigTechScreenHandler extends ScreenHandler {

	public final Inventory inventory;

	public BigTechScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory) {
		super(type, syncId);
		this.inventory = inventory;
	}

	@Override
	public Slot addSlot(Slot slot) {
		return super.addSlot(slot);
	}

	public SlotGrid slotGrid() {
		return this.new SlotGrid();
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

		public SlotGrid inventory(Inventory inventory) {
			this.inventory = inventory;
			this.index = 0;
			return this;
		}

		public SlotGrid slotFactory(SlotFactory slotFactory) {
			this.slotFactory = slotFactory;
			return this;
		}

		public SlotGrid add() {
			for (int row = 0; row < this.height; row++) {
				for (int column = 0; column < this.width; column++) {
					BigTechScreenHandler.this.addSlot(this.slotFactory.create(this.inventory, this.index++, this.x + this.spaceX * column, this.y + this.spaceY * row));
				}
			}
			return this;
		}
	}

	@FunctionalInterface
	public static interface SlotFactory {

		public abstract Slot create(Inventory inventory, int index, int x, int y);
	}
}