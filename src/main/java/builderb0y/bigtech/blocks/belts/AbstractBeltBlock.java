package builderb0y.bigtech.blocks.belts;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import builderb0y.bigtech.blocks.BigTechBlockTags;

public abstract class AbstractBeltBlock extends Block implements Waterloggable {

	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

	public AbstractBeltBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(WATERLOGGED, Boolean.FALSE);
	}

	public abstract void move(World world, BlockPos pos, BlockState state, Entity entity);

	public boolean canMove(World world, BlockPos pos, BlockState state, Entity entity) {
		return (
			entity.isAlive &&
			!entity.isSpectator &&
			!entity.isSneaking &&
			!(entity instanceof PlayerEntity player && player.abilities.flying)
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (
			this.canMove(world, pos, state, entity) &&
			MathHelper.floor(entity.pos.x) == pos.x &&
			MathHelper.floor(entity.pos.z) == pos.z &&
			MathHelper.approximatelyEquals(entity.pos.y, pos.y + 0.0625D)
		) {
			this.move(world, pos, state, entity);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (
			direction == Direction.DOWN && !(
				neighborState.isSideSolid(world, neighborPos, Direction.UP, SideShapeType.RIGID) ||
				neighborState.isIn(BigTechBlockTags.BELT_SUPPORT)
			)
		) {
			return Blocks.AIR.getDefaultState();
		}
		else {
			return state;
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos downPos = pos.down();
		BlockState downState = world.getBlockState(downPos);
		return downState.isSideSolid(world, downPos, Direction.UP, SideShapeType.RIGID) || downState.isIn(BigTechBlockTags.BELT_SUPPORT);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return context.getStack().isOf(this.asItem());
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(WATERLOGGED);
	}
}