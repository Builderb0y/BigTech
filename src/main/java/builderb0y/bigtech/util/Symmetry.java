package builderb0y.bigtech.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3d;

public enum Symmetry {
	IDENTITY,
	ROTATE_90,
	ROTATE_180,
	ROTATE_270,
	FLIP_0,
	FLIP_45,
	FLIP_90,
	FLIP_135;

	public static final Symmetry[] VALUES = values();

	public int getX(int x, int z) {
		return switch (this) {
			case IDENTITY -> x;
			case ROTATE_90 -> -z;
			case ROTATE_180 -> -x;
			case ROTATE_270 -> z;
			case FLIP_0 -> -x;
			case FLIP_45 -> z;
			case FLIP_90 -> x;
			case FLIP_135 -> -z;
		};
	}

	public int getZ(int x, int z) {
		return switch (this) {
			case IDENTITY -> z;
			case ROTATE_90 -> x;
			case ROTATE_180 -> -z;
			case ROTATE_270 -> -x;
			case FLIP_0 -> z;
			case FLIP_45 -> x;
			case FLIP_90 -> -z;
			case FLIP_135 -> -x;
		};
	}

	public double getX(double x, double z) {
		return switch (this) {
			case IDENTITY -> x;
			case ROTATE_90 -> -z;
			case ROTATE_180 -> -x;
			case ROTATE_270 -> z;
			case FLIP_0 -> -x;
			case FLIP_45 -> z;
			case FLIP_90 -> x;
			case FLIP_135 -> -z;
		};
	}

	public double getZ(double x, double z) {
		return switch (this) {
			case IDENTITY -> z;
			case ROTATE_90 -> x;
			case ROTATE_180 -> -z;
			case ROTATE_270 -> -x;
			case FLIP_0 -> z;
			case FLIP_45 -> x;
			case FLIP_90 -> -z;
			case FLIP_135 -> -x;
		};
	}

	public BlockPos modifyBlockPos(BlockPos pos) {
		return new BlockPos(this.getX(pos.getX(), pos.getZ()), pos.getY(), this.getZ(pos.getX(), pos.getZ()));
	}

	public Vec3d modifyEntityPos(Vec3d pos) {
		return new Vec3d(this.getX(pos.getX(), pos.getZ()), pos.getY(), this.getZ(pos.getX(), pos.getZ()));
	}

	public BlockState modifyState(BlockState state) {
		return switch (this) {
			case IDENTITY -> state;
			case ROTATE_90 -> state.rotate(BlockRotation.CLOCKWISE_90);
			case ROTATE_180 -> state.rotate(BlockRotation.CLOCKWISE_180);
			case ROTATE_270 -> state.rotate(BlockRotation.COUNTERCLOCKWISE_90);
			case FLIP_0 -> state.mirror(BlockMirror.FRONT_BACK);
			case FLIP_45 -> state.rotate(BlockRotation.CLOCKWISE_90).mirror(BlockMirror.FRONT_BACK);
			case FLIP_90 -> state.mirror(BlockMirror.LEFT_RIGHT);
			case FLIP_135 -> state.rotate(BlockRotation.CLOCKWISE_90).mirror(BlockMirror.LEFT_RIGHT);
		};
	}

	public float modifyYaw(float yaw) {
		return switch (this) {
			case IDENTITY -> yaw;
			case ROTATE_90 -> 90.0F + yaw;
			case ROTATE_180 -> 180.0F + yaw;
			case ROTATE_270 -> 270.0F + yaw;
			case FLIP_0 -> -yaw;
			case FLIP_45 -> 270.0F - yaw;
			case FLIP_90 -> 180.0F - yaw;
			case FLIP_135 -> 90.0F - yaw;
		};
	}

	public Direction modifyDirection(Direction direction) {
		return direction.getAxis() == Axis.Y ? direction : switch (this) {
			case IDENTITY -> direction;
			case ROTATE_90 -> direction.rotateYClockwise();
			case ROTATE_180 -> direction.getOpposite();
			case ROTATE_270 -> direction.rotateYCounterclockwise();
			case FLIP_0 -> direction.getAxis() == Axis.X ? direction.getOpposite() : direction;
			case FLIP_45 -> switch (direction) {
				case NORTH -> Direction.WEST;
				case WEST -> Direction.NORTH;
				case SOUTH -> Direction.EAST;
				case EAST -> Direction.SOUTH;
				default -> direction;
			};
			case FLIP_90 -> direction.getAxis() == Axis.Z ? direction.getOpposite() : direction;
			case FLIP_135 -> switch (direction) {
				case NORTH -> Direction.EAST;
				case EAST -> Direction.NORTH;
				case SOUTH -> Direction.WEST;
				case WEST -> Direction.SOUTH;
				default -> direction;
			};
		};
	}
}