package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.screen.AbstractCraftingScreenHandler;

@Mixin(AbstractCraftingScreenHandler.class)
public interface AbstractCraftingScreenHandler_Setters {

	@Mutable
	@Accessor("craftingInventory")
	public abstract void bigtech_setInput(RecipeInputInventory input);

	@Mutable
	@Accessor("craftingResultInventory")
	public abstract void bigtech_setResult(CraftingResultInventory result);
}