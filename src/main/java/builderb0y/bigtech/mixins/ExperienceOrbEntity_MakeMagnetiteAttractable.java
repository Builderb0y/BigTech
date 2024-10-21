package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ExperienceOrbEntity;

import builderb0y.bigtech.api.MagnetiteAttractableEntity;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntity_MakeMagnetiteAttractable implements MagnetiteAttractableEntity {

	@Override
	public double getMagnetiteAttractionForce() {
		return 0.075D;
	}

	@Inject(method = "tick()V", at = @At("HEAD"))
	private void bigtech_attractToMagnets(CallbackInfo callback) {
		this.attractToNearbyMagnetiteBlocks(2.5D, false);
	}
}