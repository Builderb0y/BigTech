package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.EntityShapeContext;
import net.minecraft.item.ItemStack;

@Mixin(EntityShapeContext.class)
public interface EntityShapeContext_HeldItemGetter {

	@Accessor("heldItem")
	public abstract ItemStack bigtech_getHeldItem();
}