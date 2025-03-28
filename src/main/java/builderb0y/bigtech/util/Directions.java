package builderb0y.bigtech.util;

import java.util.Arrays;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

public class Directions {

	public static final Direction[] ALL = Direction.values();
	public static final Direction[] ALL_INCLUDING_NULL = Arrays.copyOf(ALL, ALL.length + 1);
	public static final Direction[] HORIZONTAL = { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };
	public static final Axis[] AXES = Axis.VALUES;

	public static final Direction
		NEGATIVE_X = Direction.WEST,
		NEGATIVE_Y = Direction.DOWN,
		NEGATIVE_Z = Direction.NORTH,
		POSITIVE_X = Direction.EAST,
		POSITIVE_Y = Direction.UP,
		POSITIVE_Z = Direction.SOUTH;
}