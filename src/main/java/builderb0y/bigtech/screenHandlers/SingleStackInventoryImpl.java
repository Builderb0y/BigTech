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
	public ItemStack getStack(int slot) {
		Objects.checkIndex(slot, 1);
		return this.stack;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		Objects.checkIndex(slot, 1);
		return this.stack.split(amount);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		Objects.checkIndex(slot, 1);
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
}