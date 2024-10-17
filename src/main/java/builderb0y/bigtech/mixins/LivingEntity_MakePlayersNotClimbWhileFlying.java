package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(LivingEntity.class)
public class LivingEntity_MakePlayersNotClimbWhileFlying {

	@Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
	private void bigtech_makePlayersNotClimbWhileFlying(CallbackInfoReturnable<Boolean> callback) {
		if (((Object)(this)) instanceof PlayerEntity player && player.getAbilities().flying) {
			callback.setReturnValue(Boolean.FALSE);
		}
	}
}