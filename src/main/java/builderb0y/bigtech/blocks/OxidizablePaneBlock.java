package builderb0y.bigtech.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.PaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class OxidizablePaneBlock extends PaneBlock implements Oxidizable {

	public final OxidationLevel oxidationLevel;

	public OxidizablePaneBlock(Settings settings, OxidationLevel oxidationLevel) {
		super(settings);
		this.oxidationLevel = oxidationLevel;
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return Oxidizable.getIncreasedOxidationBlock(this).isPresent;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
	}

	@Override
	public OxidationLevel getDegradationLevel() {
		return this.oxidationLevel;
	}
}