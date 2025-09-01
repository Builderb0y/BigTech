package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;

import builderb0y.bigtech.blockEntities.AssemblerBlockEntity;
import builderb0y.bigtech.gui.screenHandlers.AssemblerScreenHandler;

public class AssemblerSizePacket implements C2SPlayPacket<AssemblerSizePacket.Payload> {

	public static final AssemblerSizePacket INSTANCE = new AssemblerSizePacket();

	public void send(int width, int height) {
		BigTechNetwork.sendToServer(new Payload(width, height));
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return new Payload(buffer.readByte());
	}

	public static record Payload(byte data) implements C2SPayload {

		public Payload(int width, int height) {
			this((byte)(AssemblerBlockEntity.WIDTH.assemble(width) | AssemblerBlockEntity.HEIGHT.assemble(height)));
		}

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeByte(this.data);
		}

		@Override
		public void process(ServerPlayNetworking.Context context) {
			if (context.player().currentScreenHandler instanceof AssemblerScreenHandler handler && handler.assembler != null) {
				handler.assembler.setSize(AssemblerBlockEntity.WIDTH.get(this.data), AssemblerBlockEntity.HEIGHT.get(this.data));
			}
		}
	}
}