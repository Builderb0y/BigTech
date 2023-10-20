package builderb0y.bigtech.datagen.impl.ascenders;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.BigTechItems;

public class AscenderDataGenerator extends AbstractAscenderDataGenerator {

	public AscenderDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_ascender")),
			"""
				{
					"jmxl": true,
					"parent": "minecraft:block/block",
					"ambientocclusion": false,
					"textures": {
						"outer":       "bigtech:block/ascender_outer",
						"inner":       "bigtech:block/ascender_inner",
						"top_bottom":  "bigtech:block/ascender_top_bottom",
						"particle":    "bigtech:block/ascender_outer"
					},
					"elements": [
						{
							"from": [ 0,  0,  0 ],
							"to":   [ 2, 16, 16 ],
							"faces": {
								"east": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                     },
								"west": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "west" }
							}
						},
						{
							"from": [  0,  0, 0 ],
							"to":   [ 16, 16, 2 ],
							"faces": {
								"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "north" },
								"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                      }
							}
						},
						{
							"from": [ 14,  0,  0 ],
							"to":   [ 16, 16, 16 ],
							"faces": {
								"east": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "east" },
								"west": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                     }
							}
						},
						{
							"from": [  0,  0, 14 ],
							"to":   [ 16, 16, 16 ],
							"faces": {
								"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                      },
								"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "south" }
							}
						},
						{
							"from": [  0,  0,  0 ],
							"to":   [ 16, 16, 16 ],
							"faces": {
								"up":   { "uv": [ 0, 0, 16, 16 ], "texture": "#top_bottom", "cullface": "up"   },
								"down": { "uv": [ 0, 0, 16, 16 ], "texture": "#top_bottom", "cullface": "down" }
							}
						},
						{
							"from": [  1,  0,  1 ],
							"to":   [ 15, 16, 15 ],
							"jmxl_layer": "TRANSLUCENT",
							"faces": {
								"north": { "uv": [ 1, 0, 15, 16 ], "texture": "#translucent" },
								"east":  { "uv": [ 1, 0, 15, 16 ], "texture": "#translucent" },
								"south": { "uv": [ 1, 0, 15, 16 ], "texture": "#translucent" },
								"west":  { "uv": [ 1, 0, 15, 16 ], "texture": "#translucent" }
							}
						}
					]
				}"""
		);
		this.writeAscenderModel(context, BigTechMod.modID("ascender_translucent"));
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("ascender_from_belts")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group(BigTechMod.modID("ascenders"))
			.pattern("bgb", "b b", "bgb")
			.itemIngredient('b', BigTechItems.BELT)
			.tagIngredient('g', ConventionalItemTags.GLASS_BLOCKS)
			.result(BigTechItems.ASCENDER)
			.count(6)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("ascender_from_descender")),
			new ShapelessRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group(BigTechMod.modID("ascenders"))
			.itemIngredient(BigTechItems.DESCENDER)
			.result(BigTechItems.ASCENDER)
			.toString()
		);
	}
}