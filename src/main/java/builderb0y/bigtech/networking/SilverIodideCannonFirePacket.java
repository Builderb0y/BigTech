package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blockEntities.SilverIodideCannonBlockEntity;
import builderb0y.bigtech.screenHandlers.SilverIodideCannonScreenHandler;

public record SilverIodideCannonFirePacket(boolean moreRainy) implements C2SPlayPacket {

	public static SilverIodideCannonFirePacket parse(PacketByteBuf buffer) {
		return new SilverIodideCannonFirePacket(buffer.readBoolean());
	}

	@Override
	public void handle(ServerPlayerEntity player, PacketSender responseSender) {
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

	@Override
	public void write(PacketByteBuf buffer) {
		buffer.writeBoolean(this.moreRainy);
	}

	@Override
	public PacketType<?> getType() {
		return BigTechServerNetwork.FIRE_SILVER_IODIDE_CANNON;
	}
}