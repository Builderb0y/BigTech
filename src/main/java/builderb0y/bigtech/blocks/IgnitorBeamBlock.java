package builderb0y.bigtech.blocks;

import java.util.UUID;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.IgnitorBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.IgnitorBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.Directions;
import builderb0y.bigtech.util.WorldHelper;

public class IgnitorBeamBlock extends BeamBlock implements BlockEntityProvider {

	public static final MapCodec<IgnitorBeamBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public IgnitorBeamBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWERED, Boolean.FALSE)
			.with(Properties.LIT, Boolean.FALSE)
		);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new IgnitorBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : IgnitorBlockEntity.SERVER_TICKER.as();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
			if (factory != null) player.openHandledScreen(factory);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return world.getBlockEntity(pos) instanceof IgnitorBlockEntity blockEntity ? blockEntity : null;
	}

	public boolean shouldBePowered(RedstoneView world, BlockPos pos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if (world.getEmittedRedstonePower(mutable.set(pos, Direction.DOWN), Direction.DOWN) > 0) return true;
		for (Direction direction : Directions.HORIZONTAL) {
			if (world.getEmittedRedstonePower(mutable.set(pos, direction), direction) > 0) return true;
		}
		return false;
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		PersistentBeam oldBeam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
		if (oldBeam != null) oldBeam.removeFromWorld();
		if (state.get(Properties.POWERED) && state.get(Properties.LIT)) {
			PersistentBeam newBeam = new IgnitorBeam(world, UUID.randomUUID());
			newBeam.fire(pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		}
		return false;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = this.shouldBePowered(world, pos);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered), Block.NOTIFY_ALL);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean moved) {
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, moved);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = this.shouldBePowered(world, pos);
		if (powered != shouldBePowered) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onBlockAdded(state, world, pos, oldState, moved);
		if (state.get(Properties.POWERED) && state.get(Properties.LIT)) {
			PersistentBeam beam = new IgnitorBeam(world, UUID.randomUUID());
			beam.fire(pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!BigTechBlockEntityTypes.IGNITOR.supports(newState)) {
			IgnitorBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, IgnitorBlockEntity.class);
			if (blockEntity != null) ItemScatterer.spawn(world, pos, blockEntity);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
		if (state.get(Properties.POWERED)) {
			PersistentBeam beam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
			if (beam != null) beam.removeFromWorld();
		}
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(Properties.POWERED, this.shouldBePowered(context.getWorld(), context.getBlockPos()))
		);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED, Properties.LIT);
	}
}