package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;

import builderb0y.bigtech.blockEntities.LongRangeDeployerBlockEntity;
import builderb0y.bigtech.gui.screenHandlers.LongRangeDeployerScreenHandler;

public class LongRangeDeployerEverywherePacket implements C2SPlayPacket<LongRangeDeployerEverywherePacket.Payload> {

	public static final LongRangeDeployerEverywherePacket INSTANCE = new LongRangeDeployerEverywherePacket();

	public void send(boolean everywhere) {
		BigTechNetwork.sendToServer(everywhere ? Payload.EVERYWHERE : Payload.SOMEWHERE);
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return buffer.readBoolean() ? Payload.EVERYWHERE : Payload.SOMEWHERE;
	}

	public static record Payload(boolean everywhere) implements C2SPayload {

		public static final Payload
			EVERYWHERE = new Payload(true),
			SOMEWHERE  = new Payload(false);

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeBoolean(this.everywhere);
		}

		@Override
		public void process(ServerPlayNetworking.Context context) {
			if (context.player().currentScreenHandler instanceof LongRangeDeployerScreenHandler handler && handler.inventory instanceof LongRangeDeployerBlockEntity blockEntity) {
				blockEntity.everywhere.set(this.everywhere ? 1 : 0);
				blockEntity.markDirty();
			}
		}
	}
}