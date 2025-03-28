package builderb0y.bigtech.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.util.WorldHelper;

public abstract class AbstractDeployerBlock extends AbstractDestroyerBlock {

	public AbstractDeployerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (world instanceof ServerWorld serverWorld && state.get(Properties.POWERED)) {
			serverWorld.addSyncedBlockEvent(pos, this, 0, 0);
		}
		super.onBlockAdded(state, world, pos, oldState, notify);
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(WorldHelper.getBlockEntity(world, pos, Inventory.class));
	}
}