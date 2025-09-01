package builderb0y.bigtech.circuits;

import java.lang.reflect.Array;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public abstract class ByteBackedCircuitComponent implements CircuitComponent {

	public final byte data;

	public ByteBackedCircuitComponent(byte data) {
		this.data = data;
	}

	@Override
	public abstract CircuitComponent getDefaultState();

	@Override
	public abstract String toString();

	public static class Canonicalizer<C extends ByteBackedCircuitComponent> implements Byte2ObjectFunction<C> {

		public final C[] instances;
		public final PacketCodec<ByteBuf, C> packetCodec;

		public Canonicalizer(int length, Byte2ObjectFunction<C> constructor) {
			C first = constructor.get((byte)(0));
			@SuppressWarnings("unchecked")
			C[] array = (C[])(Array.newInstance(first.getClass(), length));
			array[0] = first;
			for (int index = 1; index < length; index++) {
				array[index] = constructor.get((byte)(index));
			}
			this.instances = array;
			this.packetCodec = PacketCodecs.BYTE.xmap(this, (C component) -> component.data);
		}

		@Override
		public C get(byte b) {
			return this.instances[b & 255];
		}

		@Override
		@SuppressWarnings("deprecation")
		public C apply(int operand) {
			return this.instances[operand & 255];
		}
	}
}