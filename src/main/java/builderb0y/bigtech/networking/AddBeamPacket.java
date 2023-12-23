package builderb0y.bigtech.networking;

import java.util.*;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.light.LightingProvider;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.CommonChunkBeamStorage;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public record AddBeamPacket(int sectionX, int sectionY, int sectionZ, UUID uuid, List<MinimalBeamSegment> segments) implements S2CPlayPacket {

	public static AddBeamPacket create(int sectionX, int sectionY, int sectionZ, UUID uuid, BasicSectionBeamStorage storage) {
		return new AddBeamPacket(
			sectionX,
			sectionY,
			sectionZ,
			uuid,
			storage
			.short2ObjectEntrySet()
			.stream()
			.flatMap((Short2ObjectMap.Entry<LinkedList<BeamSegment>> entry) ->
				entry
				.value
				.stream()
				.filter((BeamSegment segment) -> segment.visible)
				.map((BeamSegment segment) -> MinimalBeamSegment.from(entry.shortKey, segment))
			)
			.toList()
		);
	}

	public static AddBeamPacket parse(PacketByteBuf buffer) {
		int sectionX = buffer.readMedium();
		int sectionY = buffer.readMedium();
		int sectionZ = buffer.readMedium();
		UUID uuid = buffer.readUuid();
		int segmentCount = buffer.readInt();
		List<MinimalBeamSegment> segments = new ArrayList<>(segmentCount);
		for (int segmentIndex = 0; segmentIndex < segmentCount; segmentIndex++) {
			short position = buffer.readShort();
			BeamDirection direction = BeamDirection.VALUES[buffer.readByte()];
			Vector3f color = BeamSegment.unpackRgb(buffer.readUnsignedMedium());
			segments.add(new MinimalBeamSegment(position, direction, color));
		}
		return new AddBeamPacket(sectionX, sectionY, sectionZ, uuid, segments);
	}

	@Override
	public void write(PacketByteBuf buffer) {
		buffer
		.writeMedium(this.sectionX)
		.writeMedium(this.sectionY)
		.writeMedium(this.sectionZ)
		.writeUuid(this.uuid);
		buffer.writeInt(this.segments.size());
		for (MinimalBeamSegment segment : this.segments) {
			buffer
			.writeShort(segment.position)
			.writeByte(segment.direction.ordinal())
			.writeMedium(BeamSegment.packRgb(segment.color));
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		CommonWorldBeamStorage world = CommonWorldBeamStorage.KEY.get(player.world);
		PersistentBeam beam = world.getBeam(this.uuid);
		if (beam == null) {
			BigTechMod.LOGGER.warn("Received beam add packet for unknown beam: ${this.uuid}");
			return;
		}
		Chunk chunk = player.world.getChunk(this.sectionX, this.sectionZ, ChunkStatus.FULL, false);
		if (chunk == null) {
			BigTechMod.LOGGER.warn("Received beam add packet for unloaded section: ${this.sectionX}, ${this.sectionY}, ${this.sectionZ}");
			return;
		}
		CommonChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).require();
		CommonSectionBeamStorage sectionStorage = chunkStorage.getSection(this.sectionY);
		LightingProvider lightingProvider = player.world.chunkManager.lightingProvider;
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		for (MinimalBeamSegment minimalSegment : this.segments) {
			BeamSegment segment = minimalSegment.toBeamSegment(beam);
			sectionStorage.addSegment(minimalSegment.position, segment, false);
			if (beam.getLightLevel(segment) > 0) {
				int x = (this.sectionX << 4) | BasicSectionBeamStorage.unpackX(minimalSegment.position);
				int y = (this.sectionY << 4) | BasicSectionBeamStorage.unpackY(minimalSegment.position);
				int z = (this.sectionZ << 4) | BasicSectionBeamStorage.unpackZ(minimalSegment.position);
				lightingProvider.checkBlock(mutablePos.set(x, y, z));
			}
		}
	}

	@Override
	public PacketType<?> getType() {
		return BigTechClientNetwork.ADD_BEAM;
	}

	public static record MinimalBeamSegment(short position, BeamDirection direction, Vector3fc color) {

		public static MinimalBeamSegment from(short position, BeamSegment segment) {
			return new MinimalBeamSegment(position, segment.direction, segment.effectiveColor);
		}

		public BeamSegment toBeamSegment(PersistentBeam beam) {
			return new BeamSegment(beam, this.direction, true, this.color);
		}
	}
}