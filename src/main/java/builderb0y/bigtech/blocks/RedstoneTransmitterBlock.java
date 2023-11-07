package builderb0y.bigtech.blocks;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.RedstoneBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.util.Enums;

public class RedstoneTransmitterBlock extends Block implements Waterloggable {

	public static VoxelShape NORTH_SOUTH_SHAPE = VoxelShapes.union(
		VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 3.0D / 16.0D, 1.0D),
		VoxelShapes.cuboid(3.0D / 16.0D, 3.0D / 16.0D, 3.0D / 16.0D, 13.0D / 16.0D, 4.0D / 16.0D, 5.0D / 16.0D),
		VoxelShapes.cuboid(3.0D / 16.0D, 3.0D / 16.0D, 11.0D / 16.0D, 13.0D / 16.0D, 4.0D / 16.0D, 13.0D / 16.0D),
		VoxelShapes.cuboid(4.0D / 16.0D, 4.0D / 16.0D, 1.0D / 16.0D, 12.0D / 16.0D, 12.0D / 16.0D, 15.0D / 16.0D)
	);
	public static VoxelShape EAST_WEST_SHAPE = VoxelShapes.union(
		VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 3.0D / 16.0D, 1.0D),
		VoxelShapes.cuboid(3.0D / 16.0D, 3.0D / 16.0D, 3.0D / 16.0D, 5.0D / 16.0D, 4.0D / 16.0D, 13.0D / 16.0D),
		VoxelShapes.cuboid(11.0D / 16.0D, 3.0D / 16.0D, 3.0D / 16.0D, 13.0D / 16.0D, 4.0D / 16.0D, 13.0D / 16.0D),
		VoxelShapes.cuboid(1.0D / 16.0D, 4.0D / 16.0D, 4.0D / 16.0D, 15.0D / 16.0D, 12.0D / 16.0D, 12.0D / 16.0D)
	);

	public RedstoneTransmitterBlock(Settings settings) {
		super(settings);
		this.defaultState = (
			this.defaultState
			.with(Properties.POWERED, Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	public boolean shouldBePowered(RedstoneView world, BlockPos pos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if (world.getEmittedRedstonePower(mutable.set(pos, Direction.DOWN), Direction.DOWN) > 0) return true;
		for (Direction direction : Enums.HORIZONTAL_DIRECTIONS) {
			if (world.getEmittedRedstonePower(mutable.set(pos, direction), direction) > 0) return true;
		}
		return false;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		PersistentBeam oldBeam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
		if (oldBeam != null) oldBeam.removeFromWorld();
		PersistentBeam newBeam = new RedstoneBeam(world, UUID.randomUUID());
		newBeam.fire(pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		return false;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = this.shouldBePowered(world, pos);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered), Block.NOTIFY_ALL);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean moved) {
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, moved);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = this.shouldBePowered(world, pos);
		if (powered != shouldBePowered) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onBlockAdded(state, world, pos, oldState, moved);
		if (state.get(Properties.POWERED)) {
			PersistentBeam beam = new RedstoneBeam(world, UUID.randomUUID());
			beam.fire(pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (state.get(Properties.POWERED)) {
			PersistentBeam beam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
			if (beam != null) beam.removeFromWorld();
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(Properties.HORIZONTAL_FACING)) {
			case NORTH, SOUTH -> NORTH_SOUTH_SHAPE;
			case EAST, WEST -> EAST_WEST_SHAPE;
			case UP, DOWN -> throw new AssertionError("vertical redstone transmitter?");
		};
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super.getPlacementState(context)
			.with(Properties.HORIZONTAL_FACING, context.horizontalPlayerFacing.opposite)
			.with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).isEqualAndStill(Fluids.WATER))
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).defaultState;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING, Properties.POWERED, Properties.WATERLOGGED);
	}
}