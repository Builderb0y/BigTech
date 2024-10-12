package builderb0y.bigtech.gui.screenHandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;

public class TrashInventory implements SingleStackInventory {

	public static final TrashInventory INSTANCE = new TrashInventory();

	@Override
	public ItemStack getStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public void setStack(ItemStack stack) {

	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
}