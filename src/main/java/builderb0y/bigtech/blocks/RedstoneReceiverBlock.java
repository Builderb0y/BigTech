package builderb0y.bigtech.blocks;

import java.util.LinkedList;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.base.*;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;

public class RedstoneReceiverBlock extends Block implements BeamCallback, Waterloggable {

	public RedstoneReceiverBlock(Settings settings) {
		super(settings);
		this.defaultState = (
			this.defaultState
			.with(Properties.POWERED, Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	public boolean spreadOut(BlockPos pos, BlockState state, BeamSegment inputSegment) {
		if (inputSegment.direction == BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)).opposite) {
			inputSegment.beam.addSegment(pos, inputSegment.terminate());
			return true;
		}
		return false;
	}

	@Override
	public void onBeamAdded(BlockPos pos, BlockState state, PersistentBeam beam) {
		if (this.shouldBePoweredBy(beam, pos, state)) {
			beam.world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public void onBeamRemoved(BlockPos pos, BlockState state, PersistentBeam beam) {
		if (state.get(Properties.POWERED)) {
			beam.world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public void onBeamPulse(BlockPos pos, BlockState state, PulseBeam beam) {
		if (this.shouldBePoweredBy(beam, pos, state)) {
			beam.world.addSyncedBlockEvent(pos, this, 0, 0);
		}
	}

	public boolean shouldBePowered(World world, BlockPos pos, BlockState state) {
		Direction direction = state.get(Properties.HORIZONTAL_FACING);
		BlockPos query = pos.offset(direction);
		CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(world.getChunk(query)).require().get(query.y >> 4);
		if (sectionStorage != null) {
			LinkedList<BeamSegment> segments = sectionStorage.checkSegments(query);
			if (segments != null) {
				BeamDirection beamDirection = BeamDirection.from(direction.opposite);
				for (BeamSegment segment : segments) {
					if (segment.visible && segment.direction == beamDirection) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean shouldBePoweredBy(Beam beam, BlockPos pos, BlockState state) {
		Direction direction = state.get(Properties.HORIZONTAL_FACING);
		BlockPos query = pos.offset(direction);
		BasicSectionBeamStorage sectionStorage = beam.seen.get(ChunkSectionPos.toLong(query));
		if (sectionStorage != null) {
			LinkedList<BeamSegment> segments = sectionStorage.checkSegments(query);
			if (segments != null) {
				BeamDirection beamDirection = BeamDirection.from(direction.opposite);
				for (BeamSegment segment : segments) {
					if (segment.visible && segment.direction == beamDirection) {
						return true;
					}
				}
			}
		}
		return false;
	}


	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = this.shouldBePowered(world, pos, state);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered));
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		if (!state.get(Properties.POWERED)) {
			world.setBlockState(pos, state.with(Properties.POWERED, Boolean.TRUE));
			world.scheduleBlockTick(pos, this, 2);
		}
		return false;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return direction.horizontal >= 0 && state.get(Properties.POWERED) ? 15 : 0;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return direction == Direction.UP && state.get(Properties.POWERED) ? 15 : 0;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.down(), this);
		}
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.down(), this);
		}
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super.getPlacementState(context)
			.with(Properties.HORIZONTAL_FACING, context.horizontalPlayerFacing.opposite)
			.with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).isEqualAndStill(Fluids.WATER))
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(Properties.HORIZONTAL_FACING)) {
			case NORTH, SOUTH -> RedstoneTransmitterBlock.NORTH_SOUTH_SHAPE;
			case EAST, WEST -> RedstoneTransmitterBlock.EAST_WEST_SHAPE;
			case UP, DOWN -> throw new AssertionError("vertical redstone receiver?");
		};
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
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).defaultState;
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
		builder.add(Properties.HORIZONTAL_FACING, Properties.POWERED, Properties.WATERLOGGED);
	}
}