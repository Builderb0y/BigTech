package builderb0y.bigtech.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface LegacyOnStateReplaced {

	public abstract void legacyOnStateReplaced(ServerWorld world, BlockPos pos, BlockState state, BlockState newState, boolean moved);
}