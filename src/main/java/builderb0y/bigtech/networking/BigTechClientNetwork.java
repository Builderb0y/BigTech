package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class BigTechClientNetwork {

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(                PulseBeamPacket.TYPE, S2CPlayPacket.handler());
		ClientPlayNetworking.registerGlobalReceiver(          EntityRouteSyncPacket.TYPE, S2CPlayPacket.handler());
		ClientPlayNetworking.registerGlobalReceiver(     TogglePersistentBeamPacket.TYPE, S2CPlayPacket.handler());
		ClientPlayNetworking.registerGlobalReceiver(IncrementalPersistentBeamPacket.TYPE, S2CPlayPacket.handler());
	}
}