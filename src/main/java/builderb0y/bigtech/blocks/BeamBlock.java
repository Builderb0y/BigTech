package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class BeamBlock extends Block implements Waterloggable {

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

	public static final MapCodec<BeamBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public BeamBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(Properties.HORIZONTAL_FACING)) {
			case NORTH, SOUTH -> NORTH_SOUTH_SHAPE;
			case EAST, WEST -> EAST_WEST_SHAPE;
			case UP, DOWN -> throw new AssertionError("Vertical ${this.getClass().getSimpleName()}?");
		};
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
			.with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing().getOpposite())
			.with(Properties.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).isEqualAndStill(Fluids.WATER))
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
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
		builder.add(Properties.HORIZONTAL_FACING, Properties.WATERLOGGED);
	}
}