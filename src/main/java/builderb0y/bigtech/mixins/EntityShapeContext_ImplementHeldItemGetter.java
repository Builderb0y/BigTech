package builderb0y.bigtech.mixins;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.mixinterfaces.HeldItemGetter;

@Mixin(EntityShapeContext.class)
public class EntityShapeContext_ImplementHeldItemGetter implements HeldItemGetter {

	@Shadow @Final private ItemStack heldItem;

	@Inject(method = "<init>(ZDLnet/minecraft/item/ItemStack;Ljava/util/function/Predicate;Lnet/minecraft/entity/Entity;)V", at = @At("RETURN"))
	private void bigtech_sanityCheck(boolean descending, double minY, ItemStack heldItem, Predicate walkOnFluidPredicate, Entity entity, CallbackInfo ci) {
		if (heldItem == null) {
			BigTechMod.LOGGER.warn("Someone constructed an EntityShapeContext with a null heldItem!", new Throwable("stack trace"));
		}
	}

	@Override
	public ItemStack bigtech_getHeldItem() {
		return this.heldItem != null ? this.heldItem : ItemStack.EMPTY;
	}
}