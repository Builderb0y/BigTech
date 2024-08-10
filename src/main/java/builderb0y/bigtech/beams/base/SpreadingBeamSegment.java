package builderb0y.bigtech.beams.base;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import net.minecraft.util.math.BlockPos;

public record SpreadingBeamSegment(@NotNull BlockPos startPos, @NotNull BeamSegment segment, double distanceRemaining) {

	public SpreadingBeamSegment {
		startPos = startPos.toImmutable();
		Objects.requireNonNull(segment, "segment");
	}

	public BlockPos endPos() {
		return this.startPos.offset(this.segment.direction());
	}

	public Beam beam() {
		return this.segment.beam();
	}

	public BeamDirection direction() {
		return this.segment.direction();
	}

	public SpreadingBeamSegment withDirection(BeamDirection direction) {
		return this.withSegment(this.segment.withDirection(direction));
	}

	public boolean visible() {
		return this.segment.visible();
	}

	public SpreadingBeamSegment withVisibility(boolean visible) {
		return this.withSegment(this.segment.withVisibility(visible));
	}

	public @Nullable Vector3fc color() {
		return this.segment.color();
	}

	public SpreadingBeamSegment withColor(Vector3fc color) {
		return this.withSegment(this.segment.withColor(color));
	}

	public SpreadingBeamSegment withSegment(BeamSegment segment) {
		return this.segment == segment ? this : new SpreadingBeamSegment(this.startPos, segment, this.distanceRemaining);
	}

	public SpreadingBeamSegment extend() {
		BeamDirection direction = this.direction();
		double distanceToSubtract = direction.type.magnitude;
		return distanceToSubtract == 0.0D ? null : this.extend(this.distanceRemaining - distanceToSubtract, direction);
	}

	public SpreadingBeamSegment extend(double newDistance, BeamDirection newDirection) {
		return newDistance >= 0.0D ? new SpreadingBeamSegment(this.endPos(), this.segment.withDirection(newDirection), newDistance) : this.terminate();
	}

	public SpreadingBeamSegment terminate() {
		return new SpreadingBeamSegment(this.endPos(), this.segment.withDirection(BeamDirection.CENTER), 0.0D);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (
			obj instanceof SpreadingBeamSegment that &&
			this.startPos.equals(that.startPos) &&
			this.segment.equals(that.segment)
		);
	}

	@Override
	public int hashCode() {
		return this.startPos.hashCode() * 31 + this.segment.hashCode();
	}
}