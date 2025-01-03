package builderb0y.bigtech.mixins;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

import builderb0y.bigtech.api.MagnetiteAttractableEntity;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntity_MakeMagnetiteAttractable implements MagnetiteAttractableEntity {

	@Shadow public abstract @Nullable Entity getOwner();

	@Override
	public double getMagnetiteAttractionForce() {
		return 0.25D;
	}

	@Inject(method = "tick()V", at = @At("HEAD"))
	private void bigtech_attractToMagnets(CallbackInfo callback) {
		this.attractToNearbyMagnetiteBlocks(2.0D, false);
	}

	@Override
	public boolean isImmuneToMagnetiteArmor(LivingEntity wearer) {
		return this.getOwner() == wearer;
	}
}