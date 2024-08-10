package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public interface C2SPlayPacket<T extends C2SPlayPacket.C2SPayload> extends PacketHandler<T> {

	public static interface C2SPayload extends BigTechPayload<ServerPlayNetworking.Context> {}
}