package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.EntityShapeContext;
import net.minecraft.item.ItemStack;

import builderb0y.bigtech.mixinterfaces.HeldItemGetter;

@Mixin(EntityShapeContext.class)
public class EntityShapeContext_ImplementHeldItemGetter implements HeldItemGetter {

	@Shadow @Final private ItemStack heldItem;

	@Override
	public ItemStack bigtech_getHeldItem() {
		//lithium nulls out this field for no apparent reason.
		//it doesn't save on any performance,
		//and it breaks assumptions other mods make.
		//https://github.com/CaffeineMC/lithium-fabric/issues/498
		//I have an override in fabric.mod.json to disable lithium's mixin to this class,
		//but I'm leaving this null check here just in case another mod does something equally dumb.
		return this.heldItem != null ? this.heldItem : ItemStack.EMPTY;
	}
}