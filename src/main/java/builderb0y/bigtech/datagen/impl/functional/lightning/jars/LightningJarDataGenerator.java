package builderb0y.bigtech.datagen.impl.functional.lightning.jars;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.items.BigTechItemTags;

@Dependencies(CommonLightningJarDataGenerator.class)
public abstract class LightningJarDataGenerator extends BasicBlockDataGenerator {

	public LightningJarDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeLootTableJson(DataGenContext context) {
		this.writeBlockEntityComponentCopyingLootTableJson(context, BigTechDataComponents.LIGHTNING_ENERGY);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//handled by dependency.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//handled by dependency.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.LIGHTNING_JARS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.LIGHTNING_JARS).addElement(this.getId());
	}
}