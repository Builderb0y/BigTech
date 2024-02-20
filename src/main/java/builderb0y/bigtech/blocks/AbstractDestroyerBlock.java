package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.AbstractDestroyerBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public abstract class AbstractDestroyerBlock extends Block implements BlockEntityProvider {

	public AbstractDestroyerBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.POWERED, Boolean.FALSE);
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
		return world.getBlockEntity(pos) instanceof NamedScreenHandlerFactory factory ? factory : null;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean moved) {
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, moved);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = world.isReceivingRedstonePower(pos);
		if (powered != shouldBePowered) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = world.isReceivingRedstonePower(pos);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered), Block.NOTIFY_ALL);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!newState.isOf(this)) {
			AbstractDestroyerBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, AbstractDestroyerBlockEntity.class);
			if (blockEntity != null) ItemScatterer.spawn(world, pos, blockEntity);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(Properties.HORIZONTAL_FACING, context.horizontalPlayerFacing.opposite)
			.with(Properties.POWERED, context.world.isReceivingRedstonePower(context.blockPos))
		);
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
		builder.add(Properties.HORIZONTAL_FACING, Properties.POWERED);
	}
}