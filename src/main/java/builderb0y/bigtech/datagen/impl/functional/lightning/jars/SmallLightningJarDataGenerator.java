package builderb0y.bigtech.datagen.impl.functional.lightning.jars;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class SmallLightningJarDataGenerator extends LightningJarDataGenerator {

	public SmallLightningJarDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		this.writeDefaultBlockstateJson(context, this.getId());
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "block/block",
				"textures": {
					"particle": "bigtech:block/small_lightning_jar_side",
					"base":     "bigtech:block/small_lightning_jar_bottom",
					"side":     "bigtech:block/small_lightning_jar_side",
					"top":      "bigtech:block/small_lightning_jar_top",
					"rod":      "bigtech:block/lightning_jar_interior"
				},
				"elements": [
					{
						"from": [  3, 0,  3 ],
						"to":   [ 13, 3, 13 ],
						"faces": {
							"up":    { "uv": [ 3,  3, 13, 13 ], "texture": "#base" },
							"down":  { "uv": [ 3,  3, 13, 13 ], "texture": "#base" },
							"north": { "uv": [ 3, 13, 13, 16 ], "texture": "#side" },
							"south": { "uv": [ 3, 13, 13, 16 ], "texture": "#side" },
							"east":  { "uv": [ 3, 13, 13, 16 ], "texture": "#side" },
							"west":  { "uv": [ 3, 13, 13, 16 ], "texture": "#side" }
						}
					},
					{
						"from": [  4,  3,  4 ],
						"to":   [ 12, 15, 12 ],
						"faces": {
							"up":    { "uv": [ 4, 4, 12, 12 ], "texture": "#top"  },
							"north": { "uv": [ 4, 1, 12, 13 ], "texture": "#side" },
							"south": { "uv": [ 4, 1, 12, 13 ], "texture": "#side" },
							"east":  { "uv": [ 4, 1, 12, 13 ], "texture": "#side" },
							"west":  { "uv": [ 4, 1, 12, 13 ], "texture": "#side" }
						}
					},
					{
						"from": [  6,  3,  6 ],
						"to":   [ 10, 13, 10 ],
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
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("ggg", "geg", "ppp")
			.where('g', ConventionalItemTags.GLASS_PANES)
			.where('e', FunctionalItems.LIGHTNING_ELECTRODE)
			.where('p', FunctionalItems.MEDIUM_WEIGHTED_PRESSURE_PLATE)
			.result(this.getId())
			.toString()
		);
	}
}