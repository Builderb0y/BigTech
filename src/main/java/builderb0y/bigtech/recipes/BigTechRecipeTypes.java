package builderb0y.bigtech.recipes;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;

public class BigTechRecipeTypes {

	public static final RecipeType<TransmuteRecipe> TRANSMUTE = register("transmute");

	public static void init() {}

	public static <T extends Recipe<?>> RecipeType<T> register(String id) {
		return Registry.register(
			Registries.RECIPE_TYPE,
			BigTechMod.modID(id),
			new RecipeType<T>() {

				@Override
				public String toString() {
					return "bigtech:" + id;
				}
			}
		);
	}
}