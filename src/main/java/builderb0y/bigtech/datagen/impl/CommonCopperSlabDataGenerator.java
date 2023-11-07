package builderb0y.bigtech.datagen.impl;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningLevelTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CommonCopperSlabDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.COPPER_SLABS);
		context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.COPPER_SLABS);
	}
}