package builderb0y.bigtech.datagen.impl.lightningJars;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItems;

public class LargeLightningJarDataGenerator extends LightningJarDataGenerator {

	public LargeLightningJarDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		this.writeDefaultBlockstateJson(context, this.id);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.id),
			//language=json
			"""
			{
				"parent": "block/block",
				"textures": {
					"particle": "bigtech:block/large_lightning_jar_side",
					"base":     "bigtech:block/large_lightning_jar_bottom",
					"side":     "bigtech:block/large_lightning_jar_side",
					"top":      "bigtech:block/large_lightning_jar_top",
					"rod":      "bigtech:block/lightning_jar_interior"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 3, 16 ],
						"faces": {
							"up":    { "uv": [ 0,  0, 16, 16 ], "texture": "#base" },
							"down":  { "uv": [ 0,  0, 16, 16 ], "texture": "#base", "cullface": "down" },
							"north": { "uv": [ 0, 13, 16, 16 ], "texture": "#side", "cullface": "north" },
							"south": { "uv": [ 0, 13, 16, 16 ], "texture": "#side", "cullface": "south" },
							"east":  { "uv": [ 0, 13, 16, 16 ], "texture": "#side", "cullface": "east" },
							"west":  { "uv": [ 0, 13, 16, 16 ], "texture": "#side", "cullface": "west" }
						}
					},
					{
						"from": [  1,  3,  1 ],
						"to":   [ 15, 15, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 1, 15, 15 ], "texture": "#top" },
							"north": { "uv": [ 1, 1, 15, 13 ], "texture": "#side" },
							"south": { "uv": [ 1, 1, 15, 13 ], "texture": "#side" },
							"east":  { "uv": [ 1, 1, 15, 13 ], "texture": "#side" },
							"west":  { "uv": [ 1, 1, 15, 13 ], "texture": "#side" }
						}
					},
					{
						"from": [ 3,  3, 3 ],
						"to":   [ 7, 13, 7 ],
						"faces": {
							"up":    { "uv": [ 11, 6, 15, 10 ], "texture": "#rod" },
							"north": { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"south": { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"east":  { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"west":  { "uv": [  6, 3, 10, 13 ], "texture": "#rod" }
						}
					},
					{
						"from": [  9,  3, 3 ],
						"to":   [ 13, 13, 7 ],
						"faces": {
							"up":    { "uv": [ 11, 6, 15, 10 ], "texture": "#rod" },
							"north": { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"south": { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"east":  { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"west":  { "uv": [  6, 3, 10, 13 ], "texture": "#rod" }
						}
					},
					{
						"from": [ 3,  3,  9 ],
						"to":   [ 7, 13, 13 ],
						"faces": {
							"up":    { "uv": [ 11, 6, 15, 10 ], "texture": "#rod" },
							"north": { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"south": { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"east":  { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"west":  { "uv": [  6, 3, 10, 13 ], "texture": "#rod" }
						}
					},
					{
						"from": [  9,  3,  9 ],
						"to":   [ 13, 13, 13 ],
						"faces": {
							"up":    { "uv": [ 11, 6, 15, 10 ], "texture": "#rod" },
							"north": { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"south": { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"east":  { "uv": [  6, 3, 10, 13 ], "texture": "#rod" },
							"west":  { "uv": [  6, 3, 10, 13 ], "texture": "#rod" }
						}
					}
				]
			}"""
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.pattern("ggg", "geg", "ppp")
			.where('g', ConventionalItemTags.GLASS_PANES)
			.where('e', BigTechItems.QUAD_LIGHTNING_ELECTRODE)
			.where('p', BigTechItems.MEDIUM_WEIGHTED_PRESSURE_PLATE)
			.result(this.id)
			.toString()
		);
	}
}