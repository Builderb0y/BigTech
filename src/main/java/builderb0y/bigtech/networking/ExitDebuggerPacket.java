package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;

import builderb0y.bigtech.gui.screenHandlers.CircuitDebuggerScreenHandler;

public class ExitDebuggerPacket implements C2SPlayPacket<ExitDebuggerPacket.Payload> {

	public static final ExitDebuggerPacket INSTANCE = new ExitDebuggerPacket();

	public void send() {
		BigTechNetwork.sendToServer(Payload.INSTANCE);
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return Payload.INSTANCE;
	}

	public static enum Payload implements C2SPayload {
		INSTANCE;

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return ExitDebuggerPacket.INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {

		}

		@Override
		public void process(ServerPlayNetworking.Context context) {
			if (context.player().currentScreenHandler instanceof CircuitDebuggerScreenHandler handler) {
				handler.exit();
			}
			else {
				context.player().closeHandledScreen();
			}
		}
	}
}