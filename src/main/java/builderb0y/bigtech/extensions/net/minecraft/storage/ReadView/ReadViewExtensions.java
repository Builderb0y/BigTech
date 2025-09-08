package builderb0y.bigtech.extensions.net.minecraft.storage.ReadView;

import java.util.Optional;
import java.util.UUID;
import java.util.function.IntPredicate;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.extensions.net.minecraft.nbt.NbtCompound.NbtCompoundExtensions;

@Extension
public class ReadViewExtensions {

	public static <T> Optional<T> getArray(@This ReadView view, String key, T[] values) {
		int index = view.getInt(key, -1);
		if (index >= 0 && index < values.length) {
			return Optional.of(values[index]);
		}
		return Optional.empty();
	}

	public static short getShortProperly(@This ReadView view, String key, short fallback) {
		return (short)(view.getShort(key, fallback));
	}

	public static Optional<float[]> getFloatArray(@This ReadView view, String key, IntPredicate lengthChecker) {
		int[] ints = view.getOptionalIntArray(key).orElse(null);
		if (ints != null && lengthChecker.test(ints.length)) {
			return Optional.of(NbtCompoundExtensions.toFloatArray(ints));
		}
		return Optional.empty();
	}

	public static Optional<float[]> getFloatArray(@This ReadView view, String key, int length) {
		int[] ints = view.getOptionalIntArray(key).orElse(null);
		if (ints != null && ints.length == length) {
			return Optional.of(NbtCompoundExtensions.toFloatArray(ints));
		}
		return Optional.empty();
	}

	public static Optional<float[]> getFloatArray(@This ReadView view, String key) {
		int[] ints = view.getOptionalIntArray(key).orElse(null);
		if (ints != null) {
			return Optional.of(NbtCompoundExtensions.toFloatArray(ints));
		}
		return Optional.empty();
	}

	public static Optional<double[]> getDoubleArray(@This ReadView view, String key, IntPredicate lengthChecker) {
		long[] longs = view.getOptionalLongArray(key).orElse(null);
		if (longs != null && lengthChecker.test(longs.length)) {
			return Optional.of(NbtCompoundExtensions.toDoubleArray(longs));
		}
		return Optional.empty();
	}

	public static Optional<double[]> getDoubleArray(@This ReadView view, String key, long length) {
		long[] longs = view.getOptionalLongArray(key).orElse(null);
		if (longs != null && longs.length == length) {
			return Optional.of(NbtCompoundExtensions.toDoubleArray(longs));
		}
		return Optional.empty();
	}

	public static Optional<double[]> getDoubleArray(@This ReadView view, String key) {
		long[] longs = view.getOptionalLongArray(key).orElse(null);
		if (longs != null) {
			return Optional.of(NbtCompoundExtensions.toDoubleArray(longs));
		}
		return Optional.empty();
	}

	public static ItemStack getItemStack(@This ReadView view, String key) {
		return view.read(key, ItemStack.CODEC).orElse(ItemStack.EMPTY);
	}

	public static Optional<UUID> getUUID(@This ReadView view, String key) {
		return view.read(key, Uuids.CODEC);
	}

	public static Optional<Identifier> getIdentifier(@This ReadView view, String key) {
		return view.read(key, Identifier.CODEC);
	}

	public static Optional<BlockPos> getBlockPos(@This ReadView view, String key) {
		return view.getOptionalIntArray(key).flatMap(NbtCompoundExtensions::toBlockPos);
	}
}