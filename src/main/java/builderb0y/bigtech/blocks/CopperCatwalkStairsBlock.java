package builderb0y.bigtech.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class CopperCatwalkStairsBlock extends CatwalkStairsBlock {

	public CopperCatwalkStairsBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState handleMismatchedNeighbor(WorldAccess world, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction direction) {
		if (BigTechBlocks.COPPER_CATWALK_STAIRS.contains(neighborState.getBlock())) {
			return this.transferState(neighborState.getBlock().getStateWithProperties(state), neighborState);
		}
		return super.handleMismatchedNeighbor(world, pos, state, neighborPos, neighborState, direction);
	}
}