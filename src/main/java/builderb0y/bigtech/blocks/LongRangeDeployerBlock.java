package builderb0y.bigtech.blocks;

import java.util.UUID;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.impl.DeployerBeam;
import builderb0y.bigtech.blockEntities.LongRangeDeployerBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.WorldHelper;

public class LongRangeDeployerBlock extends AbstractDeployerBlock {

	public static final MapCodec<LongRangeDeployerBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	public LongRangeDeployerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends Block> getCodec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new LongRangeDeployerBlockEntity(pos, state);
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		if (world instanceof ServerWorld serverWorld && state.get(Properties.POWERED)) {
			LongRangeDeployerBlockEntity deployer = WorldHelper.getBlockEntity(world, pos, LongRangeDeployerBlockEntity.class);
			if (deployer != null) {
				DeployerBeam beam = new DeployerBeam(serverWorld, UUID.randomUUID());
				beam.everywhere = deployer.everywhere.get() != 0;
				beam.fire(serverWorld, pos, BeamDirection.from(state.get(Properties.HORIZONTAL_FACING)), 15.0D);
			}
		}
		return false;
	}
}