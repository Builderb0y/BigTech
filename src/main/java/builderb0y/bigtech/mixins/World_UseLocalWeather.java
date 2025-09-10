package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.api.LocalWeather;

@Mixin(World.class)
public abstract class World_UseLocalWeather {

	@Shadow protected abstract boolean canHaveWeather();

	@Redirect(method = "getPrecipitation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isRaining()Z"))
	private boolean bigtech_useLocalWeather(World instance, @Local(argsOnly = true) BlockPos pos) {
		return this.canHaveWeather() && LocalWeather.getPrecipitationAt(instance, 1.0F, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 0.2F;
	}
}