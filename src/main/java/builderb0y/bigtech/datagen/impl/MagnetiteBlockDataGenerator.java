package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.Blocks;
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
			context.blockModelPath(this.id),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "cube_bottom_top"))
			.blockTexture("top",    context.suffixPath(this.id, "_top"   ))
			.blockTexture("bottom", context.suffixPath(this.id, "_bottom"))
			.blockTexture("side",   context.suffixPath(this.id, "_side"  ))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.STONE).addElement(this.id);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.IRON_BLOCKS).add(Blocks.IRON_BLOCK);
		context.getTags(BigTechBlockTags.MAGNETITE_BLOCKS).addElement(this.id);
		context.getTags(BlockTags.BEACON_BASE_BLOCKS).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.IRON_BLOCKS).add(Items.IRON_BLOCK);
		context.getTags(BigTechItemTags.MAGNETITE_BLOCKS).addElement(this.id);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_ingots")),
			new ShapedRecipeBuilder()
			.pattern("iii", "iii", "iii")
			.where('i', BigTechItemTags.MAGNETITE_INGOTS)
			.result(this.id)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_transmute")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input":  { "tag":  "c:iron_blocks"           },
				"output": { "item": "bigtech:magnetite_block" },
				"energy": 810
			}"""
		);
	}
}