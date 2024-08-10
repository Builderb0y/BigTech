package builderb0y.bigtech.networking;

import io.netty.buffer.Unpooled;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.DynamicRegistryManager;

public interface PacketHandler<T extends PacketHandler.BigTechPayload<?>> {

	public default byte getId() {
		return BigTechNetwork.INSTANCE.getId(this);
	}

	public default RegistryByteBuf buffer(DynamicRegistryManager registries) {
		RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), registries);
		buffer.writeByte(this.getId());
		return buffer;
	}

	public abstract T decode(RegistryByteBuf buffer);

	public static interface BigTechPayload<T_Context> extends CustomPayload {

		@Override
		public default Id<? extends CustomPayload> getId() {
			return BigTechNetwork.ID;
		}

		public abstract PacketHandler<?> getAssociatedPacket();

		public abstract void encode(RegistryByteBuf buffer);

		public abstract void process(T_Context context);
	}
}