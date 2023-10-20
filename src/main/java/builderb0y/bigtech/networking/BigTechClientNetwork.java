package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class BigTechClientNetwork {

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(EntityRouteSyncPacket.TYPE, S2CPlayPacket.handler());
	}
}