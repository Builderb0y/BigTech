package builderb0y.bigtech.beams.storage;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PositionedBeamSegment;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;

public class SortedSectionBeamStorage extends Long2ObjectOpenHashMap<BasicSectionBeamStorage> {

	public boolean addSegment(PositionedBeamSegment segment, boolean unique) {
		return this.addSegment(segment.startPos, segment.segment, unique);
	}

	public boolean addSegment(BlockPos pos, BeamSegment segment, boolean unique) {
		return this.addSegment(pos.x, pos.y, pos.z, segment, unique);
	}

	public boolean addSegment(int x, int y, int z, BeamSegment segment, boolean unique) {
		return this.getSegments(x >> 4, y >> 4, z >> 4).addSegment(x, y, z, segment, unique);
	}

	public void removeSegment(PositionedBeamSegment segment) {
		this.removeSegment(segment.startPos, segment.segment);
	}

	public void removeSegment(BlockPos pos, BeamSegment segment) {
		this.removeSegment(pos.x, pos.y, pos.z, segment);
	}

	public void removeSegment(int x, int y, int z, BeamSegment segment) {
		this.getSegments(x >> 4, y >> 4, z >> 4).removeSegment(x, y, z, segment);
	}

	public BasicSectionBeamStorage getSegments(int sectionX, int sectionY, int sectionZ) {
		return this.getSegments(ChunkSectionPos.asLong(sectionX, sectionY, sectionZ));
	}

	public BasicSectionBeamStorage getSegments(long packedPosition) {
		return this.computeIfAbsent(
			packedPosition,
			(long position) -> new BasicSectionBeamStorage()
		);
	}
}