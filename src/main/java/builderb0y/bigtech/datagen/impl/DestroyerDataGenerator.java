package builderb0y.bigtech.datagen.impl;

import java.util.Arrays;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

public class DestroyerDataGenerator extends BasicBlockDataGenerator {

	public DestroyerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER)
				.map(direction -> new BlockStateJsonVariant(
					"facing=${direction.getName()}",
					context.prefixPath("block/", this.id).toString(),
					null,
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.id),
			//use furnace rotation to face the player.
			"""
			{
				"parent": "block/cube",
				"display": {
					"firstperson_righthand": {
						"rotation":    [ 0,   135,   0   ],
						"translation": [ 0,     0,   0   ],
						"scale":       [ 0.4,   0.4, 0.4 ]
					}
				},
				"textures": {
					"up":       "bigtech:block/destroyer_top",
					"down":     "bigtech:block/destroyer_top",
					"north":    "bigtech:block/destroyer_front",
					"east":     "bigtech:block/destroyer_side",
					"south":    "bigtech:block/destroyer_back",
					"west":     "bigtech:block/destroyer_side",
					"particle": "bigtech:block/destroyer_front"
				}
			}"""
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood is fine.
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
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("sss", "lcd", "sss")
			.where('s', Items.SMOOTH_STONE)
			.where('l', FunctionalItems.LENS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.where('d', Items.IRON_PICKAXE)
			.result(this.id)
			.toString()
		);
	}
}