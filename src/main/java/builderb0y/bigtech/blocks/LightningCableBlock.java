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
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.util.Enums;

public class LightningCableBlock extends ConnectingBlock implements LightningPulseInteractor, Waterloggable {

	public static final VoxelShape[] CONDUCTION_SHAPES = {
		VoxelShapes.cuboid(0.0D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D),
		VoxelShapes.cuboid(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),
		VoxelShapes.cuboid(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 1.0D),
	};

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
	public VoxelShape getConductionShape(BlockView world, BlockPos pos, BlockState state, Direction face) {
		return CONDUCTION_SHAPES[face.axis.ordinal()];
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (world instanceof World liveWorld) {
			return state.with(FACING_PROPERTIES.get(direction), this.canConnect(liveWorld, pos, state, neighborPos, neighborState, direction));
		}
		return state;
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

	public boolean canConnect(World world, BlockPos pos, BlockState state, Direction direction) {
		BlockPos adjacentPos = pos.offset(direction);
		BlockState adjacentState = world.getBlockState(adjacentPos);
		return this.canConnect(world, pos, state, adjacentPos, adjacentState, direction);
	}

	public boolean canConnect(World world, BlockPos pos, BlockState state, BlockPos adjacentPos, BlockState adjacentState, Direction direction) {
		if (adjacentState.getBlock() instanceof LightningCableBlock) {
			return adjacentState.isOf(this);
		}
		LightningPulseInteractor adjacentInteractor = LightningPulseInteractor.get(world, adjacentPos, adjacentState);
		return (
			(
				adjacentInteractor.canConductIn (world, adjacentPos, adjacentState, direction.opposite) ||
				adjacentInteractor.canConductOut(world, adjacentPos, adjacentState, direction.opposite)
			)
			&&
			VoxelShapes.matchesAnywhere(
				this.getConductionShape(world, pos, state, direction),
				adjacentInteractor.getConductionShape(world, adjacentPos, adjacentState, direction.opposite),
				BooleanBiFunction.AND
			)
		);
	}

	@Override
	public void forceSpreadOut(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		for (Direction direction : Enums.DIRECTIONS) {
			if (!state.get(FACING_PROPERTIES.get(direction))) continue;
			LinkedBlockPos adjacentPos = pos.offset(direction);
			if (pulse.hasNode(adjacentPos)) continue;
			BlockState adjacentState = world.getBlockState(adjacentPos);
			LightningPulseInteractor adjacentInteractor = LightningPulseInteractor.get(world, adjacentPos, adjacentState);
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