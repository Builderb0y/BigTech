package builderb0y.bigtech.extensions.net.minecraft.storage.WriteView;

import java.util.UUID;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Self;
import manifold.ext.rt.api.This;

import net.minecraft.item.ItemStack;
import net.minecraft.storage.WriteView;
import net.minecraft.storage.WriteView.ListAppender;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.extensions.net.minecraft.nbt.NbtCompound.NbtCompoundExtensions;

@Extension
public class WriteViewExtensions {

	public static @Self WriteView withByte(@This WriteView view, String key, byte value) {
		view.putByte(key, value);
		return view;
	}

	public static @Self WriteView withShort(@This WriteView view, String key, short value) {
		view.putShort(key, value);
		return view;
	}

	public static @Self WriteView withInt(@This WriteView view, String key, int value) {
		view.putInt(key, value);
		return view;
	}

	public static @Self WriteView withLong(@This WriteView view, String key, long value) {
		view.putLong(key, value);
		return view;
	}

	public static @Self WriteView withFloat(@This WriteView view, String key, float value) {
		view.putFloat(key, value);
		return view;
	}

	public static @Self WriteView withDouble(@This WriteView view, String key, double value) {
		view.putDouble(key, value);
		return view;
	}

	public static @Self WriteView withBoolean(@This WriteView view, String key, boolean value) {
		view.putBoolean(key, value);
		return view;
	}

	public static @Self WriteView withString(@This WriteView view, String key, String value) {
		view.putString(key, value);
		return view;
	}

	public static @Self WriteView withByteArray(@This WriteView view, String key, byte[] value) {
		view.putByteArray(key, value);
		return view;
	}

	public static @Self WriteView withIntArray(@This WriteView view, String key, int[] value) {
		view.putIntArray(key, value);
		return view;
	}

	public static @Self WriteView withLongArray(@This WriteView view, String key, long[] value) {
		view.putLongArray(key, value);
		return view;
	}

	public static <T> @Self WriteView with(@This WriteView view, String key, Codec<T> codec, T value) {
		view.put(key, codec, value);
		return view;
	}

	public static void putItemStack(@This WriteView view, String key, ItemStack stack) {
		if (!stack.isEmpty()) view.put(key, ItemStack.CODEC, stack);
	}

	public static @Self WriteView withItemStack(@This WriteView view, String key, ItemStack stack) {
		view.putItemStack(key, stack);
		return view;
	}

	public static void putUUID(@This WriteView view, String key, UUID uuid) {
		view.put(key, Uuids.CODEC, uuid);
	}

	public static @Self WriteView withUUID(@This WriteView view, String key, UUID uuid) {
		view.putUUID(key, uuid);
		return view;
	}

	public static void putFloatArray(@This WriteView view, String key, float... values) {
		view.putIntArray(key, NbtCompoundExtensions.toIntArray(values));
	}

	public static @Self WriteView withFloatArray(@This WriteView view, String key, float... values) {
		view.putFloatArray(key, values);
		return view;
	}

	public static void putDoubleArray(@This WriteView view, String key, double... values) {
		view.putLongArray(key, NbtCompoundExtensions.toLongArray(values));
	}

	public static @Self WriteView withDoubleArray(@This WriteView view, String key, double... values) {
		view.putDoubleArray(key, values);
		return view;
	}

	public static void putIdentifier(@This WriteView view, String key, Identifier value) {
		view.put(key, Identifier.CODEC, value);
	}

	public static @Self WriteView withIdentifier(@This WriteView view, String key, Identifier value) {
		view.putIdentifier(key, value);
		return view;
	}

	public static void putBlockPos(@This WriteView view, String key, BlockPos pos) {
		view.putIntArray(key, new int[] { pos.getX(), pos.getY(), pos.getZ() });
	}

	public static @Self WriteView withBlockPos(@This WriteView view, String key, BlockPos pos) {
		view.putBlockPos(key, pos);
		return view;
	}

	public static void putSubList(@This WriteView view, String key, Consumer<WriteView.ListView> populator) {
		populator.accept(view.getList(key));
	}

	public static @Self WriteView withSubList(@This WriteView view, String key, Consumer<WriteView.ListView> populator) {
		view.putSubList(key, populator);
		return view;
	}

	public static class ListView {

		public static void add(@This WriteView.ListView view, Consumer<WriteView> populator) {
			populator.accept(view.add());
		}

		public static @Self WriteView.ListView with(@This WriteView.ListView view, Consumer<WriteView> populator) {
			view.add(populator);
			return view;
		}
	}

	public static class ListAppender {

		public static <T> @Self WriteView.ListAppender<T> with(@This WriteView.ListAppender<T> list, T value) {
			list.add(value);
			return list;
		}

		public static <T> void addAll(@This WriteView.ListAppender<T> list, Iterable<T> values) {
			for (T value : values) {
				list.add(value);
			}
		}

		@SafeVarargs
		public static <T> void addAll(@This WriteView.ListAppender<T> list, T... values) {
			for (T value : values) {
				list.add(value);
			}
		}

		public static <T> @Self WriteView.ListAppender<T> withAll(@This WriteView.ListAppender<T> list, Iterable<T> values) {
			list.addAll(values);
			return list;
		}

		@SafeVarargs
		public static <T> @Self WriteView.ListAppender<T> withAll(@This WriteView.ListAppender<T> list, T... values) {
			list.addAll(values);
			return list;
		}
	}
}