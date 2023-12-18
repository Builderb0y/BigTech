package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.Item;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;

public class MinerToolDataGenerator extends BasicItemDataGenerator {

	public MinerToolDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {

	}

	@Override
	public void run(DataGenContext context) {
		super.run(context);
		context.getTags(BigTechBlockTags.MINER_BREAKABLE).add(MiningToolTags.PICKAXE);
		context.getTags(BigTechBlockTags.MINER_BREAKABLE).add(MiningToolTags.SHOVEL);
	}
}