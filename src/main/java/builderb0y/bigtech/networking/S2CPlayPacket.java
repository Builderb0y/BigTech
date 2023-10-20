package builderb0y.bigtech.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

import net.minecraft.client.network.ClientPlayerEntity;

public interface S2CPlayPacket extends FabricPacket {

	@Environment(EnvType.CLIENT)
	public static <P extends S2CPlayPacket> ClientPlayNetworking.PlayPacketHandler<P> handler() {
		return P::handle;
	}

	@Environment(EnvType.CLIENT)
	public abstract void handle(ClientPlayerEntity player, PacketSender responseSender);
}