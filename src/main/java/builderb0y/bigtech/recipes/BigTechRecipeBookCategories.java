package builderb0y.bigtech.recipes;

import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;

public class BigTechRecipeBookCategories {

	public static final RecipeBookCategory TRANSMUTING = Registry.register(Registries.RECIPE_BOOK_CATEGORY, BigTechMod.modID("transmuting"), new RecipeBookCategory());

	public static void init() {}
}