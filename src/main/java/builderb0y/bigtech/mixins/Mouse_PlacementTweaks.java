package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;

import builderb0y.bigtech.networking.CycleBlockstatePacket;
import builderb0y.bigtech.tweaks.PlacementSpeed;

@Mixin(Mouse.class)
public class Mouse_PlacementTweaks {

	@Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;"), cancellable = true)
	private void bigtech_cycleBlockstate(long window, double horizontal, double vertical, CallbackInfo callback) {
		if (vertical != 0.0D) {
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null && !player.isSpectator()) {
				if (PlacementSpeed.CYCLE_STATE.isPressed()) {
					if (player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof BlockItem) {
						CycleBlockstatePacket.INSTANCE.send(false, vertical < 0.0D);
						callback.cancel();
					}
					else if (player.getStackInHand(Hand.OFF_HAND).getItem() instanceof BlockItem) {
						CycleBlockstatePacket.INSTANCE.send(true, vertical < 0.0D);
						callback.cancel();
					}
				}
				if (PlacementSpeed.PLACE_DELAY.isPressed()) {
					PlacementSpeed.changePlaceDelay(vertical > 0.0D);
					callback.cancel();
				}
				if (PlacementSpeed.BREAK_DELAY.isPressed()) {
					PlacementSpeed.changeBreakDelay(vertical > 0.0D);
					callback.cancel();
				}
			}
		}
	}
}