package builderb0y.bigtech.datagen.impl.belts;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItems;

public class NormalBeltDataGenerator extends DirectionalBeltDataGenerator {

	public NormalBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			"bigtech:block/belts/normal",
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("belts/template")),
			"""
			{
				"parent": "block/thin_block",
				"textures": {
					"particle": "#belt",
					"bottom": "bigtech:block/belts/bottom"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 1, 16 ],
						"faces": {
							"up":    { "uv": [  0,  0, 16, 16 ], "texture": "#belt"                                         },
							"down":  { "uv": [  0,  0, 16, 16 ], "texture": "#bottom", "cullface": "down"                   },
							"north": { "uv": [  0,  0, 16,  1 ], "texture": "#belt",   "cullface": "north", "rotation": 180 },
							"south": { "uv": [  0, 15, 16, 16 ], "texture": "#belt",   "cullface": "south"                  },
							"east":  { "uv": [ 15,  0, 16, 16 ], "texture": "#belt",   "cullface": "east",  "rotation": 90  },
							"west":  { "uv": [  0,  0,  1, 16 ], "texture": "#belt",   "cullface": "west",  "rotation": 270 }
						}
					}
				]
			}
			"""
		);
		this.writeBeltBlockModel(context, "normal");
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeBeltItemModel(context, "normal");
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("belt_from_paper")),
			new ShapedRecipeBuilder()
			.group("bigtech:belts")
			.pattern("ppp", "i i")
			.itemIngredient('p', Items.PAPER)
			.tagIngredient('i', ConventionalItemTags.IRON_INGOTS)
			.result(BigTechItems.BELT)
			.count(3)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("belt_from_leather")),
			new ShapedRecipeBuilder()
			.group("bigtech:belts")
			.pattern("lll", "i i")
			.itemIngredient('l', Items.LEATHER)
			.tagIngredient('i', ConventionalItemTags.IRON_INGOTS)
			.result(BigTechItems.BELT)
			.count(6)
			.toString()
		);
	}
}