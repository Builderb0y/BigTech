package builderb0y.bigtech.recipes;

import net.minecraft.recipe.RecipeSerializer;

public class BigTechRecipeSerializers {

	public static final RecipeSerializer<TransmuteRecipe> TRANSMUTE = RecipeSerializer.register("bigtech:transmute", new TransmuteRecipe.Serializer());
	public static final RecipeSerializer<ArcFurnaceRecipe> ARC_FURNACE = RecipeSerializer.register("bigtech:arc_furnace", new ArcFurnaceRecipe.Serializer());

	public static void init() {}
}