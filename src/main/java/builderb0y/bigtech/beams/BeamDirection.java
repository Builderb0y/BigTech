package builderb0y.bigtech.beams;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public enum BeamDirection {
	DOWN_NORTH_WEST,
	DOWN_NORTH,
	DOWN_NORTH_EAST,
	DOWN_WEST,
	DOWN,
	DOWN_EAST,
	DOWN_SOUTH_WEST,
	DOWN_SOUTH,
	DOWN_SOUTH_EAST,
	NORTH_WEST,
	NORTH,
	NORTH_EAST,
	WEST,
	CENTER,
	EAST,
	SOUTH_WEST,
	SOUTH,
	SOUTH_EAST,
	UP_NORTH_WEST,
	UP_NORTH,
	UP_NORTH_EAST,
	UP_WEST,
	UP,
	UP_EAST,
	UP_SOUTH_WEST,
	UP_SOUTH,
	UP_SOUTH_EAST;

	public static final BeamDirection[] VALUES = values();

	public final int x, y, z;
	public final Type type;

	BeamDirection() {
		this.x = ( this.ordinal()      % 3) - 1;
		this.z = ((this.ordinal() / 3) % 3) - 1;
		this.y = ((this.ordinal() / 9) % 3) - 1;
		this.type = Type.VALUES[Math.abs(this.x) + Math.abs(this.y) + Math.abs(this.z)];
	}

	public static BeamDirection from(Direction from) {
		return switch (from) {
			case UP    -> UP;
			case DOWN  -> DOWN;
			case NORTH -> NORTH;
			case EAST  -> EAST;
			case SOUTH -> SOUTH;
			case WEST  -> WEST;
		};
	}

	public static BeamDirection get(int x, int y, int z) {
		return getUnchecked(
			Math.max(Math.min(x, 1), -1),
			Math.max(Math.min(y, 1), -1),
			Math.max(Math.min(z, 1), -1)
		);
	}

	public static BeamDirection getUnchecked(int x, int y, int z) {
		return VALUES[multiplyBy3(multiplyBy3(y) + z) + x + CENTER.ordinal()];
	}

	public static int multiplyBy3(int number) {
		return number + (number << 1);
	}

	public BeamDirection getOpposite() {
		return VALUES[26 - this.ordinal()];
	}

	public BeamDirection reflect(double x, double y, double z) {
		double scalar = 1.0D / MathHelper.magnitude(x, y, z);
		if (!Double.isFinite(scalar)) return this;
		return this.reflectUnchecked(x * scalar, y * scalar, z * scalar);
	}

	public BeamDirection reflectUnchecked(double x, double y, double z) {
		double dot2 = 2.0D * (this.x * x + this.y * y + this.z * z);
		int newX = MathHelper.floor(this.x - dot2 * x + 0.5D);
		int newY = MathHelper.floor(this.y - dot2 * y + 0.5D);
		int newZ = MathHelper.floor(this.z - dot2 * z + 0.5D);
		return get(newX, newY, newZ);
	}

	public static enum Type {
		CENTER,
		FACE,
		EDGE,
		CORNER;

		public static final Type[] VALUES = values();

		public final double magnitude, reciprocalMagnitude;

		Type() {
			this.magnitude = Math.sqrt(this.ordinal());
			this.reciprocalMagnitude = 1.0D / this.magnitude;
		}
	}
}