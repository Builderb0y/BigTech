package builderb0y.bigtech.networking;

import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Predicate;

import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.apache.commons.lang3.mutable.MutableBoolean;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
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
import builderb0y.bigtech.util.Lockable;
import builderb0y.bigtech.util.Locked;

public class RemoveBeamPacket implements S2CPlayPacket<RemoveBeamPacket.Payload> {

	public static final RemoveBeamPacket INSTANCE = new RemoveBeamPacket();

	public static void send(
		Collection<ServerPlayerEntity> players,
		int sectionX,
		int sectionY,
		int sectionZ,
		UUID uuid,
		ShortCollection segmentPositions
	) {
		if (!players.isEmpty()) {
			Payload payload = new Payload(sectionX, sectionY, sectionZ, uuid, segmentPositions);
			for (ServerPlayerEntity player : players) {
				BigTechNetwork.sendToClient(player, payload);
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Payload decode(RegistryByteBuf buffer) {
		int sectionX = buffer.readMedium();
		int sectionY = buffer.readMedium();
		int sectionZ = buffer.readMedium();
		UUID uuid = buffer.readUuid();
		int positionCount = buffer.readVarInt();
		ShortSet positions = new ShortOpenHashSet(positionCount);
		for (int index = 0; index < positionCount; index++) {
			positions.add(buffer.readShort());
		}
		return new Payload(sectionX, sectionY, sectionZ, uuid, positions);
	}

	public static record Payload(int sectionX, int sectionY, int sectionZ, UUID uuid, ShortCollection segmentPositions) implements S2CPayload {

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
			.writeUuid(this.uuid)
			.writeVarInt(this.segmentPositions.size());
			for (ShortIterator iterator = this.segmentPositions.iterator(); iterator.hasNext(); ) {
				buffer.writeShort(iterator.nextShort());
			}
		}

		@Override
		@Environment(EnvType.CLIENT)
		public void process(ClientPlayNetworking.Context context) {
			ClientPlayerEntity player = context.player();
			Chunk chunk = player.getWorld().getChunk(this.sectionX, this.sectionZ, ChunkStatus.FULL, false);
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
			LightingProvider lightingProvider = player.getWorld().getChunkManager().getLightingProvider();
			BlockPos.Mutable mutablePos = new BlockPos.Mutable();
			ShortIterator blockIterator = this.segmentPositions.iterator();
			MutableBoolean needLightUpdate = new MutableBoolean();
			Predicate<BeamSegment> lambda = (BeamSegment segment) -> {
				if (segment.beam().uuid.equals(this.uuid)) {
					if (segment.beam().<PersistentBeam>as().getLightLevel(segment) > 0) {
						needLightUpdate.setTrue();
					}
					return true;
				}
				return false;
			};
			while (blockIterator.hasNext()) {
				short position = blockIterator.nextShort();
				Lockable<TreeSet<BeamSegment>> segments = sectionStorage.getSegments(position);
				needLightUpdate.setFalse();
				boolean removed, empty;
				try (Locked<TreeSet<BeamSegment>> locked = segments.write()) {
					removed = locked.value.removeIf(lambda);
					empty = locked.value.isEmpty();
				}
				if (!removed) {
					BigTechMod.LOGGER.warn("No segments removed at ${this.sectionX}, ${this.sectionY}, ${this.sectionZ} position index ${position}");
				}
				else if (needLightUpdate.booleanValue()) {
					int x = (this.sectionX << 4) | BasicSectionBeamStorage.unpackX(position);
					int y = (this.sectionY << 4) | BasicSectionBeamStorage.unpackY(position);
					int z = (this.sectionZ << 4) | BasicSectionBeamStorage.unpackZ(position);
					lightingProvider.checkBlock(mutablePos.set(x, y, z));
				}
				if (empty) {
					sectionStorage.remove(position);
				}
			}
		}
	}
}