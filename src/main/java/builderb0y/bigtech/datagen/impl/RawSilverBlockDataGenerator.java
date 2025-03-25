package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class RawSilverBlockDataGenerator extends BasicBlockDataGenerator {

	public RawSilverBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.IRON).addElement(this.getId());
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_all"))
			.blockTexture("all", this.getId())
			.toString()
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.RAW_SILVER_BLOCKS).addElement(this.getId());
		context.getTags(ConventionalBlockTags.STORAGE_BLOCKS).add(BigTechBlockTags.RAW_SILVER_BLOCKS);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.RAW_SILVER_BLOCKS).addElement(this.getId());
		context.getTags(ConventionalItemTags.STORAGE_BLOCKS).add(BigTechItemTags.RAW_SILVER_BLOCKS);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("sss", "sss", "sss")
			.where('s', BigTechItemTags.RAW_SILVER)
			.result(this.getId())
			.toString()
		);
	}
}