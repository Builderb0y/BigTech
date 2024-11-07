package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class MagnetiteBlockDataGenerator extends BasicBlockDataGenerator {

	public MagnetiteBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_bottom_top"))
			.blockTexture("top",    context.suffixPath(this.getId(), "_top"   ))
			.blockTexture("bottom", context.suffixPath(this.getId(), "_bottom"))
			.blockTexture("side",   context.suffixPath(this.getId(), "_side"  ))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.STONE).addElement(this.getId());
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.MAGNETITE_BLOCKS).addElement(this.getId());
		context.getTags(BlockTags.BEACON_BASE_BLOCKS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.IRON_BLOCKS).add(Items.IRON_BLOCK);
		context.getTags(BigTechItemTags.MAGNETITE_BLOCKS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_ingots")),
			new ShapedRecipeBuilder()
			.pattern("iii", "iii", "iii")
			.where('i', BigTechItemTags.MAGNETITE_INGOTS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_transmute")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input": "#c:storage_blocks/iron",
				"output": { "item": "bigtech:magnetite_block" },
				"energy": 810
			}"""
		);
	}
}