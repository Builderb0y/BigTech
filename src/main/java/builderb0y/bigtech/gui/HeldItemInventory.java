package builderb0y.bigtech.gui;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;

public interface HeldItemInventory extends Inventory {

	public abstract int getHeldSlot();

	public static class SimpleHeldItemInventory extends SimpleInventory implements HeldItemInventory {

		public int heldSlot;

		public SimpleHeldItemInventory(int size, int heldSlot) {
			super(size);
			this.heldSlot = heldSlot;
		}

		@Override
		public int getHeldSlot() {
			return this.heldSlot;
		}
	}
}