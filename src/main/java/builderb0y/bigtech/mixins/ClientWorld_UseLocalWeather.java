package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import builderb0y.bigtech.api.LocalWeather;

@Mixin(ClientWorld.class)
public class ClientWorld_UseLocalWeather {

	@Shadow @Final private MinecraftClient client;

	@ModifyExpressionValue(
		method = "getSkyColor",
		at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderGradient(F)F")
		}
	)
	private float bigtech_useLocalWeather(float original, @Local(argsOnly = true) Vec3d pos) {
		return LocalWeather.adjustPrecipitation((World)(Object)(this), original, pos.x, pos.y, pos.z);
	}

	@ModifyExpressionValue(
		method = "getCloudsColor",
		at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderGradient(F)F")
		}
	)
	private float bigtech_useLocalWeather(float original) {
		Vec3d pos = this.client.gameRenderer.getCamera().getPos();
		return LocalWeather.adjustPrecipitation((World)(Object)(this), original, pos.x, pos.y, pos.z);
	}
}