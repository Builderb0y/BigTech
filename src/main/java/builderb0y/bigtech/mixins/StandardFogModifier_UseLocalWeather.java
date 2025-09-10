package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.StandardFogModifier;
import net.minecraft.client.world.ClientWorld;

import builderb0y.bigtech.api.LocalWeather;

@Mixin(StandardFogModifier.class)
public class StandardFogModifier_UseLocalWeather {

	@ModifyExpressionValue(
		method = "getFogColor",
		at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderGradient(F)F")
		}
	)
	private float bigtech_useLocalWeather(float original, @Local(argsOnly = true) ClientWorld world, @Local(argsOnly = true) Camera camera) {
		return LocalWeather.adjustPrecipitation(world, original, camera.getPos().x, camera.getPos().y, camera.getPos().z);
	}
}