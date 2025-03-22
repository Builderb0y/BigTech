package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.MinecraftClient;

import builderb0y.bigtech.tweaks.PlacementSpeed;

@Mixin(MinecraftClient.class)
public class MinecraftClient_PlacementSpeed {

	@ModifyConstant(method = "doItemUse", constant = @Constant(intValue = 4))
	private int bigtech_overridePlacementSpeed(int original) {
		return PlacementSpeed.getPlaceDelay();
	}
}