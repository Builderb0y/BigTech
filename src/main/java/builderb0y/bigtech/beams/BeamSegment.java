package builderb0y.bigtech.beams;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public record BeamSegment(
	Beam beam,
	BeamDirection direction,
	double distanceRemaining,
	boolean visible,
	@Nullable Vector3f color
) {

	public BlockPos getEndPos(BlockPos startPos) {
		return startPos.offset(this.direction);
	}

	public Vector3f getEffectiveColor() {
		return this.color != null ? this.color : this.beam.initialColor;
	}

	public BeamSegment withDirection(BeamDirection direction) {
		return this.direction == direction ? this : new BeamSegment(this.beam, direction, this.distanceRemaining, this.visible, this.color);
	}

	public BeamSegment withDistance(double distance) {
		if (!(distance >= 0.0D)) throw new IllegalArgumentException("Invalid distance: ${distance}");
		return this.distanceRemaining == distance ? this : new BeamSegment(this.beam, this.direction, distance, this.visible, this.color);
	}

	public BeamSegment addDistance(double distance, boolean terminate) {
		double newDistance = this.distanceRemaining + distance;
		return newDistance >= 0.0D ? this.withDistance(distance) : terminate ? this.terminate() : null;
	}

	public BeamSegment visible(boolean visible) {
		return this.visible == visible ? this : new BeamSegment(this.beam, this.direction, this.distanceRemaining, visible, this.color);
	}

	public BeamSegment withColor(@Nullable Vector3f color) {
		return this.color == color ? this : new BeamSegment(this.beam, this.direction, this.distanceRemaining, this.visible, color);
	}

	public @Nullable BeamSegment extend() {
		double distanceToSubtract = this.direction.type.magnitude;
		if (distanceToSubtract != 0.0D && this.distanceRemaining >= distanceToSubtract) {
			return new BeamSegment(this.beam, this.direction, this.distanceRemaining - distanceToSubtract, this.visible, this.color);
		}
		return null;
	}

	public BeamSegment terminate() {
		return new BeamSegment(this.beam, BeamDirection.CENTER, 0.0D, false, this.color);
	}

	public void toNbt(NbtCompound compound) {
		compound
			.withUuid("uuid", this.beam.uuid)
			.withByte("dir", (byte)(this.direction.ordinal()))
			.withBoolean("vis", this.visible);
		if (this.color != null) compound.putFloatArray("color", new float[] { this.color.x, this.color.y, this.color.z });
	}

	public static @Nullable BeamSegment fromNbt(NbtCompound tag, CommonWorldBeamStorage storage) {
		UUID uuid = tag.getUuid("uuid");
		Beam beam = storage.getBeam(uuid);
		//it is fully expected that a beam can be removed while its segments are unloaded.
		//these segments should quietly disappear next time they are loaded.
		if (beam == null) return null;
		BeamDirection direction = BeamDirection.VALUES[tag.getByte("dir")];
		boolean visible = tag.getBoolean("vis");
		float[] color = tag.getFloatArray("color");
		Vector3f actualColor = color.length == 3 ? new Vector3f(color[0], color[1], color[2]) : null;
		return new BeamSegment(beam, direction, 0.0D, visible, actualColor);
	}

	public static Vector3f unpackRgb(int color) {
		return new Vector3f(
			((color >>> 16) & 255) / 255.0F,
			((color >>> 8) & 255) / 255.0F,
			((color) & 255) / 255.0F
		);
	}

	public static int packRgb(Vector3f rgb) {
		return MathHelper.packRgb(rgb.x, rgb.y, rgb.z);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (
			obj instanceof BeamSegment that &&
			this.beam == that.beam &&
			this.direction == that.direction
		);
	}

	@Override
	public int hashCode() {
		int hash = System.identityHashCode(this.beam);
		hash = hash * 31 + this.direction.hashCode();
		return hash;
	}
}