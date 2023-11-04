package builderb0y.bigtech.beams.storage.section;

import java.util.stream.Collectors;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.BeamSegment;
import builderb0y.bigtech.beams.PositionedBeamSegment;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public abstract class CommonSectionBeamStorage extends BasicSectionBeamStorage {

	public final WorldChunk chunk;
	public final int sectionCoordY;

	public CommonSectionBeamStorage(WorldChunk chunk, int sectionCoordY) {
		this.chunk = chunk;
		this.sectionCoordY = sectionCoordY;
	}

	public void writeToNbt(NbtCompound tag) {
		tag.put(
			"segments",
			this.short2ObjectEntrySet().stream().flatMap(entry -> {
				return entry.value.stream().map(segment -> {
					NbtCompound compound = new NbtCompound().withShort("pos", entry.shortKey);
					segment.toNbt(compound);
					return compound;
				});
			})
			.collect(Collectors.toCollection(NbtList::new))
		);
	}

	public void readFromNbt(NbtCompound tag) {
		CommonWorldBeamStorage world = CommonWorldBeamStorage.KEY.get(this.chunk.world);
		int startX = this.chunk.pos.startX;
		int startY = this.sectionCoordY << 4;
		int startZ = this.chunk.pos.startZ;
		tag
		.getList("segments", NbtElement.COMPOUND_TYPE)
		.stream()
		.map(NbtCompound.class::cast)
		.map(compound -> {
			short position = compound.getShort("pos");
			return new PositionedBeamSegment(
				new BlockPos(
					startX | ChunkSectionPos.unpackLocalX(position),
					startY | ChunkSectionPos.unpackLocalY(position),
					startZ | ChunkSectionPos.unpackLocalZ(position)
				),
				BeamSegment.fromNbt(compound, world)
			);
		})
		.forEach(segment -> this.addSegment(segment, true));
	}

	public abstract void tick();
}