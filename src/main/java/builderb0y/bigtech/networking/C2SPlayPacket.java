package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.network.ServerPlayerEntity;

public interface C2SPlayPacket extends FabricPacket {

	public static <P extends C2SPlayPacket>ServerPlayNetworking.PlayPacketHandler<P> handler() {
		return P::handle;
	}

	public abstract void handle(ServerPlayerEntity player, PacketSender responseSender);
}