package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class CopperCatwalkStairsBlock extends CatwalkStairsBlock {

	public static final MapCodec<CopperCatwalkStairsBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public CopperCatwalkStairsBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState handleMismatchedNeighbor(WorldView world, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction direction) {
		if (DecoBlocks.COPPER_CATWALK_STAIRS.contains(neighborState.getBlock())) {
			return this.transferState(neighborState.getBlock().getStateWithProperties(state), neighborState);
		}
		return super.handleMismatchedNeighbor(world, pos, state, neighborPos, neighborState, direction);
	}
}