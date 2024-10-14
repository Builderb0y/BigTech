package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ShortRangeDeployerBlockEntity extends AbstractDeployerBlockEntity {

	public ShortRangeDeployerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public ShortRangeDeployerBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.SHORT_RANGE_DEPLOYER, pos, state);
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.short_range_deployer");
	}
}