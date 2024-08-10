package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;

public class StoneCraftingTableDataGenerator extends BasicBlockDataGenerator {

	public StoneCraftingTableDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "minecraft:block/cube",
				"textures": {
					"up":       "bigtech:block/stone_crafting_table_top",
					"down":     "minecraft:block/cobblestone",
					"north":    "bigtech:block/stone_crafting_table_front",
					"south":    "bigtech:block/stone_crafting_table_side",
					"east":     "bigtech:block/stone_crafting_table_side",
					"west":     "bigtech:block/stone_crafting_table_front",
					"particle": "bigtech:block/stone_crafting_table_front"
				}
			}"""
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wooden pickaxe is fine.
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
			context.recipePath(context.suffixPath(this.getId(), "_without_wood")),
			new ShapedRecipeBuilder()
			.result(this.getId())
			.pattern("cc", "cc")
			.where('c', ItemTags.STONE_CRAFTING_MATERIALS)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_with_wood")),
			new ShapelessRecipeBuilder()
			.result(this.getId())
			.ingredient(Items.CRAFTING_TABLE)
			.ingredient(ItemTags.STONE_CRAFTING_MATERIALS)
			.ingredient(ItemTags.STONE_CRAFTING_MATERIALS)
			.ingredient(ItemTags.STONE_CRAFTING_MATERIALS)
			.toString()
		);
	}
}