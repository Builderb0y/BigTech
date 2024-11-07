package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.screen.slot.CraftingResultSlot;

@Mixin(CraftingResultSlot.class)
public interface CraftingResultSlot_InputSetter {

	@Mutable
	@Accessor("input")
	public abstract RecipeInputInventory bigtech_getInput();

	@Mutable
	@Accessor("input")
	public abstract void bigtech_setInput(RecipeInputInventory input);
}