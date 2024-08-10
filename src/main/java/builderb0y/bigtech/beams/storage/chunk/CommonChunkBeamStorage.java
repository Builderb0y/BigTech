package builderb0y.bigtech.beams.storage.chunk;

import java.util.LinkedList;
import java.util.UUID;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import org.joml.Vector3f;

import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.util.AsyncRunner;

public abstract class CommonChunkBeamStorage extends Int2ObjectOpenHashMap<CommonSectionBeamStorage> {

	public final WorldChunk chunk;

	public CommonChunkBeamStorage(WorldChunk chunk) {
		super(4);
		this.chunk = chunk;
	}

	public abstract CommonSectionBeamStorage newSection(int sectionCoordY);

	public CommonSectionBeamStorage getSection(int sectionCoordY) {
		return this.computeIfAbsent(sectionCoordY, this::newSection);
	}

	public void writeToNbt(NbtCompound tag) {
		if (!this.isEmpty()) {
			NbtList sectionsNbt = tag.createSubList("sections");
			try (AsyncRunner async = new AsyncRunner()) {
				ObjectIterator<Int2ObjectMap.Entry<CommonSectionBeamStorage>> iterator = this.int2ObjectEntrySet().fastIterator();
				while (iterator.hasNext()) {
					Int2ObjectMap.Entry<CommonSectionBeamStorage> entry = iterator.next();
					CommonSectionBeamStorage sectionStorage = entry.getValue();
					if (!sectionStorage.isEmpty()) {
						NbtCompound compound = sectionsNbt.createCompound().withInt("coord", entry.getIntKey());
						async.submit(() -> sectionStorage.writeToNbt(compound, true));
					}
				}
			}
		}
	}

	public void readFromNbt(NbtCompound tag) {
		this.clear();
		NbtList sectionsNbt = tag.getList("sections", NbtElement.COMPOUND_TYPE);
		if (!sectionsNbt.isEmpty()) try (AsyncRunner async = new AsyncRunner()) {
			for (NbtCompound sectionNbt : sectionsNbt.<Iterable<NbtCompound>>as()) {
				NbtElement coord = sectionNbt.get("coord");
				if (!(coord instanceof AbstractNbtNumber)) {
					BigTechMod.LOGGER.warn("Skipping beam section storage with unknown coord");
					continue;
				}
				int actualCoord = coord.<AbstractNbtNumber>as().intValue();
				CommonSectionBeamStorage section = this.newSection(actualCoord);
				CommonSectionBeamStorage old = this.putIfAbsent(actualCoord, section);
				if (old == null) {
					async.submit(() -> section.readFromNbt(sectionNbt));
				}
				else {
					BigTechMod.LOGGER.warn("Skipping beam section storage with duplicate coord: ${actualCoord}");
				}
			}
		}
	}

	/**
	will be called automatically for ProtoChunk -> Chunk conversion.
	while ProtoChunk's will never have beams in them,
	it is possible that other places could call this too,
	and I want this to work faster than the default implementation,
	which simply serializes and deserializes from NBT.
	*/
	public void copyFrom(CommonChunkBeamStorage other) {
		this.clear();
		if (!other.isEmpty()) {
			try (AsyncRunner async = new AsyncRunner()) {
				ObjectIterator<Int2ObjectMap.Entry<CommonSectionBeamStorage>> iterator = other.int2ObjectEntrySet().fastIterator();
				while (iterator.hasNext()) {
					Int2ObjectMap.Entry<CommonSectionBeamStorage> entry = iterator.next();
					CommonSectionBeamStorage thisSection = this.getSection(entry.getIntKey());
					CommonSectionBeamStorage thatSection = entry.getValue();
					async.submit(() -> thisSection.addAll(thatSection, false));
				}
			}
		}
	}

	public void writeSyncPacket(PacketByteBuf buffer, ServerPlayerEntity recipient) {
		buffer.writeVarInt(this.size());
		ObjectIterator<Int2ObjectMap.Entry<CommonSectionBeamStorage>> sectionIterator = this.int2ObjectEntrySet().fastIterator();
		while (sectionIterator.hasNext()) {
			Int2ObjectMap.Entry<CommonSectionBeamStorage> sectionEntry = sectionIterator.next();
			buffer.writeInt(sectionEntry.getIntKey());
			int countPosition = buffer.writerIndex();
			int count = 0;
			buffer.writeInt(0); //allocate space to hold count.
			ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> blockIterator = sectionEntry.getValue().short2ObjectEntrySet().fastIterator();
			while (blockIterator.hasNext()) {
				Short2ObjectMap.Entry<LinkedList<BeamSegment>> blockEntry = blockIterator.next();
				for (BeamSegment segment : blockEntry.getValue()) {
					if (segment.visible()) {
						buffer.writeShort(blockEntry.getShortKey());
						buffer.writeByte(segment.direction().ordinal());
						buffer.writeUuid(segment.beam().uuid);
						buffer.writeMedium(BeamSegment.packRgb(segment.getEffectiveColor()));
						count++;
					}
				}
			}
			buffer.setInt(countPosition, count);
		}
	}

	public void applySyncPacket(PacketByteBuf buffer) {
		this.clear();
		CommonWorldBeamStorage world = CommonWorldBeamStorage.KEY.get(this.chunk.getWorld());
		int sectionCount = buffer.readVarInt();
		for (int sectionIndex = 0; sectionIndex < sectionCount; sectionIndex++) {
			int sectionY = buffer.readInt();
			CommonSectionBeamStorage section = this.getSection(sectionY);
			int segmentCount = buffer.readInt();
			for (int segmentIndex = 0; segmentIndex < segmentCount; segmentIndex++) {
				short position = buffer.readShort();
				BeamDirection direction = BeamDirection.VALUES[buffer.readByte()];
				UUID uuid = buffer.readUuid();
				Vector3f color = BeamSegment.unpackRgb(buffer.readUnsignedMedium());
				Beam beam = world.getBeam(uuid);
				if (beam == null) {
					BigTechMod.LOGGER.warn("Received beam segment for unknown beam ${uuid}");
					continue;
				}
				section.addSegment(position, new BeamSegment(beam, direction, true, color), false);
			}
		}
	}
}