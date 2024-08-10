package builderb0y.bigtech.extensions.net.minecraft.network.RegistryByteBuf;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Self;
import manifold.ext.rt.api.This;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.encoding.VarInts;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

@Extension
public class RegistryByteBufExtensions {

	public static <T> @Self RegistryByteBuf writeRegistryValue(@This RegistryByteBuf thiz, RegistryKey<? extends Registry<T>> key, T value) {
		VarInts.write(thiz, thiz.getRegistryManager().get(key).getRawIdOrThrow(value));
		return thiz;
	}

	public static <T> T readRegistryValue(@This RegistryByteBuf thiz, RegistryKey<? extends Registry<T>> key) {
		return thiz.getRegistryManager().get(key).getOrThrow(VarInts.read(thiz));
	}
}