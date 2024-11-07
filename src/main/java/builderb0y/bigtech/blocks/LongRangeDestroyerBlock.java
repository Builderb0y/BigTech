package builderb0y.bigtech.blocks;

import java.util.UUID;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.DestroyerBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.blockEntities.AbstractDestroyerBlockEntity;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.LongRangeDestroyerBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class LongRangeDestroyerBlock extends AbstractDestroyerBlock {

	public static final MapCodec<LongRangeDestroyerBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public LongRangeDestroyerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		if (world instanceof ServerWorld serverWorld) {
			PersistentBeam oldBeam = CommonWorldBeamStorage.KEY.get(serverWorld).getBeam(pos);
			if (oldBeam != null) oldBeam.removeFromWorld(serverWorld);
			if (state.get(Properties.POWERED)) {
				PersistentBeam newBeam = new DestroyerBeam(serverWorld, UUID.randomUUID());
				newBeam.fire(serverWorld, pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
			}
		}
		return false;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onBlockAdded(state, world, pos, oldState, moved);
		if (world instanceof ServerWorld serverWorld && state.get(Properties.POWERED)) {
			PersistentBeam beam = new DestroyerBeam(serverWorld, UUID.randomUUID());
			beam.fire(serverWorld, pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (world instanceof ServerWorld serverWorld && state.get(Properties.POWERED)) {
			PersistentBeam beam = CommonWorldBeamStorage.KEY.get(serverWorld).getBeam(pos);
			if (beam != null) beam.removeFromWorld(serverWorld);
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new LongRangeDestroyerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BigTechBlockEntityTypes.LONG_RANGE_DESTROYER && !world.isClient ? AbstractDestroyerBlockEntity.SERVER_TICKER.as() : null;
	}
}