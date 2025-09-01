package builderb0y.bigtech.blocks;

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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.BeamUtil;
import builderb0y.bigtech.beams.base.*;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class RedstoneReceiverBlock extends BeamBlock implements BeamCallback {

	public static final MapCodec<RedstoneReceiverBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public RedstoneReceiverBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWERED, Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	public boolean spreadOut(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment inputSegment) {
		if (inputSegment.segment().direction() == BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)).getOpposite()) {
			inputSegment.beam().addSegment(world, inputSegment.terminate());
			return true;
		}
		return false;
	}

	@Override
	public void onBeamAdded(ServerWorld world, BlockPos pos, BlockState state, PersistentBeam beam) {
		if (this.shouldBePoweredBy(beam, pos, state)) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public void onBeamRemoved(ServerWorld world, BlockPos pos, BlockState state, PersistentBeam beam) {
		if (state.get(Properties.POWERED)) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public void onBeamPulse(ServerWorld world, BlockPos pos, BlockState state, PulseBeam beam) {
		if (this.shouldBePoweredBy(beam, pos, state)) {
			world.addSyncedBlockEvent(pos, this, 0, 0);
		}
	}

	public boolean shouldBePowered(World world, BlockPos pos, BlockState state) {
		Direction direction = state.get(Properties.HORIZONTAL_FACING);
		return BeamUtil.hasSegmentLeadingInto(world, pos, BeamDirection.from(direction), BeamUtil.VISIBLE);
	}

	public boolean shouldBePoweredBy(Beam beam, BlockPos pos, BlockState state) {
		Direction direction = state.get(Properties.HORIZONTAL_FACING);
		return BeamUtil.hasSegmentLeadingInto(beam, pos, BeamDirection.from(direction), BeamUtil.VISIBLE);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = this.shouldBePowered(world, pos, state);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered));
		}
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		if (!state.get(Properties.POWERED)) {
			world.setBlockState(pos, state.with(Properties.POWERED, Boolean.TRUE));
			world.scheduleBlockTick(pos, this, 2);
		}
		return false;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(Properties.POWERED) ? 15 : 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return direction == Direction.UP && state.get(Properties.POWERED) ? 15 : 0;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.down(), this);
		}
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		super.onStateReplaced(state, world, pos, moved);
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.down(), this);
		}
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		return state.with(Properties.POWERED, this.shouldBePowered(context.getWorld(), context.getBlockPos(), state));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}
}