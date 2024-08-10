package builderb0y.bigtech.recipes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.math.random.Random;

public class TransmuteRecipeInventory implements SingleStackInventory, RecipeInput {

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
	public void setStack(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot == 0) return this.stack;
		else throw new IndexOutOfBoundsException("Slot: ${slot}, size: 1");
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void markDirty() {}
}