package builderb0y.bigtech.blocks;

import java.util.EnumMap;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class LightningDiodeBlock extends Block implements LightningPulseInteractor, Waterloggable {

	public static final MapCodec<LightningDiodeBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public static final EnumMap<Axis, VoxelShape> SHAPES = new EnumMap<>(Axis.class);
	static {
		SHAPES.put(Axis.X, VoxelShapes.cuboidUnchecked(0.0D,  0.25D, 0.25D, 1.0D,  0.75D, 0.75D));
		SHAPES.put(Axis.Y, VoxelShapes.cuboidUnchecked(0.25D, 0.0D,  0.25D, 0.75D, 1.0D,  0.75D));
		SHAPES.put(Axis.Z, VoxelShapes.cuboidUnchecked(0.25D, 0.25D, 0.0D,  0.75D, 0.75D, 1.0D ));
	}

	public LightningDiodeBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.FACING, Direction.NORTH)
			.with(Properties.POWERED, Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(Properties.FACING).getAxis());
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
		return state;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = world.isReceivingRedstonePower(pos);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered));
		}
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(
				Properties.FACING,
				context.getPlayer() != null && context.getPlayer().isSneaking()
				? context.getPlayerLookDirection()
				: context.getSide().getOpposite()
			)
			.with(
				Properties.POWERED,
				context.getWorld().isReceivingRedstonePower(context.getBlockPos())
			)
			.with(
				Properties.WATERLOGGED,
				context.getWorld().getFluidState(context.getBlockPos()).isEqualAndStill(Fluids.WATER)
			)
		);
	}

	@Override
	public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {

	}

	public Direction getOutputSide(BlockState state) {
		Direction direction = state.get(Properties.FACING);
		if (state.get(Properties.POWERED)) {
			direction = direction.getOpposite();
		}
		return direction;
	}

	public Direction getInputSide(BlockState state) {
		Direction direction = state.get(Properties.FACING);
		if (!state.get(Properties.POWERED)) {
			direction = direction.getOpposite();
		}
		return direction;
	}

	@Override
	public boolean canConductIn(WorldView world, BlockPos pos, BlockState state, @Nullable Direction side) {
		return side == null || side == this.getInputSide(state);
	}

	@Override
	public boolean canConductOut(WorldView world, BlockPos pos, BlockState state, Direction side) {
		return side == this.getOutputSide(state);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.FACING, mirror.apply(state.get(Properties.FACING)));
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING, Properties.POWERED, Properties.WATERLOGGED);
	}
}