package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigtech.blockEntities.SilverIodideCannonBlockEntity;
import builderb0y.bigtech.gui.screenHandlers.SilverIodideCannonScreenHandler;

public class SilverIodideCannonSetSelectedButtonPacket implements C2SPlayPacket<SilverIodideCannonSetSelectedButtonPacket.Payload> {

	public static final SilverIodideCannonSetSelectedButtonPacket INSTANCE = new SilverIodideCannonSetSelectedButtonPacket();

	public void send(boolean moreRainy) {
		BigTechNetwork.sendToServer(moreRainy ? Payload.MORE_RAINY : Payload.LESS_RAINY);
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		return buffer.readBoolean() ? Payload.MORE_RAINY : Payload.LESS_RAINY;
	}

	public static record Payload(boolean moreRainy) implements C2SPayload {

		public static final Payload
			MORE_RAINY = new Payload(true),
			LESS_RAINY = new Payload(false);

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeBoolean(this.moreRainy);
		}

		@Override
		public void process(ServerPlayNetworking.Context context) {
			ServerPlayerEntity player = context.player();
			if (player.currentScreenHandler instanceof SilverIodideCannonScreenHandler handler && handler.inventory instanceof SilverIodideCannonBlockEntity blockEntity) {
				blockEntity.setSelectedButton(this.moreRainy);
			}
		}
	}
}