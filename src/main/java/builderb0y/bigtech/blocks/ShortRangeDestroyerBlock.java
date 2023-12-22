package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.beams.impl.DestructionManager;
import builderb0y.bigtech.blockEntities.AbstractDestroyerBlockEntity;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.ShortRangeDestroyerBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public class ShortRangeDestroyerBlock extends AbstractDestroyerBlock {

	public ShortRangeDestroyerBlock(Settings settings) {
		super(settings);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction == state.get(Properties.HORIZONTAL_FACING)) {
			ShortRangeDestroyerBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, ShortRangeDestroyerBlockEntity.class);
			if (blockEntity != null && blockEntity.queue != null) {
				if (world instanceof ServerWorld serverWorld && blockEntity.queue.populated && !blockEntity.queue.inactive.isEmpty) {
					DestructionManager.forWorld(serverWorld).resetProgress(blockEntity.queue.inactive.lastKey());
				}
				blockEntity.queue = null;
			}
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.get(Properties.POWERED)) {
			ShortRangeDestroyerBlockEntity destroyer = WorldHelper.getBlockEntity(world, pos, ShortRangeDestroyerBlockEntity.class);
			if (destroyer != null) {
				if (destroyer.queue != null && destroyer.queue.populated && !destroyer.queue.inactive.isEmpty) {
					DestructionManager.forWorld(world).resetProgress(destroyer.queue.inactive.lastKey());
				}
				if (!newState.isOf(this)) destroyer.queue = null;
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ShortRangeDestroyerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BigTechBlockEntityTypes.SHORT_RANGE_DESTROYER && !world.isClient ? AbstractDestroyerBlockEntity.SERVER_TICKER.as() : null;
	}
}