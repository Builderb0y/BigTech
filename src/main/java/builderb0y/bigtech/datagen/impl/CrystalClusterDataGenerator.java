package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.registrableCollections.CrystalClusterRegistrableCollection.CrystalClusterColor;

public class CrystalClusterDataGenerator extends BasicBlockDataGenerator {

	public final CrystalClusterColor color;

	public CrystalClusterDataGenerator(BlockItem blockItem, CrystalClusterColor color) {
		super(blockItem);
		this.color = color;
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return "δβπψζλωΩ".charAt(this.color.ordinal()) + " Crystal Cluster";
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.CRYSTAL_CLUSTERS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.CRYSTAL_CLUSTERS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {

	}
}