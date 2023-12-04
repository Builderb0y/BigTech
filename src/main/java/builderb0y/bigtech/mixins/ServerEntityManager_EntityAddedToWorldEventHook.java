package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.world.entity.EntityLike;

import builderb0y.bigtech.api.EntityAddedToWorldEvent;

@Mixin(ServerEntityManager.class)
public class ServerEntityManager_EntityAddedToWorldEventHook {

	@Inject(method = "addEntity(Lnet/minecraft/world/entity/EntityLike;Z)Z", at = @At("HEAD"), cancellable = true)
	private void bigtech_beforeEntityAdded(EntityLike entityLike, boolean existing, CallbackInfoReturnable<Boolean> callback) {
		if (!existing && entityLike instanceof Entity entity && !EntityAddedToWorldEvent.EVENT.invoker().onEntityAddedToWorld(entity)) {
			callback.setReturnValue(Boolean.FALSE);
		}
	}
}