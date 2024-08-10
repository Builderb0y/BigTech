package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import builderb0y.bigtech.mixinterfaces.SilverIodideProjectile;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocket_ImplementSilverIodideProjectile extends Entity implements SilverIodideProjectile {

	@Unique
	public Type bigtech_type;

	public FireworkRocket_ImplementSilverIodideProjectile() {
		super(null, null);
	}

	@Inject(method = "explode", at = @At("HEAD"))
	private void bigtech_changeWeather(CallbackInfo callback) {
		if (this.getWorld() instanceof ServerWorld world && world.isSkyVisible(this.getBlockPos())) {
			switch (this.bigtech_getProjectileType()) {
				case NONE -> {}
				case LESS_RAINY -> {
					if (world.isThundering()) {
						world.setWeather(0, ServerWorld.RAIN_WEATHER_DURATION_PROVIDER.get(world.random), true, false);
					}
					else {
						world.setWeather(ServerWorld.CLEAR_WEATHER_DURATION_PROVIDER.get(world.random), 0, false, false);
					}
				}
				case MORE_RAINY -> {
					if (world.isRaining()) {
						world.setWeather(0, ServerWorld.THUNDER_WEATHER_DURATION_PROVIDER.get(world.random), true, true);
					}
					else {
						world.setWeather(0, ServerWorld.RAIN_WEATHER_DURATION_PROVIDER.get(world.random), true, false);
					}
				}
			}
		}
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

	@Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
	private void bigtech_writeType(NbtCompound nbt, CallbackInfo callback) {
		nbt.putByte("bigtech_type", (byte)(this.bigtech_getProjectileType().ordinal()));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
	private void bigtech_readType(NbtCompound nbt, CallbackInfo callback) {
		this.bigtech_type = Type.VALUES[nbt.getByte("bigtech_type")];
	}
}