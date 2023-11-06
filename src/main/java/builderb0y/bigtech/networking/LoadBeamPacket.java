package builderb0y.bigtech.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;

public record LoadBeamPacket(int chunkX, int chunkZ, PacketByteBuf buffer) implements S2CPlayPacket {

	public static LoadBeamPacket parse(PacketByteBuf buffer) {
		return new LoadBeamPacket(buffer.readMedium(), buffer.readMedium(), buffer);
	}

	@Override
	public void write(PacketByteBuf buffer) {
		buffer.writeMedium(this.chunkX).writeMedium(this.chunkZ);
		buffer.writeBytes(this.buffer);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		Chunk chunk = player.world.getChunk(this.chunkX, this.chunkZ, ChunkStatus.FULL, false);
		if (chunk == null) {
			BigTechMod.LOGGER.warn("Received load beam packet for unloaded chunk at ${this.chunkX}, ${this.chunkZ}");
			return;
		}
		ChunkBeamStorageHolder.KEY.get(chunk).require().applySyncPacket(this.buffer);
	}

	@Override
	public PacketType<?> getType() {
		return BigTechClientNetwork.LOAD_BEAM;
	}
}