package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class TechnoCrafterDataGenerator extends BasicBlockDataGenerator {

	public TechnoCrafterDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "minecraft:block/cube_bottom_top",
				"textures": {
					"top": "bigtech:block/techno_crafter_top",
					"side": "bigtech:block/techno_crafter_side",
					"bottom": "minecraft:block/iron_block",
					"particle": "bigtech:block/techno_crafter_side"
				}
			}
			"""
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

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("rcr", "rir", "rcr")
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.where('c', FunctionalItems.STONE_CRAFTING_TABLE)
			.where('i', ConventionalItemTags.STORAGE_BLOCKS_IRON)
			.result(this.getId())
			.toString()
		);
	}
}