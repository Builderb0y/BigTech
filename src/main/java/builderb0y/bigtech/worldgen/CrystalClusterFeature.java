package builderb0y.bigtech.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import builderb0y.autocodec.annotations.VerifyFloatRange;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class CrystalClusterFeature extends Feature<CrystalClusterFeature.Config> {

	public CrystalClusterFeature(Codec<Config> configCodec) {
		super(configCodec);
	}

	public CrystalClusterFeature() {
		this(BigTechAutoCodec.AUTO_CODEC.createDFUCodec(Config.class));
	}

	@Override
	public boolean generate(FeatureContext<Config> context) {
		if (context.getConfig().place().size() == 0) return false;
		Chunk chunk = context.getWorld().getChunk(context.getOrigin());
		BlockPos.Mutable pos = new BlockPos.Mutable();
		for (int baseY = chunk.minY(), maxY = chunk.maxY(); baseY < maxY; baseY += 16) {
			for (int attempt = context.getConfig().perSection(context.getRandom()); --attempt >= 0;) {
				int rng = context.getRandom().nextInt();
				int x = chunk.getPos().getStartX() | (rng & 15);
				int y = baseY | ((rng >>> 4) & 15);
				int z = chunk.getPos().getStartZ() | ((rng >>> 8) & 15);
				pos.set(x, y, z);
				if (chunk.getBlockState(pos).isReplaceable() && chunk.getBlockState(pos.setY(y + 1)).isIn(context.getConfig().under())) {
					ImmutableList<BlockState> states = context.getConfig().place().getRandom(context.getRandom()).orElseThrow().value().getStateManager().getStates();
					BlockState state = states.get(context.getRandom().nextInt(states.size()));
					if (state.contains(Properties.WATERLOGGED)) {
						state = state.with(Properties.WATERLOGGED, context.getWorld().getFluidState(pos.setY(y)).isEqualAndStill(Fluids.WATER));
					}
					context.getWorld().setBlockState(pos.setY(y), state, Block.NOTIFY_ALL);
				}
			}
		}
		return true;
	}

	public static record Config(
		RegistryEntryList<Block> place,
		RegistryEntryList<Block> under,
		@VerifyFloatRange(min = 0.0D, minInclusive = false, max = 4096) double per_section
	)
	implements FeatureConfig {

		public int perSection(Random random) {
			return (int)(this.per_section + random.nextDouble());
		}
	}
}