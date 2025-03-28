package builderb0y.bigtech.blocks.belts;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.api.AscenderInteractor;
import builderb0y.bigtech.blocks.BigTechBlockTags;

public abstract class AbstractBeltBlock extends Block implements Waterloggable, AscenderInteractor {

	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

	public AbstractBeltBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	public abstract void move(World world, BlockPos pos, BlockState state, Entity entity);

	public boolean canMove(World world, BlockPos pos, BlockState state, Entity entity) {
		return (
			entity.isAlive() &&
			!entity.isSpectator() &&
			!entity.isSneaking() &&
			!(entity instanceof PlayerEntity player && player.getAbilities().flying)
		);
	}

	public boolean isOnBelt(World world, BlockPos pos, BlockState state, Entity entity) {
		return (
			MathHelper.floor(entity.getPos().x) == pos.getX() &&
			MathHelper.floor(entity.getPos().z) == pos.getZ() &&
			MathHelper.approximatelyEquals(entity.getPos().y, pos.getY() + 0.0625D)
		);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		super.onEntityCollision(state, world, pos, entity, handler);
		if (this.canMove(world, pos, state, entity) && this.isOnBelt(world, pos, state, entity)) {
			this.move(world, pos, state, entity);
		}
		BlockPos downPos = pos.down();
		BlockState downState = world.getBlockState(downPos);
		if (!(downState.getBlock() instanceof AbstractBeltBlock)) {
			downState.onEntityCollision(world, downPos, entity, handler);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
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
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos downPos = pos.down();
		BlockState downState = world.getBlockState(downPos);
		return downState.isSideSolid(world, downPos, Direction.UP, SideShapeType.RIGID) || downState.isIn(BigTechBlockTags.BELT_SUPPORT);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).getFluid() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.WATERLOGGED);
	}
}