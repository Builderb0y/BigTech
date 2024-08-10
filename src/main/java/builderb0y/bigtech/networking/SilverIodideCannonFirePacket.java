package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blockEntities.SilverIodideCannonBlockEntity;
import builderb0y.bigtech.screenHandlers.SilverIodideCannonScreenHandler;

public class SilverIodideCannonFirePacket implements C2SPlayPacket<SilverIodideCannonFirePacket.Payload> {

	public static final SilverIodideCannonFirePacket INSTANCE = new SilverIodideCannonFirePacket();

	public void send(boolean moreRainy) {
		BigTechNetwork.sendToServer(new Payload(moreRainy));
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
			if (player.currentScreenHandler instanceof SilverIodideCannonScreenHandler handler) {
				if (handler.inventory instanceof SilverIodideCannonBlockEntity blockEntity) {
					blockEntity.fire(player, this.moreRainy);
				}
				else {
					BigTechMod.LOGGER.warn("Player ${player} attempted to launch fire a silver iodide cannon from an invalid inventory: ${handler.inventory}");
				}
			}
			else {
				BigTechMod.LOGGER.warn("Player ${player} attempted to launch fire a silver iodide cannon from an invalid GUI: ${player.currentScreenHandler}");
			}
		}
	}
}