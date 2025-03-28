package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class LightningCableBlock extends ConnectingBlock implements LightningPulseInteractor, Waterloggable {

	public static final MapCodec<LightningCableBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public static final VoxelShape[] CONDUCTION_SHAPES = {
		VoxelShapes.cuboidUnchecked(0.0D,  0.25D, 0.25D, 1.0D,  0.75D, 0.75D),
		VoxelShapes.cuboidUnchecked(0.25D, 0.0D,  0.25D, 0.75D, 1.0D,  0.75D),
		VoxelShapes.cuboidUnchecked(0.25D, 0.25D, 0.0D,  0.75D, 0.75D, 1.0D ),
	};

	public final float resistance;

	public LightningCableBlock(Settings settings, float resistance) {
		super(8.0F, settings);
		this.resistance = resistance;
		this.setDefaultState(
			this
			.getDefaultState()
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
	public VoxelShape getConductionShape(WorldView world, BlockPos pos, BlockState state, Direction face) {
		return CONDUCTION_SHAPES[face.getAxis().ordinal()];
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state,
		WorldView world,
		ScheduledTickView tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		Random random
	) {
		if (state.get(Properties.WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (world instanceof World liveWorld) {
			return state.with(FACING_PROPERTIES.get(direction), this.canConnect(liveWorld, pos, state, neighborPos, neighborState, direction));
		}
		return state;
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = this.getDefaultState();
		return (
			super
			.getPlacementState(context)
			.with(UP,    this.canConnect(world, pos, state, Direction.UP))
			.with(DOWN,  this.canConnect(world, pos, state, Direction.DOWN))
			.with(NORTH, this.canConnect(world, pos, state, Direction.NORTH))
			.with(EAST,  this.canConnect(world, pos, state, Direction.EAST))
			.with(SOUTH, this.canConnect(world, pos, state, Direction.SOUTH))
			.with(WEST,  this.canConnect(world, pos, state, Direction.WEST))
			.with(Properties.WATERLOGGED, world.getFluidState(pos).isEqualAndStill(Fluids.WATER))
		);
	}

	@Override
	public float getResistance(WorldView world, BlockPos pos, BlockState state, Direction side) {
		return this.resistance;
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
				adjacentInteractor.canConductIn (world, adjacentPos, adjacentState, direction.getOpposite()) ||
				adjacentInteractor.canConductOut(world, adjacentPos, adjacentState, direction.getOpposite())
			)
			&&
			VoxelShapes.matchesAnywhere(
				this.getConductionShape(world, pos, state, direction),
				adjacentInteractor.getConductionShape(world, adjacentPos, adjacentState, direction.getOpposite()),
				BooleanBiFunction.AND
			)
		);
	}

	@Override
	public boolean canConductOut(WorldView world, BlockPos pos, BlockState state, Direction side) {
		return state.get(FACING_PROPERTIES.get(side));
	}

	@Override
	public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		//no-op.
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return switch (rotation) {
			case NONE -> (
				state
			);
			case CLOCKWISE_90 -> (
				state
				.with(Properties.NORTH, state.get(Properties.WEST ))
				.with(Properties.EAST,  state.get(Properties.NORTH))
				.with(Properties.SOUTH, state.get(Properties.EAST ))
				.with(Properties.WEST,  state.get(Properties.SOUTH))
			);
			case CLOCKWISE_180 -> (
				state
				.with(Properties.NORTH, state.get(Properties.SOUTH))
				.with(Properties.EAST,  state.get(Properties.WEST ))
				.with(Properties.SOUTH, state.get(Properties.NORTH))
				.with(Properties.WEST,  state.get(Properties.EAST ))
			);
			case COUNTERCLOCKWISE_90 -> (
				state
				.with(Properties.NORTH, state.get(Properties.EAST ))
				.with(Properties.EAST,  state.get(Properties.SOUTH))
				.with(Properties.SOUTH, state.get(Properties.WEST ))
				.with(Properties.WEST,  state.get(Properties.NORTH))
			);
		};
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return switch (mirror) {
			case NONE -> (
				state
			);
			case LEFT_RIGHT -> (
				state
				.with(Properties.NORTH, state.get(Properties.SOUTH))
				.with(Properties.SOUTH, state.get(Properties.NORTH))
			);
			case FRONT_BACK -> (
				state
				.with(Properties.EAST, state.get(Properties.WEST))
				.with(Properties.WEST, state.get(Properties.EAST))
			);
		};
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, Properties.WATERLOGGED);
	}
}