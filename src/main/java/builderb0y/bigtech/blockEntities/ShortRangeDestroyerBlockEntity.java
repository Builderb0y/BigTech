package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.beams.impl.DestructionManager.DestroyQueue;

public class ShortRangeDestroyerBlockEntity extends AbstractDestroyerBlockEntity {

	public DestroyQueue queue;

	public ShortRangeDestroyerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
		super(blockEntityType, pos, blockState);
	}

	public ShortRangeDestroyerBlockEntity(BlockPos pos, BlockState state) {
		super(BigTechBlockEntityTypes.SHORT_RANGE_DESTROYER, pos, state);
	}

	@Override
	public void doTickServer() {
		if (this.queue == null) {
			this.queue = new DestroyQueue(this.getWorld().as(), this.getPos().offset(this.getCachedState().get(Properties.HORIZONTAL_FACING)), 16.0D);
		}
		this.queue.populate();
		if (this.queue.inactive.isEmpty()) return;
		if (this.queue.tick(this.getStack(0))) {
			this.markDirty();
		}
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.short_range_destroyer");
	}
}