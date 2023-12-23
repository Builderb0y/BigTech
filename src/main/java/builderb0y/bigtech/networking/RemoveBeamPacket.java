package builderb0y.bigtech.networking;

import java.util.LinkedList;
import java.util.UUID;

import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import org.apache.commons.lang3.mutable.MutableBoolean;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.light.LightingProvider;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.CommonChunkBeamStorage;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;

public record RemoveBeamPacket(int sectionX, int sectionY, int sectionZ, UUID uuid, ShortCollection segmentPositions) implements S2CPlayPacket {

	public static RemoveBeamPacket parse(PacketByteBuf buffer) {
		int sectionX = buffer.readMedium();
		int sectionY = buffer.readMedium();
		int sectionZ = buffer.readMedium();
		UUID uuid = buffer.readUuid();
		int positionCount = buffer.readVarInt();
		ShortSet positions = new ShortOpenHashSet(positionCount);
		for (int index = 0; index < positionCount; index++) {
			positions.add(buffer.readShort());
		}
		return new RemoveBeamPacket(sectionX, sectionY, sectionZ, uuid, positions);
	}

	@Override
	public void write(PacketByteBuf buffer) {
		buffer
		.writeMedium(this.sectionX)
		.writeMedium(this.sectionY)
		.writeMedium(this.sectionZ)
		.writeUuid(this.uuid)
		.writeVarInt(this.segmentPositions.size());
		ShortIterator iterator = this.segmentPositions.iterator();
		while (iterator.hasNext()) {
			buffer.writeShort(iterator.nextShort());
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		Chunk chunk = player.world.getChunk(this.sectionX, this.sectionZ, ChunkStatus.FULL, false);
		if (chunk == null) {
			BigTechMod.LOGGER.warn("Received beam removal packet for unloaded section: ${this.sectionX}, ${this.sectionY}, ${this.sectionZ}");
			return;
		}
		CommonChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).require();
		CommonSectionBeamStorage sectionStorage = chunkStorage.get(this.sectionY);
		if (sectionStorage == null) {
			BigTechMod.LOGGER.warn("Received beam removal packet for empty section: ${this.sectionX}, ${this.sectionY}, ${this.sectionZ}");
			return;
		}
		LightingProvider lightingProvider = player.world.chunkManager.lightingProvider;
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		ShortIterator blockIterator = this.segmentPositions.iterator();
		MutableBoolean needLightUpdate = new MutableBoolean();
		while (blockIterator.hasNext()) {
			short position = blockIterator.nextShort();
			LinkedList<BeamSegment> segments = sectionStorage.getSegments(position);
			needLightUpdate.setFalse();
			if (!segments.removeIf((BeamSegment segment) -> {
				if (segment.beam.uuid.equals(this.uuid)) {
					if (((PersistentBeam)(segment.beam)).getLightLevel(segment) > 0) {
						needLightUpdate.setTrue();
					}
					return true;
				}
				return false;
			})) {
				BigTechMod.LOGGER.warn("No segments removed at ${this.sectionX}, ${this.sectionY}, ${this.sectionZ} position index ${position}");
			}
			else if (needLightUpdate.booleanValue()) {
				int x = (this.sectionX << 4) | BasicSectionBeamStorage.unpackX(position);
				int y = (this.sectionY << 4) | BasicSectionBeamStorage.unpackY(position);
				int z = (this.sectionZ << 4) | BasicSectionBeamStorage.unpackZ(position);
				lightingProvider.checkBlock(mutablePos.set(x, y, z));
			}
			if (segments.isEmpty()) {
				sectionStorage.remove(position);
			}
		}
	}

	@Override
	public PacketType<?> getType() {
		return BigTechClientNetwork.REMOVE_BEAM;
	}
}