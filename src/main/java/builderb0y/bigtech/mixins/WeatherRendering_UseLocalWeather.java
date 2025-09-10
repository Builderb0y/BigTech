package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.WeatherRendering;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import builderb0y.bigtech.api.LocalWeather;

@Mixin(WeatherRendering.class)
public class WeatherRendering_UseLocalWeather {

	@ModifyExpressionValue(
		method = "renderPrecipitation(Lnet/minecraft/world/World;Lnet/minecraft/client/render/VertexConsumerProvider;IFLnet/minecraft/util/math/Vec3d;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getRainGradient(F)F")
	)
	private float bigtech_useLocalWeatherForRainGradient(float original, @Local(argsOnly = true) World world, @Local(argsOnly = true) Vec3d pos) {
		return LocalWeather.adjustPrecipitation(world, original, pos.x, pos.y, pos.z);
	}
}