package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class OxidizableConductiveAnvilBlock extends ConductiveAnvilBlock implements Oxidizable {

	public static final MapCodec<OxidizableConductiveAnvilBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public final OxidationLevel oxidation_level;

	public OxidizableConductiveAnvilBlock(Settings settings, OxidationLevel oxidation_level) {
		super(settings);
		this.oxidation_level = oxidation_level;
	}

	@Override
	public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		BlockState newState = Oxidizable.getDecreasedOxidationState(state).orElse(null);
		if (newState != null) world.setBlockState(pos, state = newState);
		super.onPulse(world, pos, state, pulse);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return Oxidizable.getIncreasedOxidationBlock(this).isPresent();
	}

	@Override
	public OxidationLevel getDegradationLevel() {
		return this.oxidation_level;
	}
}