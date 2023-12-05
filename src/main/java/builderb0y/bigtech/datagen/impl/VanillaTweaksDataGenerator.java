package builderb0y.bigtech.datagen.impl;

import net.minecraft.registry.tag.BlockTags;

import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class VanillaTweaksDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(MiningToolTags.AXE).add(BlockTags.LEAVES);
	}
}