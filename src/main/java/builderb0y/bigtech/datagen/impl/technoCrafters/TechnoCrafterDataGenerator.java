package builderb0y.bigtech.datagen.impl.technoCrafters;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.items.FunctionalItems;

public class TechnoCrafterDataGenerator extends BasicBlockDataGenerator {

	public TechnoCrafterDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return "Techno Crafter";
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
			.pattern("trt", "rir", "rcr")
			.where('t', FunctionalItems.STONE_CRAFTING_TABLE)
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.where('i', DecoItems.IRON_FRAME)
			.where('c', ConventionalItemTags.CHESTS)
			.result(this.getId())
			.toString()
		);
	}
}