package builderb0y.bigtech.extensions.net.minecraft.nbt.NbtCompound;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DataResult.Error;
import com.mojang.serialization.DataResult.Success;
import com.mojang.serialization.DynamicOps;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Self;
import manifold.ext.rt.api.This;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.util.NbtReadingException;

@Extension
public class NbtCompoundExtensions {

	public static <T> Optional<T> getArray(@This NbtCompound thiz, String key, T[] array) {
		if (thiz.get(key) instanceof AbstractNbtNumber number) {
			int index = number.intValue();
			if (index >= 0 && index < array.length) {
				return Optional.of(array[index]);
			}
		}
		return Optional.empty();
	}

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

	public static NbtElement require(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.get(key);
		if (element != null) return element;
		else throw new NbtReadingException("No such key " + key + " in NBT compound " + thiz);
	}

	public static byte requireByte(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof AbstractNbtNumber number) return number.byteValue();
		else throw new NbtReadingException("Expected " + key + " to be a byte, but it was a " + element);
	}

	public static short requireShort(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof AbstractNbtNumber number) return number.shortValue();
		else throw new NbtReadingException("Expected " + key + " to be a short, but it was a " + element);
	}

	public static int requireInt(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof AbstractNbtNumber number) return number.intValue();
		else throw new NbtReadingException("Expected " + key + " to be an int, but it was a " + element);
	}

	public static long requireLong(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof AbstractNbtNumber number) return number.longValue();
		else throw new NbtReadingException("Expected " + key + " to be a long, but it was a " + element);
	}

	public static float requireFloat(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof AbstractNbtNumber number) return number.floatValue();
		else throw new NbtReadingException("Expected " + key + " to be a float, but it was a " + element);
	}

	public static double requireDouble(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof AbstractNbtNumber number) return number.doubleValue();
		else throw new NbtReadingException("Expected " + key + " to be a double, but it was a " + element);
	}

	public static boolean requireBoolean(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof AbstractNbtNumber number) return number.intValue() != 0;
		else throw new NbtReadingException("Expected " + key + " to be a boolean, but it was a " + element);
	}

	public static String requireString(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof NbtString(String value)) return value;
		else throw new NbtReadingException("Expected " + key + " to be a string, but it was a " + element);
	}

	public static byte[] requireByteArray(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof NbtByteArray array) return array.getByteArray();
		else throw new NbtReadingException("Expected " + key + " to be a byte array, but it was a " + element);
	}

	public static int[] requireIntArray(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof NbtIntArray array) return array.getIntArray();
		else throw new NbtReadingException("Expected " + key + " to be an int array, but it was a " + element);
	}

	public static long[] requireLongArray(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof NbtLongArray array) return array.getLongArray();
		else throw new NbtReadingException("Expected " + key + " to be a long array, but it was a " + element);
	}

	public static NbtCompound requireCompound(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof NbtCompound compound) return compound;
		else throw new NbtReadingException("Expected " + key + " to be an NBT compound, but it was a " + element);
	}

	public static NbtList requireList(@This NbtCompound thiz, String key) throws NbtReadingException {
		NbtElement element = thiz.require(key);
		if (element instanceof NbtList list) return list;
		else throw new NbtReadingException("Expected " + key + " to be an NBT list, but it was a " + element);
	}

	public static <T> T convert(NbtElement element, Codec<T> codec) throws NbtReadingException {
		return convert(element, codec, NbtOps.INSTANCE);
	}

	public static <T> T convert(NbtElement element, Codec<T> codec, DynamicOps<NbtElement> ops) throws NbtReadingException {
		return switch (codec.parse(ops, element)) {
			case Success<T> success -> success.value();
			case Error<T> error -> throw new NbtReadingException(error.message());
		};
	}

	public static Optional<ItemStack> getItemStack(@This NbtCompound thiz, String key, RegistryWrapper.WrapperLookup registries) {
		return thiz.get(key, ItemStack.CODEC, RegistryOps.of(NbtOps.INSTANCE, registries));
	}

	public static ItemStack requireItemStack(@This NbtCompound thiz, String key, RegistryWrapper.WrapperLookup registries) throws NbtReadingException {
		return convert(thiz.require(key), ItemStack.CODEC, RegistryOps.of(NbtOps.INSTANCE, registries));
	}

	public static void putItemStack(@This NbtCompound thiz, String key, ItemStack stack, RegistryWrapper.WrapperLookup registries) {
		thiz.put(key, ItemStack.CODEC, RegistryOps.of(NbtOps.INSTANCE, registries), stack);
	}

	public static @Self NbtCompound withItemStack(@This NbtCompound thiz, String key, ItemStack stack, RegistryWrapper.WrapperLookup registries) {
		thiz.putItemStack(key, stack, registries);
		return thiz;
	}

	public static Optional<UUID> getUuid(@This NbtCompound thiz, String key) {
		return thiz.get(key, Uuids.CODEC);
	}

	public static UUID requireUuid(@This NbtCompound thiz, String key) throws NbtReadingException {
		return convert(thiz.require(key), Uuids.CODEC);
	}

	public static void putUuid(@This NbtCompound thiz, String key, UUID uuid) {
		thiz.put(key, Uuids.CODEC, uuid);
	}

	public static @Self NbtCompound withUuid(@This NbtCompound thiz, String key, UUID uuid) {
		thiz.putUuid(key, uuid);
		return thiz;
	}

	public static float[] toFloatArray(int[] ints) {
		int length = ints.length;
		float[] floats = new float[length];
		for (int index = 0; index < length; index++) {
			floats[index] = Float.intBitsToFloat(ints[index]);
		}
		return floats;
	}

	public static int[] toIntArray(float[] floats) {
		int length = floats.length;
		int[] ints = new int[length];
		for (int index = 0; index < length; index++) {
			ints[index] = Float.floatToRawIntBits(floats[index]);
		}
		return ints;
	}

	public static Optional<float[]> getFloatArray(@This NbtCompound thiz, String key) {
		return thiz.getIntArray(key).map(NbtCompoundExtensions::toFloatArray);
	}

	public static float[] requireFloatArray(@This NbtCompound thiz, String key) throws NbtReadingException {
		return toFloatArray(thiz.requireIntArray(key));
	}

	public static void putFloatArray(@This NbtCompound thiz, String key, float[] floats) {
		thiz.putIntArray(key, toIntArray(floats));
	}

	public static @Self NbtCompound withFloatArray(@This NbtCompound thiz, String key, float[] floats) {
		thiz.putFloatArray(key, floats);
		return thiz;
	}

	public static double[] toDoubleArray(long[] longs) {
		int length = longs.length;
		double[] doubles = new double[length];
		for (int index = 0; index < length; index++) {
			doubles[index] = Double.longBitsToDouble(longs[index]);
		}
		return doubles;
	}

	public static long[] toLongArray(double[] doubles) {
		int length = doubles.length;
		long[] longs = new long[length];
		for (int index = 0; index < length; index++) {
			longs[index] = Double.doubleToRawLongBits(doubles[index]);
		}
		return longs;
	}

	public static Optional<double[]> getDoubleArray(@This NbtCompound thiz, String key) {
		return thiz.getLongArray(key).map(NbtCompoundExtensions::toDoubleArray);
	}

	public static double[] requireDoubleArray(@This NbtCompound thiz, String key) throws NbtReadingException {
		return toDoubleArray(thiz.requireLongArray(key));
	}

	public static void putDoubleArray(@This NbtCompound thiz, String key, double[] doubles) {
		thiz.putLongArray(key, toLongArray(doubles));
	}

	public static @Self NbtCompound withDoubleArray(@This NbtCompound thiz, String key, double[] doubles) {
		thiz.putDoubleArray(key, doubles);
		return thiz;
	}

	public static Optional<Identifier> getIdentifier(@This NbtCompound thiz, String key) {
		return thiz.get(key, Identifier.CODEC);
	}

	public static Identifier requireIdentifier(@This NbtCompound thiz, String key) throws NbtReadingException {
		return convert(thiz.require(key), Identifier.CODEC);
	}

	public static void putIdentifier(@This NbtCompound thiz, String key, Identifier value) {
		thiz.putString(key, value.toString());
	}

	public static @Self NbtCompound withIdentifier(@This NbtCompound thiz, String key, Identifier identifier) {
		thiz.putIdentifier(key, identifier);
		return thiz;
	}

	public static Optional<BlockPos> getBlockPos(@This NbtCompound thiz, String key) {
		return thiz.getIntArray(key).flatMap((int[] array) -> {
			if (array.length == 3) {
				return Optional.of(new BlockPos(array[0], array[1], array[2]));
			}
			else {
				BigTechMod.LOGGER.warn("Expected block pos tag to be of length 3, but it was ${array.length}");
				return Optional.empty();
			}
		});
	}

	public static BlockPos requireBlockPos(@This NbtCompound thiz, String key) throws NbtReadingException {
		int[] ints = thiz.requireIntArray(key);
		if (ints.length == 3) return new BlockPos(ints[0], ints[1], ints[2]);
		else throw new NbtReadingException("Expected block pos tag to be of length 3, but it was ${ints.length}");
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

	public static Optional<BlockState> getBlockState(@This NbtCompound thiz, String key) {
		return thiz.getString(key).map((String value) -> {
			if (!value.isEmpty()) try {
				return BlockArgumentParser.block(Registries.BLOCK, value, false).blockState();
			}
			catch (CommandSyntaxException ignored) {}
			return Blocks.AIR.getDefaultState();
		});
	}

	public static BlockState requireBlockState(@This NbtCompound thiz, String key) throws NbtReadingException {
		String string = thiz.requireString(key);
		try {
			return BlockArgumentParser.block(Registries.BLOCK, string, false).blockState();
		}
		catch (CommandSyntaxException exception) {
			throw new NbtReadingException(exception);
		}
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