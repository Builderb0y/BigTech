package builderb0y.bigtech.mixins;

import java.util.List;
import java.util.Set;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import builderb0y.bigtech.entities.BigTechEntityTypes;
import builderb0y.bigtech.entities.StormCloudEntity;
import builderb0y.bigtech.mixinterfaces.SilverIodideProjectile;
import builderb0y.bigtech.networking.StormCloudStateUpdatePacket;
import builderb0y.bigtech.util.BigTechMath;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocket_ImplementSilverIodideProjectile extends Entity implements SilverIodideProjectile {

	@Unique
	public Type bigtech_type;

	public FireworkRocket_ImplementSilverIodideProjectile() {
		super(null, null);
	}

	@ModifyExpressionValue(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;getExplosions()Ljava/util/List;"))
	private List<FireworkExplosionComponent> bigtech_changeWeather(List<FireworkExplosionComponent> original) {
		if (this.getWorld() instanceof ServerWorld world && world.isSkyVisible(this.getBlockPos())) {
			Type type = this.bigtech_getProjectileType();
			if (type != Type.NONE) {
				Set<StormCloudEntity> clouds = StormCloudEntity.ACTIVE.get(world);
				if (clouds != null && !clouds.isEmpty()) {
					for (StormCloudEntity cloud : clouds) {
						if (BigTechMath.square(this.getX() - cloud.getX(), this.getY() - cloud.getY(), this.getZ() - cloud.getZ()) < cloud.radiusSquared) {
							if (type == Type.MORE_RAINY) {
								cloud.radiusSquared += StormCloudEntity.ADDED_RADIUS_SQUARED_BY_SILVER_IODIDE_CANNON * original.size();
							}
							else {
								cloud.radiusSquared -= StormCloudEntity.ADDED_RADIUS_SQUARED_BY_SILVER_IODIDE_CANNON * original.size();
							}
							StormCloudStateUpdatePacket.send(cloud);
							return original;
						}
					}
				}
				if (type == Type.MORE_RAINY) {
					StormCloudEntity cloud = new StormCloudEntity(BigTechEntityTypes.STORM_CLOUD, world);
					cloud.setPosition(this.getPos());
					cloud.radiusSquared = StormCloudEntity.ADDED_RADIUS_SQUARED_BY_SILVER_IODIDE_CANNON * original.size();
					world.spawnEntity(cloud);
				}
			}
		}
		return original;
	}

	@Override
	public Type bigtech_getProjectileType() {
		Type type = this.bigtech_type;
		return type != null ? type : Type.NONE;
	}

	@Override
	public void bigtech_setProjectileType(Type type) {
		this.bigtech_type = type;
	}

	@Inject(method = "writeCustomData", at = @At("RETURN"))
	private void bigtech_writeType(WriteView view, CallbackInfo callback) {
		view.putByte("bigtech_type", (byte)(this.bigtech_getProjectileType().ordinal()));
	}

	@Inject(method = "readCustomData", at = @At("RETURN"))
	private void bigtech_readType(ReadView view, CallbackInfo callback) {
		this.bigtech_type = view.getArray("bigtech_type", Type.VALUES).orElse(Type.NONE);
	}
}