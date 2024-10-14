package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.beams.impl.DestroyerBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public class LongRangeDestroyerBlockEntity extends AbstractDestroyerBlockEntity {

	public LongRangeDestroyerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public LongRangeDestroyerBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.LONG_RANGE_DESTROYER, pos, state);
	}

	@Override
	public void doTick() {
		if (CommonWorldBeamStorage.KEY.get(this.getWorld()).getBeam(this.getPos()) instanceof DestroyerBeam destroyerBeam) {
			if (destroyerBeam.serverTick(this.getStack(0))) {
				this.markDirty();
			}
		}
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.long_range_destroyer");
	}
}