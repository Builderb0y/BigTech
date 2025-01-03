package builderb0y.bigtech.recipes;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record ArcFurnaceRecipeInput(List<ItemStack> stacks, boolean fastCool) implements RecipeInput {

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.stacks.get(slot);
	}

	@Override
	public int size() {
		return this.stacks.size();
	}
}