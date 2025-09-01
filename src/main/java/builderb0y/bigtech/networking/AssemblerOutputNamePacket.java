package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import builderb0y.bigtech.gui.screenHandlers.AssemblerScreenHandler;

public class AssemblerOutputNamePacket implements C2SPlayPacket<AssemblerOutputNamePacket.Payload> {

	public static final AssemblerOutputNamePacket INSTANCE = new AssemblerOutputNamePacket();

	public void send(Text text) {
		BigTechNetwork.sendToServer(new Payload(text));
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return new Payload(TextCodecs.PACKET_CODEC.decode(buffer));
	}

	public static record Payload(Text text) implements C2SPayload {

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			TextCodecs.PACKET_CODEC.encode(buffer, this.text);
		}

		@Override
		public void process(ServerPlayNetworking.Context context) {
			if (context.player().currentScreenHandler instanceof AssemblerScreenHandler handler && handler.assembler != null) {
				handler.assembler.setOutputName(this.text);
			}
		}
	}
}