package builderb0y.bigtech.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.math.random.Random;

public class TransmuteRecipeInventory implements RecipeInput {

	public ItemStack stack = ItemStack.EMPTY;
	public int totalEnergy, slotEnergy;
	public Random random;

	public TransmuteRecipeInventory(Random random) {
		this.random = random;
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
}