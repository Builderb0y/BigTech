package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.lightning.LightningPulseInteractor;
import builderb0y.bigtech.lightning.LightningPulseInteractors;
import builderb0y.bigtech.util.Enums;

public class LightningCableBlock extends ConnectingBlock implements LightningPulseInteractor, Waterloggable {

	public LightningCableBlock(Settings settings) {
		super(0.25F, settings);
		this.defaultState = (
			this.defaultState
			.with(UP,    Boolean.FALSE)
			.with(DOWN,  Boolean.FALSE)
			.with(NORTH, Boolean.FALSE)
			.with(EAST,  Boolean.FALSE)
			.with(SOUTH, Boolean.FALSE)
			.with(WEST,  Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return state.with(FACING_PROPERTIES.get(direction), this.canConnect(world, pos, state, neighborPos, neighborState, direction));
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(UP,    this.canConnect(context.world, context.blockPos, this.defaultState, Direction.UP))
			.with(DOWN,  this.canConnect(context.world, context.blockPos, this.defaultState, Direction.DOWN))
			.with(NORTH, this.canConnect(context.world, context.blockPos, this.defaultState, Direction.NORTH))
			.with(EAST,  this.canConnect(context.world, context.blockPos, this.defaultState, Direction.EAST))
			.with(SOUTH, this.canConnect(context.world, context.blockPos, this.defaultState, Direction.SOUTH))
			.with(WEST,  this.canConnect(context.world, context.blockPos, this.defaultState, Direction.WEST))
			.with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)
		);
	}

	public boolean canConnect(WorldAccess world, BlockPos pos, BlockState state, Direction direction) {
		BlockPos adjacentPos = pos.offset(direction);
		BlockState adjacentState = world.getBlockState(adjacentPos);
		return this.canConnect(world, pos, state, adjacentPos, adjacentState, direction);
	}

	public boolean canConnect(WorldAccess world, BlockPos pos, BlockState state, BlockPos adjacentPos, BlockState adjacentState, Direction direction) {
		return adjacentState.isOf(this) || LightningPulseInteractor.canConductThrough(
			world,
			this,
			pos,
			state.with(FACING_PROPERTIES.get(direction), Boolean.TRUE),
			LightningPulseInteractors.forBlock(world, adjacentPos, adjacentState),
			adjacentPos,
			adjacentState,
			direction
		);
	}

	@Override
	public void forceSpreadOut(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		for (Direction direction : Enums.DIRECTIONS) {
			if (!state.get(FACING_PROPERTIES.get(direction))) continue;
			LinkedBlockPos adjacentPos = pos.offset(direction);
			if (pulse.hasNode(adjacentPos)) continue;
			BlockState adjacentState = world.getBlockState(adjacentPos);
			LightningPulseInteractor adjacentInteractor = LightningPulseInteractors.forBlock(world, adjacentPos, adjacentState);
			adjacentInteractor.spreadIn(world, adjacentPos, adjacentState, pulse);
		}
	}

	@Override
	public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		//no-op.
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
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, Properties.WATERLOGGED);
	}
}