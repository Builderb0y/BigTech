package builderb0y.bigtech.mixins;

import java.io.PrintWriter;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.option.GameOptions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.tweaks.PlacementSpeed;

@Mixin(GameOptions.class)
public class GameOptions_SavePlaceAndBreakSpeed {

	@Inject(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getWindow()Lnet/minecraft/client/util/Window;"))
	private void bigtech_writePlaceAndBreakSpeed(CallbackInfo callback, @Local PrintWriter printWriter) {
		printWriter.println("bigtech_creativePlaceDelay:" + PlacementSpeed.creativePlaceDelay);
		printWriter.println("bigtech_survivalPlaceDelay:" + PlacementSpeed.survivalPlaceDelay);
		printWriter.println("bigtech_creativeBreakDelay:" + PlacementSpeed.creativeBreakDelay);
		printWriter.println("bigtech_survivalBreakDelay:" + PlacementSpeed.survivalBreakDelay);
	}

	@Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;updateKeysByCode()V"))
	private void bigtech_loadPlaceAndBreakSpeed(CallbackInfo callback, @Local(index = 2) NbtCompound nbt) {
		NbtElement tmp;
		if ((tmp = nbt.get("bigtech_creativePlaceDelay")) != null) try { PlacementSpeed.creativePlaceDelay = Integer.parseInt(tmp.asString()); } catch (NumberFormatException exception) { BigTechMod.LOGGER.error("Invalid creativePlaceDelay in options.txt", exception); }
		if ((tmp = nbt.get("bigtech_survivalPlaceDelay")) != null) try { PlacementSpeed.survivalPlaceDelay = Integer.parseInt(tmp.asString()); } catch (NumberFormatException exception) { BigTechMod.LOGGER.error("Invalid survivalPlaceDelay in options.txt", exception); }
		if ((tmp = nbt.get("bigtech_creativeBreakDelay")) != null) try { PlacementSpeed.creativeBreakDelay = Integer.parseInt(tmp.asString()); } catch (NumberFormatException exception) { BigTechMod.LOGGER.error("Invalid creativeBreakDelay in options.txt", exception); }
		if ((tmp = nbt.get("bigtech_survivalBreakDelay")) != null) try { PlacementSpeed.survivalBreakDelay = Integer.parseInt(tmp.asString()); } catch (NumberFormatException exception) { BigTechMod.LOGGER.error("Invalid survivalBreakDelay in options.txt", exception); }
	}
}