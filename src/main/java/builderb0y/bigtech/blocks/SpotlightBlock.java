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
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.SpotlightBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class SpotlightBlock extends Block {

	public static final MapCodec<SpotlightBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public SpotlightBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWERED, Boolean.FALSE)
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		PersistentBeam oldBeam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
		if (oldBeam != null) oldBeam.removeFromWorld();
		if (state.get(Properties.POWERED)) {
			PersistentBeam newBeam = new SpotlightBeam(world, UUID.randomUUID());
			newBeam.fire(pos, BeamDirection.from(state.get(Properties.FACING)), 31.0D);
		}
		return false;
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(Properties.FACING, context.getPlayerLookDirection().getOpposite())
			.with(Properties.POWERED, context.getWorld().isReceivingRedstonePower(context.getBlockPos()))
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = world.isReceivingRedstonePower(pos);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered));
		}
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
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onBlockAdded(state, world, pos, oldState, moved);
		if (state.get(Properties.POWERED)) {
			PersistentBeam beam = new SpotlightBeam(world, UUID.randomUUID());
			beam.fire(pos, BeamDirection.from(state.get(Properties.FACING)), 31.0D);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (state.get(Properties.POWERED)) {
			PersistentBeam beam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
			if (beam != null) beam.removeFromWorld();
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.FACING, mirror.apply(state.get(Properties.FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING, Properties.POWERED);
	}
}