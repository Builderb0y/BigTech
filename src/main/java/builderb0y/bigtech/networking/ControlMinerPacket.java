package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigtech.entities.MinerEntity;

public class ControlMinerPacket implements C2SPlayPacket<ControlMinerPacket.Payload> {

	public static final ControlMinerPacket INSTANCE = new ControlMinerPacket();

	public void send(byte input) {
		BigTechNetwork.sendToServer(new Payload(input));
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return new Payload(buffer.readByte());
	}

	public static record Payload(byte input) implements C2SPayload {

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeByte(this.input);
		}

		@Override
		public void process(ServerPlayNetworking.Context context) {
			ServerPlayerEntity player = context.player();
			if (player.getVehicle() instanceof MinerEntity miner && miner.getControllingPassenger() == player) {
				miner.applyInputFromPlayer(this.input);
			}
		}
	}
}