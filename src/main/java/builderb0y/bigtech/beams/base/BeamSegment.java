package builderb0y.bigtech.beams.base;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

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
		UUID uuid = tag.getUuid("uuid");
		Beam beam = storage.getBeam(uuid);
		if (beam == null) {
			BigTechMod.LOGGER.warn("Loaded segment from NBT which references non-existent beam: ${uuid}");
			return null;
		}
		byte dir = tag.getByte("dir");
		BeamDirection direction = BeamDirection.get(dir);
		if (direction == null) {
			BigTechMod.LOGGER.warn("Loaded beam segment with invalid direction: ${dir}");
			return null;
		}
		boolean visible = tag.getBoolean("vis");
		float[] color = tag.getFloatArray("color");
		Vector3f actualColor = color.length == 3 ? new Vector3f(color[0], color[1], color[2]) : null;
		return new BeamSegment(beam, direction, visible, actualColor);
	}

	public static @Nullable BeamSegment fromNbt(NbtCompound tag, Beam beam) {
		byte dir = tag.getByte("dir");
		BeamDirection direction = BeamDirection.get(dir);
		if (direction == null) {
			BigTechMod.LOGGER.warn("Loaded beam segment with invalid direction: ${dir}");
			return null;
		}
		boolean visible = tag.getBoolean("vis");
		float[] color = tag.getFloatArray("color");
		Vector3f actualColor = color.length == 3 ? new Vector3f(color[0], color[1], color[2]) : null;
		return new BeamSegment(beam, direction, visible, actualColor);
	}

	public static Vector3f unpackRgb(int color) {
		return new Vector3f(
			((color >>> 16) & 255) / 255.0F,
			((color >>>  8) & 255) / 255.0F,
			((color       ) & 255) / 255.0F
		);
	}

	public static int packRgb(Vector3fc rgb) {
		return MathHelper.packRgb(rgb.x(), rgb.y(), rgb.z());
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