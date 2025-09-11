package builderb0y.bigtech.beams;

import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.base.Predicates;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.util.Lockable;
import builderb0y.bigtech.util.Locked;

public class BeamUtil {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Predicate<BeamSegment>
		ANY_SEGMENT = new Predicate() { //use raw type to avoid bridge methods.

			@Override
			public boolean test(Object object) {
				return true;
			}

			@Override
			public @NotNull Predicate or(@NotNull Predicate other) {
				return this;
			}

			@Override
			public @NotNull Predicate and(@NotNull Predicate other) {
				return other;
			}

			@Override
			public @NotNull Predicate negate() {
				return Predicates.alwaysFalse();
			}

			@Override
			public String toString() {
				return "BeamUtil.ANY_SEGMENT";
			}
		},
		VISIBLE = BeamSegment::visible;

	public static Predicate<BeamSegment> checkDirection(BeamDirection direction) {
		return (BeamSegment segment) -> segment.direction() == direction;
	}

	public static boolean hasSegmentLeadingInto(Beam beam, BlockPos pos, BeamDirection from, Predicate<BeamSegment> predicate) {
		return hasSegmentLeadingOutOf(beam, pos.offset(from), predicate.and(checkDirection(from.getOpposite())));
	}

	public static boolean hasSegmentLeadingOutOf(Beam beam, BlockPos pos, Predicate<BeamSegment> predicate) {
		BasicSectionBeamStorage sectionStorage = beam.seen.get(ChunkSectionPos.toLong(pos));
		if (sectionStorage != null) {
			Lockable<TreeSet<BeamSegment>> segments = sectionStorage.checkSegments(pos);
			if (segments != null) {
				try (Locked<TreeSet<BeamSegment>> locked = segments.read()) {
					for (BeamSegment segment : locked.value) {
						if (predicate.test(segment)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean hasSegmentLeadingInto(World world, BlockPos pos, BeamDirection from, Predicate<BeamSegment> predicate) {
		return hasSegmentLeadingOutOf(world, pos.offset(from), predicate.and(checkDirection(from.getOpposite())));
	}

	public static boolean hasSegmentLeadingOutOf(World world, BlockPos pos, Predicate<BeamSegment> predicate) {
		CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(world.getChunk(pos)).require().get(pos.getY() >> 4);
		if (sectionStorage != null) {
			Lockable<TreeSet<BeamSegment>> segments = sectionStorage.checkSegments(pos);
			if (segments != null) {
				try (Locked<TreeSet<BeamSegment>> locked = segments.read()) {
					for (BeamSegment segment : locked.value) {
						if (predicate.test(segment)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static Stream<BeamSegment> getSegmentsLeadingInto(Beam beam, BlockPos pos, BeamDirection from, Predicate<BeamSegment> predicate) {
		return getSegmentsLeadingOutOf(beam, pos.offset(from), predicate.and(checkDirection(from.getOpposite())));
	}

	public static Stream<BeamSegment> getSegmentsLeadingOutOf(Beam beam, BlockPos pos, Predicate<BeamSegment> predicate) {
		BasicSectionBeamStorage sectionStorage = beam.seen.get(ChunkSectionPos.toLong(pos));
		if (sectionStorage != null) {
			Lockable<TreeSet<BeamSegment>> segments = sectionStorage.checkSegments(pos);
			if (segments != null) {
				Locked<TreeSet<BeamSegment>> lock = segments.read();
				return segments.value.stream().filter(predicate).onClose(lock::close);
			}
		}
		return Stream.empty();
	}

	public static Stream<BeamSegment> getSegmentsLeadingInto(World world, BlockPos pos, BeamDirection from, Predicate<BeamSegment> predicate) {
		return getSegmentsLeadingOutOf(world, pos.offset(from), predicate.and(checkDirection(from.getOpposite())));
	}

	public static Stream<BeamSegment> getSegmentsLeadingOutOf(World world, BlockPos pos, Predicate<BeamSegment> predicate) {
		CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(world.getChunk(pos)).require().get(pos.getY() >> 4);
		if (sectionStorage != null) {
			Lockable<TreeSet<BeamSegment>> segments = sectionStorage.checkSegments(pos);
			if (segments != null) {
				Locked<TreeSet<BeamSegment>> lock = segments.read();
				return segments.value.stream().filter(predicate).onClose(lock::close);
			}
		}
		return Stream.empty();
	}
}