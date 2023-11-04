package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

import builderb0y.bigtech.blocks.MagnetiteBlock;

@Mixin({ ProjectileEntity.class, ItemEntity.class, ExperienceOrbEntity.class })
public class AttractableEntities_AttractTowardsMagnetiteBlocks {

	@Inject(method = "tick()V", at = @At("HEAD"))
	private void bigtech_attractToMagnets(CallbackInfo callback) {
		MagnetiteBlock.attract(this.as(), this.as() instanceof ProjectileEntity ? 0.25D : 0.075D, 2.5D);
	}
}