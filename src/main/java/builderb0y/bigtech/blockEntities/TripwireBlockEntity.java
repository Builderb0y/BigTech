package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TripwireBlockEntity extends BlockEntity {

	public static final BlockEntityTicker<TripwireBlockEntity> SERVER_TICKER = (World world, BlockPos pos, BlockState state, TripwireBlockEntity blockEntity) -> blockEntity.tickServer();

	public int delay;

	public TripwireBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public TripwireBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.TRIPWIRE, pos, state);
	}

	public void setDelay(int delay) {
		if (this.delay != delay) {
			this.delay = delay;
			boolean powered = delay > 0;
			if (this.cachedState.get(Properties.POWERED) != powered) {
				this.world.setBlockState(this.pos, this.cachedState.with(Properties.POWERED, powered));
			}
			this.markDirty();
		}
	}

	public void tickServer() {
		if (this.delay > 0) this.setDelay(this.delay - 1);
	}
}