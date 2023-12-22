package builderb0y.bigtech.blocks;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.TripwireBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.TripwireBlockEntity;

public class TripwireBlock extends BeamBlock implements BlockEntityProvider {

	public TripwireBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.POWERED, Boolean.FALSE).with(Properties.WATERLOGGED, Boolean.FALSE);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		PersistentBeam oldBeam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
		if (oldBeam != null) oldBeam.removeFromWorld();
		PersistentBeam newBeam = new TripwireBeam(world, UUID.randomUUID());
		newBeam.fire(pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		return false;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onBlockAdded(state, world, pos, oldState, moved);
		PersistentBeam beam = new TripwireBeam(world, UUID.randomUUID());
		beam.fire(pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.down(), this);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		PersistentBeam beam = CommonWorldBeamStorage.KEY.get(world).getBeam(pos);
		if (beam != null) beam.removeFromWorld();
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.down(), this);
		}
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

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TripwireBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BigTechBlockEntityTypes.TRIPWIRE ? TripwireBlockEntity.SERVER_TICKER.as() : null;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}
}