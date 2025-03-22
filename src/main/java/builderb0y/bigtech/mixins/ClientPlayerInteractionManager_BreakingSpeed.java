package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.network.ClientPlayerInteractionManager;

import builderb0y.bigtech.tweaks.PlacementSpeed;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManager_BreakingSpeed {

	@ModifyConstant(method = { "updateBlockBreakingProgress", "attackBlock" }, constant = @Constant(intValue = 5), expect = 2)
	private int bigtech_overrideBreakSpeed(int original) {
		return PlacementSpeed.getBreakDelay();
	}
}