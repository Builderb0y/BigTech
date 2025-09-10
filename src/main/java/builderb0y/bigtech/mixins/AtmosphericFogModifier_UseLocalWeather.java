package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.fog.AtmosphericFogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.api.LocalWeather;

@Mixin(AtmosphericFogModifier.class)
public class AtmosphericFogModifier_UseLocalWeather {

	@ModifyExpressionValue(method = "applyStartEndModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"))
	private float bigtech_useLocalWeather(float original, @Local(argsOnly = true) ClientWorld world, @Local(argsOnly = true) BlockPos cameraPos) {
		return LocalWeather.adjustPrecipitation(world, original, cameraPos.getX() + 0.5D, cameraPos.getY() + 0.5D, cameraPos.getZ() + 0.5D);
	}
}