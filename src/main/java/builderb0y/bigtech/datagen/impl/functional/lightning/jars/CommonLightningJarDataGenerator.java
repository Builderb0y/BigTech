package builderb0y.bigtech.datagen.impl.functional.lightning.jars;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningLevelTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CommonLightningJarDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.lang.put("bigtech.lightning_jar.stored", "Contained energy: %s / %s (%s%%)");
		context.lang.put("bigtech.lightning_battery.stored", "Contained energy: %s / %s (%s%%)");
		context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.LIGHTNING_JARS);
		context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.LIGHTNING_JARS);
	}
}