package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;

@Mixin(MobEntity.class)
public abstract class MobEntity_RandomizeStats extends LivingEntity {

	public MobEntity_RandomizeStats() {
		super(null, null);
	}

	@Inject(method = "initialize", at = @At("HEAD"))
	private void bigtech_randomizeStats(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> callback) {
		if (spawnReason != SpawnReason.LOAD && spawnReason != SpawnReason.DIMENSION_TRAVEL) {
			Random random = world.getRandom();
			double base = random.nextTriangular(difficulty.getClampedLocalDifficulty() - 0.5D, 0.5D);
			this.bigtech_randomize(random, base, EntityAttributes.SCALE, 0.5D);
			this.bigtech_randomize(random, base, EntityAttributes.MAX_HEALTH, 1.0D);
			this.bigtech_randomize(random, base, EntityAttributes.FLYING_SPEED, 0.5D);
			this.bigtech_randomize(random, base, EntityAttributes.ATTACK_DAMAGE, 0.75D);
			this.bigtech_randomize(random, base, EntityAttributes.MOVEMENT_SPEED, 0.5D);
		}
	}

	@Unique
	private void bigtech_randomize(Random random, double base, RegistryEntry<EntityAttribute> attribute, double scale) {
		EntityAttributeInstance instance = this.getAttributeInstance(attribute);
		if (instance != null) {
			instance.setBaseValue(instance.getBaseValue() * Math.exp(random.nextTriangular(base, 0.5D) * Math.log(2.0D) * scale));
		}
	}
}