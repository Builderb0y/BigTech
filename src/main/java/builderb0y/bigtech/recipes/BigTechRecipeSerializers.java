package builderb0y.bigtech.recipes;

import net.minecraft.recipe.RecipeSerializer;

public class BigTechRecipeSerializers {

	public static final RecipeSerializer<TransmuteRecipe> TRANSMUTE = RecipeSerializer.register("bigtech:transmute", new TransmuteRecipe.Serializer());

	public static void init() {}
}