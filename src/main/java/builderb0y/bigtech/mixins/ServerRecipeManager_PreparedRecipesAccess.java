package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.recipe.PreparedRecipes;
import net.minecraft.recipe.ServerRecipeManager;

@Mixin(ServerRecipeManager.class)
public interface ServerRecipeManager_PreparedRecipesAccess {

	@Accessor("preparedRecipes")
	public abstract PreparedRecipes bigtech_getPreparedRecipes();
}