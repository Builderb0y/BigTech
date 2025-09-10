package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LightningEntity;

@Mixin(LightningEntity.class)
public abstract class LightningEntity_TweakLogic {

	@Shadow private boolean cosmetic;

	/**
	prevent infinite loops where a tesla coil can summon
	lightning, which activates a lightning rod,
	which pulses the tesla coil again.
	*/
	@Inject(method = "powerLightningRod", at = @At("HEAD"), cancellable = true)
	private void bigtech_dontActivateLightningRodsWhenCosmetic(CallbackInfo callback) {
		if (this.cosmetic) callback.cancel();
	}
}