package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtEnd;

import builderb0y.bigtech.BigTechMod;

@Mixin(NbtCompound.class)
public class Dev_NbtCompound_SanityCheck {

	@Unique
	private static final boolean logInvalidElements = Boolean.getBoolean("bigtech.nbtSanity.log");

	@Inject(method = "put", at = @At("HEAD"), cancellable = true)
	private void bigtech_sanityCheckValue(String key, NbtElement element, CallbackInfoReturnable<NbtElement> callback) {
		if (element == null || element instanceof NbtEnd) {
			if (logInvalidElements) {
				BigTechMod.LOGGER.warn("", new IllegalArgumentException("Someone tried to put ${element} in an NbtCompound!"));
			}
			callback.setReturnValue(null);
		}
	}
}