package builderb0y.bigtech.blocks;

import java.util.EnumMap;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.Directions;

public class LedBlock extends Block implements Waterloggable {

	public static final MapCodec<LedBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public static final EnumMap<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);
	static {
		for (Direction facing : Directions.ALL) {
			SHAPES.put(
				facing,
				VoxelShapes.union(
					generateShape(facing, 0.375D, 0.5D, 0.25D),
					generateShape(facing, 0.25D, 0.375D, 0.125D)
				)
			);
		}
	}

	public static VoxelShape generateShape(Direction facing, double minInOut, double maxInOut, double radius) {
		return VoxelShapes.cuboidUnchecked(
			facing.getAxis() == Axis.X ? Math.min(facing.getOffsetX() * minInOut, facing.getOffsetX() * maxInOut) + 0.5D : 0.5D - radius,
			facing.getAxis() == Axis.Y ? Math.min(facing.getOffsetY() * minInOut, facing.getOffsetY() * maxInOut) + 0.5D : 0.5D - radius,
			facing.getAxis() == Axis.Z ? Math.min(facing.getOffsetZ() * minInOut, facing.getOffsetZ() * maxInOut) + 0.5D : 0.5D - radius,
			facing.getAxis() == Axis.X ? Math.max(facing.getOffsetX() * minInOut, facing.getOffsetX() * maxInOut) + 0.5D : 0.5D + radius,
			facing.getAxis() == Axis.Y ? Math.max(facing.getOffsetY() * minInOut, facing.getOffsetY() * maxInOut) + 0.5D : 0.5D + radius,
			facing.getAxis() == Axis.Z ? Math.max(facing.getOffsetZ() * minInOut, facing.getOffsetZ() * maxInOut) + 0.5D : 0.5D + radius
		);
	}

	public LedBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.FACING, Direction.DOWN)
			.with(Properties.LIT, Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction facing = state.get(Properties.FACING);
		BlockPos adjacentPos = pos.offset(facing);
		return world.getBlockState(adjacentPos).isSideSolid(world, adjacentPos, facing.getOpposite(), SideShapeType.FULL);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(Properties.FACING, context.getSide().getOpposite())
			.with(Properties.LIT, context.getWorld().isReceivingRedstonePower(context.getBlockPos()))
			.with(Properties.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).isEqualAndStill(Fluids.WATER))
		);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		Direction facing = state.get(Properties.FACING);
		return (
			direction != facing
			|| neighborState.isSideSolid(
				world,
				neighborPos,
				facing.getOpposite(),
				SideShapeType.FULL
			)
			? state
			: Blocks.AIR.getDefaultState()
		);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean moved) {
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, moved);
		boolean
			lit = state.get(Properties.LIT),
			shouldBeLit = world.isReceivingRedstonePower(pos);
		if (lit != shouldBeLit) {
			world.setBlockState(pos, state.with(Properties.LIT, shouldBeLit));
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(Properties.FACING));
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
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING, Properties.LIT, Properties.WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
	}
}