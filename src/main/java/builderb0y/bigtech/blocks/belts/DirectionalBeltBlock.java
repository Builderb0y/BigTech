package builderb0y.bigtech.blocks.belts;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DirectionalBeltBlock extends AbstractBeltBlock {

	public DirectionalBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public AscenderIOType getAscenderIOType(World world, BlockPos pos, BlockState state, Direction face) {
		if (face == Direction.UP) return AscenderIOType.SECONDARY_INPUT;
		if (face == Direction.DOWN) return AscenderIOType.NO_INPUT;
		Direction facing = state.get(Properties.HORIZONTAL_FACING);
		if (face == facing) return AscenderIOType.NO_INPUT;
		if (face == facing.opposite) return AscenderIOType.PRIMARY_INPUT;
		return AscenderIOType.SECONDARY_INPUT;
	}

	public double getSpeed(World world, BlockPos pos, BlockState state, Entity entity) {
		return 0.75D;
	}

	public Direction getDirection(World world, BlockPos pos, BlockState state, Entity entity) {
		return state.get(Properties.HORIZONTAL_FACING);
	}

	@Override
	public void move(World world, BlockPos pos, BlockState state, Entity entity) {
		Direction direction = this.getDirection(world, pos, state, entity);
		double speed = this.getSpeed(world, pos, state, entity);
		Vec3d oldMotion = entity.velocity;
		double
			newX = oldMotion.x,
			newZ = oldMotion.z;
		//force calculations:
		//if the entity is already moving forward quickly,
		//we want to give it less of a push.
		//this is to prevent the entity from gaining excessive velocities.
		//at the same time, if the entity is moving in
		//the opposite direction we're trying to push it,
		//then we'll want to push it less in that case too.
		//this is so that entities (specifically players) can walk
		//backwards on belts without being pushed around too much.
		//the amount of force applied should decrease gradually near these two extremes,
		//and a parabolic curve is a very simple way to achieve this.
		final double controlFactor = 0.2D;
		double force = switch (direction) {
			case NORTH -> (controlFactor - newZ) * (speed + newZ);
			case SOUTH -> (controlFactor + newZ) * (speed - newZ);
			case EAST  -> (controlFactor + newX) * (speed - newX);
			case WEST  -> (controlFactor - newX) * (speed + newX);
			case UP, DOWN -> throw new AssertionError(direction);
		}
		/ (speed + controlFactor);

		if (force > 0.0D) switch (direction) {
			case NORTH -> newZ -= force;
			case SOUTH -> newZ += force;
			case EAST  -> newX += force;
			case WEST  -> newX -= force;
			case UP, DOWN -> throw new AssertionError();
		}
		//move towards center of the belt.
		switch (direction.axis) {
			case X -> newZ += (pos.z + 0.5D - entity.z) * 0.25D;
			case Z -> newX += (pos.x + 0.5D - entity.x) * 0.25D;
			case Y -> throw new AssertionError(direction.axis);
		}
		entity.setVelocity(newX, oldMotion.y, newZ);
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.horizontalPlayerFacing);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING);
	}
}