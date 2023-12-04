package builderb0y.bigtech.blocks;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.DestroyerBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.DestroyerBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public class DestroyerBlock extends Block implements BlockEntityProvider {

	public DestroyerBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.POWERED, Boolean.FALSE);
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
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		PersistentBeam oldBeam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
		if (oldBeam != null) oldBeam.removeFromWorld();
		if (state.get(Properties.POWERED)) {
			PersistentBeam newBeam = new DestroyerBeam(world, UUID.randomUUID());
			newBeam.fire(pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		}
		return false;
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
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onBlockAdded(state, world, pos, oldState, moved);
		if (state.get(Properties.POWERED)) {
			PersistentBeam beam = new DestroyerBeam(world, UUID.randomUUID());
			beam.fire(pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!newState.isOf(this)) {
			DestroyerBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, DestroyerBlockEntity.class);
			if (blockEntity != null) ItemScatterer.spawn(world, pos, blockEntity);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
		if (state.get(Properties.POWERED)) {
			PersistentBeam beam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
			if (beam != null) beam.removeFromWorld();
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.horizontalPlayerFacing.opposite);
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

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DestroyerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BigTechBlockEntityTypes.DESTROYER && !world.isClient ? DestroyerBlockEntity.SERVER_TICKER.as() : null;
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