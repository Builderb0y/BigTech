package builderb0y.bigtech.networking;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.CommonChunkBeamStorage;

public class LoadBeamPacket implements S2CPlayPacket<LoadBeamPacket.Payload> {

	public static final LoadBeamPacket INSTANCE = new LoadBeamPacket();

	public void send(int chunkX, int chunkZ, CommonChunkBeamStorage storage, ServerPlayerEntity player) {
		RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), player.getWorld().getRegistryManager());
		storage.writeSyncPacket(buffer, player);
		BigTechNetwork.sendToClient(player, new Payload(chunkX, chunkZ, buffer));
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		int chunkX = buffer.readMedium();
		int chunkZ = buffer.readMedium();
		RegistryByteBuf copy = new RegistryByteBuf(Unpooled.buffer(buffer.readableBytes()), buffer.getRegistryManager());
		buffer.readBytes(copy);
		return new Payload(chunkX, chunkZ, copy);
	}

	public static record Payload(int chunkX, int chunkZ, PacketByteBuf buffer) implements S2CPayload {

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeMedium(this.chunkX).writeMedium(this.chunkZ);
			buffer.writeBytes(this.buffer);
		}

		@Override
		@Environment(EnvType.CLIENT)
		public void process(ClientPlayNetworking.Context context) {
			ClientWorld world = context.player().clientWorld;
			if (world != null) {
				Chunk chunk = world.getChunk(this.chunkX, this.chunkZ, ChunkStatus.FULL, false);
				if (chunk == null) {
					BigTechMod.LOGGER.warn("Received load beam packet for unloaded chunk at ${this.chunkX}, ${this.chunkZ}");
					return;
				}
				ChunkBeamStorageHolder.KEY.get(chunk).require().applySyncPacket(this.buffer);
			}
		}
	}
}