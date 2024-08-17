package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.PaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class OxidizablePaneBlock extends PaneBlock implements Oxidizable {

	public static final MapCodec<OxidizablePaneBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public final OxidationLevel oxidation_level;

	public OxidizablePaneBlock(Settings settings, OxidationLevel oxidation_level) {
		super(settings);
		this.oxidation_level = oxidation_level;
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return Oxidizable.getIncreasedOxidationBlock(this).isPresent();
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
	}

	@Override
	public OxidationLevel getDegradationLevel() {
		return this.oxidation_level;
	}
}