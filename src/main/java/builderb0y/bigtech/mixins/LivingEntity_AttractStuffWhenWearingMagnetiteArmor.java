package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import builderb0y.bigtech.items.MagnetiteArmorMaterial;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_AttractStuffWhenWearingMagnetiteArmor extends Entity {

	public LivingEntity_AttractStuffWhenWearingMagnetiteArmor(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void bigtech_tickMagnetiteArmor(CallbackInfo callback) {
		MagnetiteArmorMaterial.onEntityTick(this.as());
	}
}