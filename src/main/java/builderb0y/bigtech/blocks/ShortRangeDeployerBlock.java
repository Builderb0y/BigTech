package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.AbstractDeployerBlockEntity;
import builderb0y.bigtech.blockEntities.ShortRangeDeployerBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.WorldHelper;

public class ShortRangeDeployerBlock extends AbstractDeployerBlock {

	public static final MapCodec<ShortRangeDeployerBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	public ShortRangeDeployerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends Block> getCodec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ShortRangeDeployerBlockEntity(pos, state);
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		AbstractDeployerBlockEntity deployer = WorldHelper.getBlockEntity(world, pos, AbstractDeployerBlockEntity.class);
		if (deployer != null) {
			Direction facing = state.get(Properties.HORIZONTAL_FACING);
			deployer.deploy(pos.offset(facing), facing);
		}
		return false;
	}
}