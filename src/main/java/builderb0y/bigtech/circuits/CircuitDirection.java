package builderb0y.bigtech.circuits;

import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public enum CircuitDirection {
	FRONT,
	RIGHT,
	BACK,
	LEFT;

	public static final CircuitDirection[] VALUES = values();

	public Direction toMcDirection() {
		return switch (this) {
			case FRONT -> Direction.NORTH;
			case RIGHT -> Direction.EAST;
			case BACK  -> Direction.SOUTH;
			case LEFT  -> Direction.WEST;
		};
	}

	public Direction toMcDirection(BlockRotation rotation) {
		return rotation.rotate(this.toMcDirection());
	}

	public CircuitDirection rotate(int by) {
		return VALUES[(this.ordinal() + by) & 3];
	}

	public CircuitDirection opposite() {
		return this.rotate(2);
	}

	public CircuitDirection rotate(CircuitRotation orientation) {
		return this.rotate(orientation.ordinal());
	}

	public CircuitDirection unrotate(int by) {
		return this.rotate(-by);
	}

	public CircuitDirection unrotate(CircuitRotation orientation) {
		return this.unrotate(orientation.ordinal());
	}
}