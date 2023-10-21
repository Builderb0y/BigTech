package builderb0y.bigtech.extensions.net.minecraft.nbt.NbtCompound;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Self;
import manifold.ext.rt.api.This;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

@Extension
public class NbtCompoundExtensions {

	public static @Self NbtCompound withByte(@This NbtCompound thiz, String key, byte value) {
		thiz.putByte(key, value);
		return thiz;
	}

	public static @Self NbtCompound withShort(@This NbtCompound thiz, String key, short value) {
		thiz.putShort(key, value);
		return thiz;
	}

	public static @Self NbtCompound withInt(@This NbtCompound thiz, String key, int value) {
		thiz.putInt(key, value);
		return thiz;
	}

	public static @Self NbtCompound withLong(@This NbtCompound thiz, String key, long value) {
		thiz.putLong(key, value);
		return thiz;
	}

	public static @Self NbtCompound withFloat(@This NbtCompound thiz, String key, float value) {
		thiz.putFloat(key, value);
		return thiz;
	}

	public static @Self NbtCompound withDouble(@This NbtCompound thiz, String key, double value) {
		thiz.putDouble(key, value);
		return thiz;
	}

	public static @Self NbtCompound withString(@This NbtCompound thiz, String key, String value) {
		thiz.putString(key, value);
		return thiz;
	}

	public static @Self NbtCompound withByteArray(@This NbtCompound thiz, String key, byte[] value) {
		thiz.putByteArray(key, value);
		return thiz;
	}

	public static @Self NbtCompound withIntArray(@This NbtCompound thiz, String key, int[] value) {
		thiz.putIntArray(key, value);
		return thiz;
	}

	public static @Self NbtCompound withLongArray(@This NbtCompound thiz, String key, long[] value) {
		thiz.putLongArray(key, value);
		return thiz;
	}

	public static @Self NbtCompound with(@This NbtCompound thiz, String key, NbtElement value) {
		thiz.put(key, value);
		return thiz;
	}
}