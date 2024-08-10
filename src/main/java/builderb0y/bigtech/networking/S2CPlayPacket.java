package builderb0y.bigtech.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.network.RegistryByteBuf;

public interface S2CPlayPacket<T extends S2CPlayPacket.S2CPayload> extends PacketHandler<T> {

	@Override
	@Environment(EnvType.CLIENT)
	public abstract T decode(RegistryByteBuf buffer);

	public static interface S2CPayload extends BigTechPayload<ClientPlayNetworking.Context> {

		@Override
		@Environment(EnvType.CLIENT)
		public abstract void process(ClientPlayNetworking.Context context);
	}
}