package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class LongRangeDeployerBlockEntity extends AbstractDeployerBlockEntity {

	public LongRangeDeployerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public LongRangeDeployerBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.LONG_RANGE_DEPLOYER, pos, state);
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.long_range_deployer");
	}
}