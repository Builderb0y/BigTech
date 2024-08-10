package builderb0y.bigtech.extensions.net.minecraft.nbt.NbtCompound;

import java.util.UUID;
import java.util.function.Consumer;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Self;
import manifold.ext.rt.api.This;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

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

	public static @Self NbtCompound withBoolean(@This NbtCompound thiz, String key, boolean value) {
		thiz.putBoolean(key, value);
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

	public static @Self NbtCompound withUuid(@This NbtCompound thiz, String key, UUID uuid) {
		thiz.putUuid(key, uuid);
		return thiz;
	}

	public static float[] getFloatArray(@This NbtCompound thiz, String key) {
		int[] ints = thiz.getIntArray(key);
		int length = ints.length;
		float[] floats = new float[length];
		for (int index = 0; index < length; index++) {
			floats[index] = Float.intBitsToFloat(ints[index]);
		}
		return floats;
	}

	public static void putFloatArray(@This NbtCompound thiz, String key, float[] floats) {
		int length = floats.length;
		int[] ints = new int[length];
		for (int index = 0; index < length; index++) {
			ints[index] = Float.floatToRawIntBits(floats[index]);
		}
		thiz.putIntArray(key, ints);
	}

	public static @Self NbtCompound withFloatArray(@This NbtCompound thiz, String key, float[] floats) {
		thiz.putFloatArray(key, floats);
		return thiz;
	}

	public static double[] getDoubleArray(@This NbtCompound thiz, String key) {
		long[] longs = thiz.getLongArray(key);
		int length = longs.length;
		double[] doubles = new double[length];
		for (int index = 0; index < length; index++) {
			doubles[index] = Double.longBitsToDouble(longs[index]);
		}
		return doubles;
	}

	public static void putDoubleArray(@This NbtCompound thiz, String key, double[] doubles) {
		int length = doubles.length;
		long[] longs = new long[length];
		for (int index = 0; index < length; index++) {
			longs[index] = Double.doubleToRawLongBits(doubles[index]);
		}
		thiz.putLongArray(key, longs);
	}

	public static @Self NbtCompound withDoubleArray(@This NbtCompound thiz, String key, double[] doubles) {
		thiz.putDoubleArray(key, doubles);
		return thiz;
	}

	public static Identifier getIdentifier(@This NbtCompound thiz, String key) {
		return Identifier.of(thiz.getString(key));
	}

	public static void putIdentifier(@This NbtCompound thiz, String key, Identifier value) {
		thiz.putString(key, value.toString());
	}

	public static @Self NbtCompound withIdentifier(@This NbtCompound thiz, String key, Identifier identifier) {
		thiz.putIdentifier(key, identifier);
		return thiz;
	}

	public static BlockPos getBlockPos(@This NbtCompound thiz, String key) {
		int[] array = thiz.getIntArray(key);
		if (array.length == 3) {
			return new BlockPos(array[0], array[1], array[2]);
		}
		else {
			throw new IllegalArgumentException("Expected block pos tag to be of length 3, but it was ${array.length}");
		}
	}

	public static void putBlockPos(@This NbtCompound thiz, String key, BlockPos value) {
		thiz.putIntArray(key, new int[] { value.getX(), value.getY(), value.getZ() });
	}

	public static @Self NbtCompound withBlockPos(@This NbtCompound thiz, String key, BlockPos value) {
		thiz.putBlockPos(key, value);
		return thiz;
	}

	public static void putBlockState(@This NbtCompound thiz, String key, BlockState state) {
		thiz.putString(key, BlockArgumentParser.stringifyBlockState(state));
	}

	public static @Self NbtCompound withBlockState(@This NbtCompound thiz, String key, BlockState state) {
		thiz.putBlockState(key, state);
		return thiz;
	}

	public static BlockState getBlockState(@This NbtCompound thiz, String key) {
		String value = thiz.getString(key);
		if (!value.isEmpty()) try {
			return BlockArgumentParser.block(Registries.BLOCK.getReadOnlyWrapper(), value, false).blockState();
		}
		catch (CommandSyntaxException ignored) {}
		return Blocks.AIR.getDefaultState();
	}

	public static NbtCompound createSubCompound(@This NbtCompound thiz, String key) {
		NbtCompound result = new NbtCompound();
		thiz.put(key, result);
		return result;
	}

	public static NbtList createSubList(@This NbtCompound thiz, String key) {
		NbtList result = new NbtList();
		thiz.put(key, result);
		return result;
	}

	public static void putSubCompound(@This NbtCompound thiz, String key, Consumer<NbtCompound> filler) {
		filler.accept(thiz.createSubCompound(key));
	}

	public static void putSubList(@This NbtCompound thiz, String key, Consumer<NbtList> filler) {
		filler.accept(thiz.createSubList(key));
	}

	public static @Self NbtCompound withSubCompound(@This NbtCompound thiz, String key, Consumer<NbtCompound> filler) {
		thiz.putSubCompound(key, filler);
		return thiz;
	}

	public static @Self NbtCompound withSubList(@This NbtCompound thiz, String key, Consumer<NbtList> filler) {
		thiz.putSubList(key, filler);
		return thiz;
	}
}