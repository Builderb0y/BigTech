package builderb0y.bigtech.beams.storage.section;

import java.util.TreeSet;
import java.util.Objects;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.ReadView.ListReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.storage.WriteView.ListView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.util.Lockable;
import builderb0y.bigtech.util.Locked;

public class BasicSectionBeamStorage extends Short2ObjectOpenHashMap<Lockable<TreeSet<BeamSegment>>> {

	public final int sectionX, sectionY, sectionZ;

	public BasicSectionBeamStorage(int sectionX, int sectionY, int sectionZ) {
		super(32);
		this.sectionX = sectionX;
		this.sectionY = sectionY;
		this.sectionZ = sectionZ;
	}

	public BasicSectionBeamStorage(long packedPosition) {
		this(
			ChunkSectionPos.unpackX(packedPosition),
			ChunkSectionPos.unpackY(packedPosition),
			ChunkSectionPos.unpackZ(packedPosition)
		);
	}

	public boolean addSegment(SpreadingBeamSegment segment, boolean unique) {
		return this.addSegment(segment.startPos(), segment.segment(), unique);
	}

	public boolean addSegment(BlockPos pos, BeamSegment segment, boolean unique) {
		return this.addSegment(pos.getX(), pos.getY(), pos.getZ(), segment, unique);
	}

	public boolean addSegment(int x, int y, int z, BeamSegment segment, boolean unique) {
		return this.addSegment(packIndex(x, y, z), segment, unique);
	}

	public boolean addSegment(int index, BeamSegment segment, boolean unique) {
		try (Locked<TreeSet<BeamSegment>> segments = this.getSegments(index).write()) {
			if (unique && segments.value.contains(segment)) return false;
			return segments.value.add(segment);
		}
	}

	public void removeSegment(SpreadingBeamSegment segment) {
		this.removeSegment(segment.startPos(), segment.segment());
	}

	public void removeSegment(BlockPos pos, BeamSegment segment) {
		this.removeSegment(pos.getX(), pos.getY(), pos.getZ(), segment);
	}

	public void removeSegment(int x, int y, int z, BeamSegment segment) {
		this.removeSegment(packIndex(x, y, z), segment);
	}

	public void removeSegment(int index, BeamSegment segment) {
		boolean empty;
		try (Locked<TreeSet<BeamSegment>> segments = this.getSegments(index).write()) {
			if (!segments.value.remove(segment)) {
				BigTechMod.LOGGER.warn("Attempt to remove a beam segment which doesn't exist.");
			}
			empty = segments.value.isEmpty();
		}
		if (empty) this.remove((short)(index));
	}

	public Lockable<TreeSet<BeamSegment>> getSegments(BlockPos pos) {
		return this.getSegments(pos.getX(), pos.getY(), pos.getZ());
	}

	public Lockable<TreeSet<BeamSegment>> getSegments(int x, int y, int z) {
		return this.getSegments(packIndex(x, y, z));
	}

	public Lockable<TreeSet<BeamSegment>> getSegments(int index) {
		return this.computeIfAbsent(
			(short)(Objects.checkIndex(index, 4096)),
			(short s) -> new Lockable<>(new TreeSet<>())
		);
	}

	public @Nullable Lockable<TreeSet<BeamSegment>> checkSegments(BlockPos pos) {
		return this.checkSegments(pos.getX(), pos.getY(), pos.getZ());
	}

	public @Nullable Lockable<TreeSet<BeamSegment>> checkSegments(int x, int y, int z) {
		return this.checkSegments(packIndex(x, y, z));
	}

	public @Nullable Lockable<TreeSet<BeamSegment>> checkSegments(int index) {
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
		ObjectIterator<Entry<Lockable<TreeSet<BeamSegment>>>> iterator = this.short2ObjectEntrySet().fastIterator();
		while (iterator.hasNext()) {
			Short2ObjectMap.Entry<Lockable<TreeSet<BeamSegment>>> entry = iterator.next();
			try (Locked<TreeSet<BeamSegment>> segments = entry.getValue().read()) {
				for (BeamSegment segment : segments.value) {
					action.accept(entry.getShortKey(), segment);
				}
			}
		}
	}

	@FunctionalInterface
	public static interface PackedPositionSegmentConsumer {

		public abstract void accept(short packedPos, BeamSegment segment);
	}

	public void write(WriteView view, boolean includeUUID) {
		ListView segments = view.getList("segments");
		this.forEachSegment((short pos, BeamSegment segment) -> {
			segment.write(segments.add().withShort("pos", pos), includeUUID);
		});
	}

	public void read(ReadView tag, CommonWorldBeamStorage world) {
		this.clear();
		ListReadView segmentsView = tag.getOptionalListReadView("segments").orElse(null);
		if (segmentsView != null && !segmentsView.isEmpty()) {
			segmentsView.stream().forEach((ReadView segmentView) -> {
				short position = (short)(segmentView.getShort("pos", (short)(-1)));
				if (position < 0 || position >= 4096) return;
				BeamSegment segment = BeamSegment.read(segmentView, world);
				if (segment != null) this.addSegment(position, segment, true);
			});
		}
	}

	public void read(ReadView view, Beam beam) {
		this.clear();
		ListReadView segments = view.getOptionalListReadView("segments").orElse(null);
		if (segments != null && !segments.isEmpty()) {
			segments.stream().forEach((ReadView segmentView) -> {
				short position = segmentView.getShortProperly("pos", (short)(-1));
				if (position < 0 || position >= 4096) return;
				BeamSegment segment = BeamSegment.read(segmentView, beam);
				if (segment != null) this.addSegment(position, segment, true);
			});
		}
	}
}