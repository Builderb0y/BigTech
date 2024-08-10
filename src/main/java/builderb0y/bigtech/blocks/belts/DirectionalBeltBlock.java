package builderb0y.bigtech.blocks.belts;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import builderb0y.bigtech.api.AscenderInteractor;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class DirectionalBeltBlock extends AbstractBeltBlock {

	public static final MapCodec<DirectionalBeltBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public DirectionalBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int getAscenderPriority(World world, BlockPos pos, BlockState state, Direction face) {
		if (face == Direction.UP) return AscenderInteractor.BELT_TOP;
		if (face == Direction.DOWN) return AscenderInteractor.BLOCKED;
		Direction facing = state.get(Properties.HORIZONTAL_FACING);
		if (face == facing) return AscenderInteractor.BLOCKED;
		if (face == facing.getOpposite()) return AscenderInteractor.BELT_BACK;
		return AscenderInteractor.BELT_SIDE;
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
		Vec3d oldMotion = entity.getVelocity();
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
		switch (direction.getAxis()) {
			case X -> newZ += (pos.getZ() + 0.5D - entity.getZ()) * 0.25D;
			case Z -> newX += (pos.getX() + 0.5D - entity.getX()) * 0.25D;
			case Y -> throw new AssertionError(direction);
		}
		entity.setVelocity(newX, oldMotion.y, newZ);
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing());
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return context.getStack().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractBeltBlock && !context.shouldCancelInteraction();
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING);
	}
}