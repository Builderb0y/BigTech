package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.api.PistonInteractor;
import builderb0y.bigtech.mixins.EntityShapeContext_HeldItemGetter;

public class FrameBlock extends Block implements PistonInteractor, Waterloggable {

	public static final VoxelShape COLLISION_SHAPE = VoxelShapes.combineAndSimplify(
		VoxelShapes.fullCube(),
		VoxelShapes.union(
			VoxelShapes.cuboid(0.0D, 0.1875D, 0.1875D, 1.0D, 0.8125D, 0.8125D),
			VoxelShapes.cuboid(0.1875D, 0.0D, 0.1875D, 0.8125D, 1.0D, 0.8125D),
			VoxelShapes.cuboid(0.1875D, 0.1875D, 0.0D, 0.8125D, 0.8125D, 1.0D)
		),
		BooleanBiFunction.ONLY_FIRST
	);

	public final TagKey<Block> sticksTo;

	public FrameBlock(Settings settings, TagKey<Block> sticksTo) {
		super(settings);
		this.sticksTo = sticksTo;
		this.defaultState = this.defaultState.with(Properties.WATERLOGGED, Boolean.FALSE);
		PistonInteractor.LOOKUP.registerForBlocks((world, pos, state, blockEntity, context) -> this, this);
	}

	@Override
	public boolean isSticky(PistonHandlerInfo handler, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canStickTo(
		PistonHandlerInfo handler,
		BlockPos selfPos,
		BlockState selfState,
		BlockPos otherPos,
		BlockState otherState,
		Direction face
	) {
		return otherState.isIn(this.sticksTo);
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
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext_HeldItemGetter heldItemGetter) {
			ItemStack heldItem = heldItemGetter.bigtech_getHeldItem();
			if (heldItem.item instanceof BlockItem) {
				return VoxelShapes.fullCube();
			}
			if (heldItem.isSuitableFor(state)) {
				return VoxelShapes.fullCube();
			}
		}
		return COLLISION_SHAPE;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return COLLISION_SHAPE;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).defaultState;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.WATERLOGGED);
	}
}