package builderb0y.bigtech.networking;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public class PacketCodecs2 {

	public static final PacketCodec<PacketByteBuf, BlockPos> BLOCK_POS = PacketCodec.ofStatic(
		//can't use method references for these because of ambiguity.
		(PacketByteBuf buffer, BlockPos value) -> PacketByteBuf.writeBlockPos(buffer, value),
		(PacketByteBuf buffer) -> PacketByteBuf.readBlockPos(buffer)
	);

	public static <B extends PacketByteBuf, V> PacketCodec<B, List<V>> list(PacketCodec<? super B, V> elementCodec) {
		return new PacketCodec<>() {

			@Override
			public List<V> decode(B buffer) {
				int count = buffer.readVarInt();
				List<V> list = new ArrayList<>(count);
				for (int index = 0; index < count; index++) {
					list.add(elementCodec.decode(buffer));
				}
				return list;
			}

			@Override
			public void encode(B buffer, List<V> list) {
				int size = list.size();
				buffer.writeVarInt(size);
				for (int index = 0; index < size; index++) {
					elementCodec.encode(buffer, list.get(index));
				}
			}

			@Override
			public String toString() {
				return "PacketCodecs2.list(${elementCodec})";
			}
		};
	}

	public static <B extends PacketByteBuf, V> PacketCodec<B, @Nullable V> nullable(PacketCodec<? super B, @NotNull V> elementCodec) {
		return new PacketCodec<>() {

			@Override
			public @Nullable V decode(B buffer) {
				return buffer.readBoolean() ? elementCodec.decode(buffer) : null;
			}

			@Override
			public void encode(B buffer, @Nullable V value) {
				buffer.writeBoolean(value != null);
				if (value != null) elementCodec.encode(buffer, value);
			}

			@Override
			public String toString() {
				return "PacketCodecs2.nullable(${elementCodec})";
			}
		};
	}

	public static <B extends PacketByteBuf, V> PacketCodec<B, @Nullable V> nullableDefault(PacketCodec<? super B, @NotNull V> elementCodec, @Nullable V defaultValue) {
		return new PacketCodec<>() {

			@Override
			public @Nullable V decode(B buffer) {
				return buffer.readBoolean() ? elementCodec.decode(buffer) : defaultValue;
			}

			@Override
			public void encode(B buffer, @Nullable V value) {
				buffer.writeBoolean(value != null);
				if (value != null) elementCodec.encode(buffer, value);
			}

			@Override
			public String toString() {
				return "PacketCodecs2.nullableDefault(${elementCodec}, ${defaultValue})";
			}
		};
	}
}