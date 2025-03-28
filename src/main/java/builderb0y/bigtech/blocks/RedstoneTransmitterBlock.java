package builderb0y.bigtech.blocks;

import java.util.UUID;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.RedstoneBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class RedstoneTransmitterBlock extends BeamBlock implements LegacyOnStateReplaced {

	public static final MapCodec<RedstoneTransmitterBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public RedstoneTransmitterBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWERED, Boolean.FALSE)
		);
	}

	public boolean shouldBePowered(RedstoneView world, BlockPos pos) {
		return world.isReceivingRedstonePower(pos);
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		if (world instanceof ServerWorld serverWorld) {
			PersistentBeam oldBeam = CommonWorldBeamStorage.KEY.get(serverWorld).getBeam(pos);
			if (oldBeam != null) oldBeam.removeFromWorld(serverWorld);
			if (state.get(Properties.POWERED)) {
				PersistentBeam newBeam = new RedstoneBeam(serverWorld, UUID.randomUUID());
				newBeam.fire(serverWorld, pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
			}
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
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = this.shouldBePowered(world, pos);
		if (powered != shouldBePowered) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onBlockAdded(state, world, pos, oldState, moved);
		if (world instanceof ServerWorld serverWorld) {
			Direction direction = this.getFiringDirection(state);
			if (direction != null && direction != this.getFiringDirection(oldState)) {
				PersistentBeam beam = new RedstoneBeam(world, UUID.randomUUID());
				beam.fire(serverWorld, pos, BeamDirection.from(direction), 15.0D);
			}
		}
	}

	@Override
	public void legacyOnStateReplaced(ServerWorld world, BlockPos pos, BlockState state, BlockState newState, boolean moved) {
		Direction direction = this.getFiringDirection(state);
		if (direction != null && direction != this.getFiringDirection(newState)) {
			PersistentBeam beam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
			if (beam != null) beam.removeFromWorld(world);
		}
	}

	public @Nullable Direction getFiringDirection(BlockState state) {
		return state.isOf(this) && state.get(Properties.POWERED) ? state.get(Properties.HORIZONTAL_FACING) : null;
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
		builder.add(Properties.POWERED);
	}
}