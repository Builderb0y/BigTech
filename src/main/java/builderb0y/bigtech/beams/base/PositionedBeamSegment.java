package builderb0y.bigtech.beams.base;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.minecraft.util.math.BlockPos;

public record PositionedBeamSegment(@NotNull BlockPos startPos, @NotNull BeamSegment segment) {

	public PositionedBeamSegment {
		startPos = startPos.toImmutable();
		Objects.requireNonNull(segment, "segment");
	}

	public BlockPos getEndPos() {
		return this.segment.getEndPos(this.startPos);
	}

	public PositionedBeamSegment extend() {
		BeamSegment extension = this.segment.extend();
		return extension != null ? new PositionedBeamSegment(this.endPos, extension) : null;
	}

	public PositionedBeamSegment terminate() {
		return new PositionedBeamSegment(this.endPos, this.segment.terminate());
	}
}