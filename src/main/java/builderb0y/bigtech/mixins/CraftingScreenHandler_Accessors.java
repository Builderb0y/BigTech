package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

@Mixin(CraftingScreenHandler.class)
public interface CraftingScreenHandler_Accessors {

	@Accessor("context")
	public abstract ScreenHandlerContext bigtech_getContext();

	@Accessor("player")
	public abstract PlayerEntity bigtech_getPlayer();
}