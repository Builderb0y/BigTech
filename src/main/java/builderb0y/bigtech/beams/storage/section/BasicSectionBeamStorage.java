package builderb0y.bigtech.beams.storage.section;

import java.util.LinkedList;
import java.util.Objects;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public class BasicSectionBeamStorage extends Short2ObjectOpenHashMap<LinkedList<BeamSegment>> {

	public BasicSectionBeamStorage() {
		super(32);
	}

	public boolean addSegment(SpreadingBeamSegment segment, boolean unique) {
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

	public void removeSegment(SpreadingBeamSegment segment) {
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

	public LinkedList<BeamSegment> checkSegments(BlockPos pos) {
		return this.checkSegments(pos.x, pos.y, pos.z);
	}

	public LinkedList<BeamSegment> checkSegments(int x, int y, int z) {
		return this.checkSegments(packIndex(x, y, z));
	}

	public LinkedList<BeamSegment> checkSegments(int index) {
		return this.get((short)(Objects.checkIndex(index, 4096)));
	}

	public static int packIndex(int x, int y, int z) {
		return ((x & 15) << 8) | ((z & 15) << 4) | (y & 15);
	}

	public static int unpackX(int packed) {
		return packed >>> 8;
	}

	public static int unpackY(int packed) {
		return packed & 15;
	}

	public static int unpackZ(int packed) {
		return (packed >>> 4) & 15;
	}

	public void addAll(BasicSectionBeamStorage that, boolean unique) {
		that.forEachSegment((short pos, BeamSegment segment) -> this.addSegment(pos, segment, unique));
	}

	public void removeAll(BasicSectionBeamStorage that) {
		that.forEachSegment(this::removeSegment);
	}

	public void forEachSegment(PackedPositionSegmentConsumer action) {
		ObjectIterator<Entry<LinkedList<BeamSegment>>> iterator = this.short2ObjectEntrySet().fastIterator();
		while (iterator.hasNext()) {
			Short2ObjectMap.Entry<LinkedList<BeamSegment>> entry = iterator.next();
			for (BeamSegment segment : entry.value) {
				action.accept(entry.shortKey, segment);
			}
		}
	}

	@FunctionalInterface
	public static interface PackedPositionSegmentConsumer {

		public abstract void accept(short packedPos, BeamSegment segment);
	}

	public void writeToNbt(NbtCompound tag, boolean includeUUID) {
		tag.putSubList("segments", (NbtList segmentsTag) -> {
			this.forEachSegment((short pos, BeamSegment segment) -> {
				segmentsTag.addCompound((NbtCompound segmentTag) -> {
					segment.toNbt(segmentTag.withShort("pos", pos), includeUUID);
				});
			});
		});
	}

	public void readFromNbt(NbtCompound tag, CommonWorldBeamStorage world) {
		this.clear();
		for (NbtCompound compound : tag.getList("segments", NbtElement.COMPOUND_TYPE).<Iterable<NbtCompound>>as()) {
			short position = compound.getShort("pos");
			BeamSegment segment = BeamSegment.fromNbt(compound, world);
			if (segment != null) this.addSegment(position, segment, true);
		}
	}

	public void readFromNbt(NbtCompound tag, Beam beam) {
		this.clear();
		for (NbtCompound compound : tag.getList("segments", NbtElement.COMPOUND_TYPE).<Iterable<NbtCompound>>as()) {
			short position = compound.getShort("pos");
			BeamSegment segment = BeamSegment.fromNbt(compound, beam);
			if (segment != null) this.addSegment(position, segment, true);
		}
	}
}