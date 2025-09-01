package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;

import builderb0y.bigtech.gui.screenHandlers.AssemblerScreenHandler;

public class CycleCircuitPacket implements C2SPlayPacket<CycleCircuitPacket.Payload> {

	public static final CycleCircuitPacket INSTANCE = new CycleCircuitPacket();

	public void send(int slot, boolean forward) {
		BigTechNetwork.sendToServer(new Payload(slot, forward));
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return new Payload(buffer.readByte());
	}

	public static record Payload(byte value) implements C2SPayload {

		public Payload(int slot, boolean forward) {
			this((byte)(forward ? slot : (slot | 128)));
		}

		public int slotIndex() {
			return this.value & 127;
		}

		public boolean forward() {
			return (this.value & 128) != 0;
		}

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeByte(this.value);
		}

		@Override
		public void process(ServerPlayNetworking.Context context) {
			if (context.player().currentScreenHandler instanceof AssemblerScreenHandler handler) {
				handler.cycle(this.slotIndex(), this.forward());
			}
		}
	}
}