package builderb0y.bigtech.recipes;

import java.util.Objects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;

public class TransmuteRecipeInventory implements SingleStackInventory {

	public ItemStack stack = ItemStack.EMPTY;
	public int totalEnergy, slotEnergy;
	public Random random;

	public TransmuteRecipeInventory(Random random) {
		this.random = random;
	}

	@Override
	public ItemStack getStack() {
		return this.stack;
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
		this.stack = stack;
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void markDirty() {}
}