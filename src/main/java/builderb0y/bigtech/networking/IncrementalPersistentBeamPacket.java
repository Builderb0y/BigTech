package builderb0y.bigtech.networking;

import java.util.*;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import org.joml.Vector3f;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.ClientChunkBeamStorage;
import builderb0y.bigtech.beams.storage.section.ClientSectionBeamStorage;
import builderb0y.bigtech.beams.storage.world.ClientWorldBeamStorage;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public record IncrementalPersistentBeamPacket(int sectionX, int sectionY, int sectionZ, List<Change> changes) implements S2CPlayPacket {

	public static final PacketType<IncrementalPersistentBeamPacket> TYPE = PacketType.create(BigTechMod.modID("incremental_persistent_beam_sync"), IncrementalPersistentBeamPacket::parse);
	public static final int
		ACTION_ADD = 0,
		ACTION_REMOVE = 1;

	public static IncrementalPersistentBeamPacket parse(PacketByteBuf buffer) {
		int sectionX = buffer.readMedium();
		int sectionY = buffer.readMedium();
		int sectionZ = buffer.readMedium();
		int changeCount = buffer.readVarInt();
		List<Change> changes = new ArrayList<>(changeCount);
		for (int index = 0; index < changeCount; index++) {
			changes.add(switch (buffer.readByte()) {
				case ACTION_ADD -> new AddChange(
					buffer.readShort(),
					buffer.readUuid(),
					BeamDirection.VALUES[buffer.readByte()],
					BeamSegment.unpackRgb(buffer.readUnsignedMedium())
				);
				case ACTION_REMOVE -> new RemoveChange(
					buffer.readShort(),
					buffer.readUuid(),
					BeamDirection.VALUES[buffer.readByte()]
				);
				default -> {
					throw new IllegalArgumentException("Invalid change");
				}
			});
		}
		return new IncrementalPersistentBeamPacket(sectionX, sectionY, sectionZ, changes);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		WorldChunk chunk = player.world.getChunk(this.sectionX, this.sectionZ);
		if (chunk == null) {
			BigTechMod.LOGGER.warn("Received incremental beam update for chunk outside loaded area: ${this.sectionX}, ${this.sectionY}, ${this.sectionZ}");
			return;
		}
		ClientWorldBeamStorage worldStorage = CommonWorldBeamStorage.KEY.get(player.world).as();
		ClientChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).require().as();
		ClientSectionBeamStorage section = chunkStorage.getSection(this.sectionY).as();
		for (Change change : this.changes) {
			change.apply(worldStorage, section);
		}
	}

	@Override
	public void write(PacketByteBuf buffer) {
		buffer.writeMedium(this.sectionX).writeMedium(this.sectionY).writeMedium(this.sectionZ);
		buffer.writeVarInt(this.changes.size());
		for (Change change : this.changes) {
			change.writeTo(buffer);
		}
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public static abstract class Change {

		public final short index;
		public final UUID uuid;
		public final BeamDirection direction;

		public Change(short index, UUID uuid, BeamDirection direction) {
			this.index = index;
			this.uuid = uuid;
			this.direction = direction;
		}

		public abstract void apply(ClientWorldBeamStorage world, ClientSectionBeamStorage section);

		public void writeTo(PacketByteBuf buffer) {
			buffer
			.writeShort(this.index)
			.writeUuid(this.uuid)
			.writeByte(this.direction.ordinal());
		}
	}

	public static class AddChange extends Change {

		public final Vector3f color;

		public AddChange(short index, UUID uuid, BeamDirection direction, Vector3f color) {
			super(index, uuid, direction);
			this.color = color;
		}

		@Override
		public void apply(ClientWorldBeamStorage world, ClientSectionBeamStorage section) {
			Beam beam = world.getBeam(this.uuid);
			if (beam == null) {
				BigTechMod.LOGGER.warn("Received segment for unknown beam: ${this.uuid}");
				return;
			}
			section.addSegment(this.index, new BeamSegment(beam, this.direction, 0.0D, true, this.color), false);
		}

		@Override
		public void writeTo(PacketByteBuf buffer) {
			buffer.writeByte(ACTION_ADD);
			super.writeTo(buffer);
			buffer.writeMedium(BeamSegment.packRgb(this.color));
		}
	}

	public static class RemoveChange extends Change {

		public RemoveChange(short index, UUID uuid, BeamDirection direction) {
			super(index, uuid, direction);
		}

		@Override
		public void apply(ClientWorldBeamStorage world, ClientSectionBeamStorage section) {
			LinkedList<BeamSegment> segments = section.getSegments(this.index);
			segments.removeIf(segment -> segment.beam.uuid.equals(this.uuid));
			if (segments.isEmpty()) {
				section.remove(this.index);
			}
		}

		@Override
		public void writeTo(PacketByteBuf buffer) {
			buffer.writeByte(ACTION_REMOVE);
			super.writeTo(buffer);
		}
	}
}