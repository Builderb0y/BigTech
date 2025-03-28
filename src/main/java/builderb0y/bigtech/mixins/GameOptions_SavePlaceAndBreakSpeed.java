package builderb0y.bigtech.mixins;

import java.io.PrintWriter;
import java.util.Optional;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.option.GameOptions;
import net.minecraft.nbt.NbtCompound;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.tweaks.PlacementSpeed;

@Mixin(GameOptions.class)
public class GameOptions_SavePlaceAndBreakSpeed {

	@Inject(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getFullscreenResolution()Ljava/lang/String;"))
	private void bigtech_writePlaceAndBreakSpeed(CallbackInfo callback, @Local PrintWriter printWriter) {
		printWriter.println("bigtech_creativePlaceDelay:" + PlacementSpeed.creativePlaceDelay);
		printWriter.println("bigtech_survivalPlaceDelay:" + PlacementSpeed.survivalPlaceDelay);
		printWriter.println("bigtech_creativeBreakDelay:" + PlacementSpeed.creativeBreakDelay);
		printWriter.println("bigtech_survivalBreakDelay:" + PlacementSpeed.survivalBreakDelay);
	}

	@Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;updateKeysByCode()V"))
	private void bigtech_loadPlaceAndBreakSpeed(CallbackInfo callback, @Local(index = 2) NbtCompound nbt) {
		Optional<String> tmp;
		if ((tmp = nbt.getString("bigtech_creativePlaceDelay")).isPresent()) try { PlacementSpeed.creativePlaceDelay = Integer.parseInt(tmp.get()); } catch (NumberFormatException exception) { BigTechMod.LOGGER.error("Invalid creativePlaceDelay in options.txt", exception); }
		if ((tmp = nbt.getString("bigtech_survivalPlaceDelay")).isPresent()) try { PlacementSpeed.survivalPlaceDelay = Integer.parseInt(tmp.get()); } catch (NumberFormatException exception) { BigTechMod.LOGGER.error("Invalid survivalPlaceDelay in options.txt", exception); }
		if ((tmp = nbt.getString("bigtech_creativeBreakDelay")).isPresent()) try { PlacementSpeed.creativeBreakDelay = Integer.parseInt(tmp.get()); } catch (NumberFormatException exception) { BigTechMod.LOGGER.error("Invalid creativeBreakDelay in options.txt", exception); }
		if ((tmp = nbt.getString("bigtech_survivalBreakDelay")).isPresent()) try { PlacementSpeed.survivalBreakDelay = Integer.parseInt(tmp.get()); } catch (NumberFormatException exception) { BigTechMod.LOGGER.error("Invalid survivalBreakDelay in options.txt", exception); }
	}
}