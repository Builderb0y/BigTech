package builderb0y.bigtech.beams.storage.section;

import java.util.LinkedList;
import java.util.Objects;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.BeamSegment;
import builderb0y.bigtech.beams.PositionedBeamSegment;

public class BasicSectionBeamStorage extends Short2ObjectOpenHashMap<LinkedList<BeamSegment>> {

	public BasicSectionBeamStorage() {
		super(32);
	}

	public boolean addSegment(PositionedBeamSegment segment, boolean unique) {
		return this.addSegment(segment.startPos, segment.segment, unique);
	}

	public boolean addSegment(BlockPos pos, BeamSegment segment, boolean unique) {
		return this.addSegment(pos.x, pos.y, pos.z, segment, unique);
	}

	public boolean addSegment(int x, int y, int z, BeamSegment segment, boolean unique) {
		return this.addSegment(packIndex(x, y, z), segment, unique);
	}

	public boolean addSegment(int index, BeamSegment segment, boolean unique) {
		LinkedList<BeamSegment> segments = this.getSegments(index);
		if (unique && segments.contains(segment)) return false;
		return segments.add(segment);
	}

	public void removeSegment(PositionedBeamSegment segment) {
		this.removeSegment(segment.startPos, segment.segment);
	}

	public void removeSegment(BlockPos pos, BeamSegment segment) {
		this.removeSegment(pos.x, pos.y, pos.z, segment);
	}

	public void removeSegment(int x, int y, int z, BeamSegment segment) {
		this.removeSegment(packIndex(x, y, z), segment);
	}

	public void removeSegment(int index, BeamSegment segment) {
		LinkedList<BeamSegment> segments = this.getSegments(index);
		if (!segments.remove(segment)) {
			BigTechMod.LOGGER.warn("Attempt to remove a beam segment which doesn't exist.");
		}
		if (segments.isEmpty()) {
			this.remove((short)(index));
		}
	}

	public LinkedList<BeamSegment> getSegments(BlockPos pos) {
		return this.getSegments(pos.x, pos.y, pos.z);
	}

	public LinkedList<BeamSegment> getSegments(int x, int y, int z) {
		return this.getSegments(packIndex(x, y, z));
	}

	public LinkedList<BeamSegment> getSegments(int index) {
		return this.computeIfAbsent(
			(short)(Objects.checkIndex(index, 4096)),
			(short s) -> new LinkedList<>()
		);
	}

	public static int packIndex(int x, int y, int z) {
		return ((x & 15) << 8) | ((z & 15) << 4) | (y & 15);
	}

	public void addAll( BasicSectionBeamStorage that, boolean unique) {
		ObjectIterator<Entry<LinkedList<BeamSegment>>> iterator = that.short2ObjectEntrySet().fastIterator();
		while (iterator.hasNext()) {
			Short2ObjectMap.Entry<LinkedList<BeamSegment>> entry = iterator.next();
			for (BeamSegment segment : entry.value) {
				this.addSegment(entry.shortKey, segment, unique);
			}
		}
	}

	public void removeAll( BasicSectionBeamStorage that) {
		ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> iterator = that.short2ObjectEntrySet().fastIterator();
		while (iterator.hasNext()) {
			Short2ObjectMap.Entry<LinkedList<BeamSegment>> entry = iterator.next();
			for (BeamSegment segment : entry.value) {
				this.removeSegment(entry.shortKey, segment);
			}
		}
	}
}