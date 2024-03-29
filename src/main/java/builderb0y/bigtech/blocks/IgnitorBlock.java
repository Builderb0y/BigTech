package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.IgnitorBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public class IgnitorBlock extends Block implements BlockEntityProvider {

	/**
	entity collision logic skips the ground, so we need to slightly
	contract the bounding box for entities to be able to collide properly.
	*/
	public static final VoxelShape COLLISION_SHAPE = VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.999D, 1.0D);

	public IgnitorBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.LIT, Boolean.FALSE);
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
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		IgnitorBlockEntity ignitor = WorldHelper.getBlockEntity(world, pos, IgnitorBlockEntity.class);
		if (ignitor != null) ignitor.onEntityCollision(entity);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
			if (factory != null) player.openHandledScreen(factory);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return world.getBlockEntity(pos) instanceof IgnitorBlockEntity blockEntity ? blockEntity : null;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!BigTechBlockEntityTypes.IGNITOR.supports(newState)) {
			IgnitorBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, IgnitorBlockEntity.class);
			if (blockEntity != null) ItemScatterer.spawn(world, pos, blockEntity);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.horizontalPlayerFacing.opposite);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new IgnitorBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : IgnitorBlockEntity.SERVER_TICKER.as();
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
		builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
	}
}