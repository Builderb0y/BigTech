package builderb0y.bigtech.networking;

import java.util.*;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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

public class AddBeamPacket implements S2CPlayPacket<AddBeamPacket.Payload> {

	public static final AddBeamPacket INSTANCE = new AddBeamPacket();

	public void send(ServerWorld world, int sectionX, int sectionY, int sectionZ, UUID uuid, BasicSectionBeamStorage storage) {
		Collection<ServerPlayerEntity> tracking = PlayerLookup.tracking(world, new ChunkPos(sectionX, sectionZ));
		if (!tracking.isEmpty()) {
			Payload payload = Payload.create(
				sectionX,
				sectionY,
				sectionZ,
				uuid,
				storage
			);
			for (ServerPlayerEntity player : tracking) {
				BigTechNetwork.sendToClient(player, payload);
			}
		}
	}

	public static record MinimalBeamSegment(short position, BeamDirection direction, Vector3fc color) {

		public static MinimalBeamSegment from(short position, BeamSegment segment) {
			return new MinimalBeamSegment(position, segment.direction(), segment.getEffectiveColor());
		}

		public BeamSegment toBeamSegment(PersistentBeam beam) {
			return new BeamSegment(beam, this.direction, true, this.color);
		}
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
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
		return new Payload(sectionX, sectionY, sectionZ, uuid, segments);
	}

	public static record Payload(int sectionX, int sectionY, int sectionZ, UUID uuid, List<MinimalBeamSegment> segments) implements S2CPayload {

		public static Payload create(int sectionX, int sectionY, int sectionZ, UUID uuid, BasicSectionBeamStorage storage) {
			return new Payload(
				sectionX,
				sectionY,
				sectionZ,
				uuid,
				storage
				.short2ObjectEntrySet()
				.stream()
				.flatMap((Short2ObjectMap.Entry<LinkedList<BeamSegment>> entry) ->
					entry
					.getValue()
					.stream()
					.filter(BeamSegment::visible)
					.map((BeamSegment segment) -> MinimalBeamSegment.from(entry.getShortKey(), segment))
				)
				.toList()
			);
		}

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
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
		public void process(ClientPlayNetworking.Context context) {
			ClientPlayerEntity player = context.player();
			CommonWorldBeamStorage world = CommonWorldBeamStorage.KEY.get(player.getWorld());
			PersistentBeam beam = world.getBeam(this.uuid);
			if (beam == null) {
				BigTechMod.LOGGER.warn("Received beam add packet for unknown beam: ${this.uuid}");
				return;
			}
			Chunk chunk = player.getWorld().getChunk(this.sectionX, this.sectionZ, ChunkStatus.FULL, false);
			if (chunk == null) {
				BigTechMod.LOGGER.warn("Received beam add packet for unloaded section: ${this.sectionX}, ${this.sectionY}, ${this.sectionZ}");
				return;
			}
			CommonChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).require();
			CommonSectionBeamStorage sectionStorage = chunkStorage.getSection(this.sectionY);
			LightingProvider lightingProvider = player.getWorld().getChunkManager().getLightingProvider();
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
	}
}