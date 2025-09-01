package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.beams.impl.DestructionManager;
import builderb0y.bigtech.blockEntities.AbstractDestroyerBlockEntity;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.ShortRangeDestroyerBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.WorldHelper;

public class ShortRangeDestroyerBlock extends AbstractDestroyerBlock {

	public static final MapCodec<ShortRangeDestroyerBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public ShortRangeDestroyerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state,
		WorldView world,
		ScheduledTickView tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		Random random
	) {
		if (direction == state.get(Properties.HORIZONTAL_FACING)) {
			if (world.getBlockEntity(pos) instanceof ShortRangeDestroyerBlockEntity blockEntity && blockEntity.queue != null) {
				if (world instanceof ServerWorld serverWorld && blockEntity.queue.populated && !blockEntity.queue.inactive.isEmpty()) {
					DestructionManager.forWorld(serverWorld).resetProgress(blockEntity.queue.inactive.lastKey());
				}
				blockEntity.queue = null;
			}
		}
		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
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