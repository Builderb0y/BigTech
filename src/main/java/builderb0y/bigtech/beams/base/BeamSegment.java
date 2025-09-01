package builderb0y.bigtech.beams.base;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.nbt.NbtCompound;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.util.ColorF;

public record BeamSegment(
	Beam beam,
	BeamDirection direction,
	boolean visible,
	@Nullable Vector3fc color
) {

	public Vector3fc getEffectiveColor() {
		return this.color != null ? this.color : this.beam.getInitialColor();
	}

	public BeamSegment withDirection(BeamDirection direction) {
		return this.direction == direction ? this : new BeamSegment(this.beam, direction, this.visible, this.color);
	}

	public BeamSegment withVisibility(boolean visible) {
		return this.visible == visible ? this : new BeamSegment(this.beam, this.direction, visible, this.color);
	}

	public BeamSegment withColor(@Nullable Vector3fc color) {
		return this.color == color ? this : new BeamSegment(this.beam, this.direction, this.visible, color);
	}

	public void toNbt(NbtCompound compound, boolean includeUUID) {
		if (includeUUID) compound.putUuid("uuid", this.beam.uuid);
		compound
		.withByte("dir", (byte)(this.direction.ordinal()))
		.withBoolean("vis", this.visible);
		if (this.color != null) compound.putFloatArray("color", new float[] { this.color.x(), this.color.y(), this.color.z() });
	}

	public static @Nullable BeamSegment fromNbt(NbtCompound tag, CommonWorldBeamStorage storage) {
		UUID uuid = tag.getUuid("uuid").orElse(null);
		if (uuid == null) {
			BigTechMod.LOGGER.warn("Loaded beam segment from NBT which has no UUID");
			return null;
		}
		Beam beam = storage.getBeam(uuid);
		if (beam == null) {
			BigTechMod.LOGGER.warn("Loaded beam segment from NBT which references non-existent beam: ${uuid}");
			return null;
		}
		BeamDirection direction = tag.getArray("dir", BeamDirection.VALUES).orElse(null);
		if (direction == null) {
			BigTechMod.LOGGER.warn("Loaded beam segment with invalid direction");
			return null;
		}
		boolean visible = tag.getBoolean("vis", true);
		float[] color = tag.getFloatArray("color").orElse(null);
		Vector3f actualColor = color != null && color.length == 3 ? new Vector3f(color[0], color[1], color[2]) : null;
		return new BeamSegment(beam, direction, visible, actualColor);
	}

	public static @Nullable BeamSegment fromNbt(NbtCompound tag, Beam beam) {
		BeamDirection direction = tag.getArray("dir", BeamDirection.VALUES).orElse(null);
		if (direction == null) {
			BigTechMod.LOGGER.warn("Loaded beam segment with invalid direction");
			return null;
		}
		boolean visible = tag.getBoolean("vis").orElse(true);
		float[] color = tag.getFloatArray("color").orElse(null);
		Vector3f actualColor = color != null && color.length == 3 ? new Vector3f(color[0], color[1], color[2]) : null;
		return new BeamSegment(beam, direction, visible, actualColor);
	}

	public static Vector3f unpackRgb(int color) {
		return ColorF.toVector(color);
	}

	public static int packRgb(Vector3fc rgb) {
		return ColorF.toInt(rgb);
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