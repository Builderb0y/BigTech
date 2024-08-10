package builderb0y.bigtech.screenHandlers;

import java.util.Objects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;

public class SingleStackInventoryImpl implements SingleStackInventory {

	public ItemStack stack;

	public SingleStackInventoryImpl(ItemStack stack) {
		this.stack = stack;
	}

	public SingleStackInventoryImpl() {
		this.stack = ItemStack.EMPTY;
	}

	@Override
	public ItemStack getStack() {
		return this.stack;
	}

	@Override
	public void setStack(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
}