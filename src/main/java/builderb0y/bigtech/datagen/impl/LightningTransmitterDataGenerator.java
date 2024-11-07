package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

public class LightningTransmitterDataGenerator extends RedstoneTransceiverDataGenerator {

	public LightningTransmitterDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_off")),
			//language=json
			"""
			{
				"parent": "bigtech:block/beam_emitter_base_transforms",
				"textures": {
					"base_bottom": "bigtech:block/lightning_transmitter_base_bottom",
					"base_side":   "bigtech:block/lightning_transmitter_base_side",
					"body_top":    "bigtech:block/lightning_transmitter_side",
					"body_front":  "bigtech:block/lightning_transmitter_front",
					"particle":    "bigtech:block/lightning_transmitter_side"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 3, 16 ],
						"faces": {
							"up":    { "uv": [ 0,  0, 16, 16 ], "texture": "#base_bottom"                     },
							"down":  { "uv": [ 0,  0, 16, 16 ], "texture": "#base_bottom", "cullface": "down" },
							"north": { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"                       },
							"south": { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"                       },
							"east":  { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"                       },
							"west":  { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"                       }
						}
					},
					{
						"from": [  3, 3, 3 ],
						"to":   [ 13, 4, 5 ],
						"faces": {
							"up":    { "uv": [  3, 3, 13, 5 ], "texture": "#base_side"                  },
							"north": { "uv": [ 13, 3,  3, 4 ], "texture": "#base_side"                  },
							"south": { "uv": [  3, 4, 13, 5 ], "texture": "#base_side"                  },
							"east":  { "uv": [ 12, 3, 13, 5 ], "texture": "#base_side", "rotation":  90 },
							"west":  { "uv": [  3, 3,  4, 5 ], "texture": "#base_side", "rotation": 270 }
						}
					},
					{
						"from": [  3, 3, 11 ],
						"to":   [ 13, 4, 13 ],
						"faces": {
							"up":    { "uv": [  3, 11, 13, 13 ], "texture": "#base_side"                  },
							"north": { "uv": [ 13, 11,  3, 12 ], "texture": "#base_side"                  },
							"south": { "uv": [  3, 12, 13, 13 ], "texture": "#base_side"                  },
							"east":  { "uv": [ 12, 11, 13, 13 ], "texture": "#base_side", "rotation":  90 },
							"west":  { "uv": [  3, 11,  4, 13 ], "texture": "#base_side", "rotation": 270 }
						}
					},
					{
						"from": [  4,  4,  1 ],
						"to":   [ 12, 12, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 8, 15, 16 ], "texture": "#body_top", "rotation": 270 },
							"down":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_top", "rotation":  90 },
							"north": { "uv": [ 0, 0,  8,  8 ], "texture": "#body_front"                },
							"south": { "uv": [ 4, 8, 12, 16 ], "texture": "#body_front"                },
							"east":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_top"                  },
							"west":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_top", "rotation": 180 }
						}
					},
					{
						"from": [  6, 7, 0 ],
						"to":   [ 10, 9, 1 ],
						"faces": {
							"up":    { "uv": [ 6, 3, 2, 4 ], "texture": "#body_front" },
							"down":  { "uv": [ 6, 4, 2, 5 ], "texture": "#body_front" },
							"north": { "uv": [ 2, 3, 6, 5 ], "texture": "#body_front" },
							"east":  { "uv": [ 2, 3, 3, 5 ], "texture": "#body_front" },
							"west":  { "uv": [ 5, 3, 6, 5 ], "texture": "#body_front" }
						}
					},
					{
						"from": [ 7, 6, 0 ],
						"to":   [ 9, 7, 1 ],
						"faces": {
							"down":  { "uv": [ 5, 5, 3, 6 ], "texture": "#body_front" },
							"north": { "uv": [ 3, 5, 5, 6 ], "texture": "#body_front" },
							"east":  { "uv": [ 3, 5, 4, 6 ], "texture": "#body_front" },
							"west":  { "uv": [ 4, 5, 5, 6 ], "texture": "#body_front" }
						}
					},
					{
						"from": [ 7,  9, 0 ],
						"to":   [ 9, 10, 1 ],
						"faces": {
							"up":    { "uv": [ 5, 2, 3, 3 ], "texture": "#body_front" },
							"north": { "uv": [ 3, 2, 5, 3 ], "texture": "#body_front" },
							"east":  { "uv": [ 3, 2, 4, 3 ], "texture": "#body_front" },
							"west":  { "uv": [ 4, 2, 5, 3 ], "texture": "#body_front" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_on")),
			//language=json
			"""
			{
				"parent": "bigtech:block/beam_emitter_base_transforms",
				"textures": {
					"base_bottom": "bigtech:block/lightning_transmitter_base_bottom",
					"base_side":   "bigtech:block/lightning_transmitter_base_side",
					"body_top":    "bigtech:block/lightning_transmitter_side",
					"body_front":  "bigtech:block/lightning_transmitter_front",
					"particle":    "bigtech:block/lightning_transmitter_side"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 3, 16 ],
						"faces": {
							"up":    { "uv": [ 0,  0, 16, 16 ], "texture": "#base_bottom"                     },
							"down":  { "uv": [ 0,  0, 16, 16 ], "texture": "#base_bottom", "cullface": "down" },
							"north": { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"                       },
							"south": { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"                       },
							"east":  { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"                       },
							"west":  { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"                       }
						}
					},
					{
						"from": [  3, 3, 3 ],
						"to":   [ 13, 4, 5 ],
						"faces": {
							"up":    { "uv": [  3, 3, 13, 5 ], "texture": "#base_side"                  },
							"north": { "uv": [ 13, 3,  3, 4 ], "texture": "#base_side"                  },
							"south": { "uv": [  3, 4, 13, 5 ], "texture": "#base_side"                  },
							"east":  { "uv": [ 12, 3, 13, 5 ], "texture": "#base_side", "rotation":  90 },
							"west":  { "uv": [  3, 3,  4, 5 ], "texture": "#base_side", "rotation": 270 }
						}
					},
					{
						"from": [  3, 3, 11 ],
						"to":   [ 13, 4, 13 ],
						"faces": {
							"up":    { "uv": [  3, 11, 13, 13 ], "texture": "#base_side"                  },
							"north": { "uv": [ 13, 11,  3, 12 ], "texture": "#base_side"                  },
							"south": { "uv": [  3, 12, 13, 13 ], "texture": "#base_side"                  },
							"east":  { "uv": [ 12, 11, 13, 13 ], "texture": "#base_side", "rotation":  90 },
							"west":  { "uv": [  3, 11,  4, 13 ], "texture": "#base_side", "rotation": 270 }
						}
					},
					{
						"from": [  4,  4,  1 ],
						"to":   [ 12, 12, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 0, 15,  8 ], "texture": "#body_top", "rotation": 270 },
							"down":  { "uv": [ 1, 0, 15,  8 ], "texture": "#body_top", "rotation":  90 },
							"north": { "uv": [ 8, 0, 16,  8 ], "texture": "#body_front"                },
							"south": { "uv": [ 4, 8, 12, 16 ], "texture": "#body_front"                },
							"east":  { "uv": [ 1, 0, 15,  8 ], "texture": "#body_top"                  },
							"west":  { "uv": [ 1, 0, 15,  8 ], "texture": "#body_top", "rotation": 180 }
						}
					},
					{
						"shade": false,
						"light_emission": 15,
						"from": [  6, 7, 0 ],
						"to":   [ 10, 9, 1 ],
						"faces": {
							"up":    { "uv": [ 14, 3, 10, 4 ], "texture": "#body_front" },
							"down":  { "uv": [ 14, 4, 10, 5 ], "texture": "#body_front" },
							"north": { "uv": [ 10, 3, 14, 5 ], "texture": "#body_front" },
							"east":  { "uv": [ 10, 3, 11, 5 ], "texture": "#body_front" },
							"west":  { "uv": [ 13, 3, 14, 5 ], "texture": "#body_front" }
						}
					},
					{
						"shade": false,
						"light_emission": 15,
						"from": [ 7, 6, 0 ],
						"to":   [ 9, 7, 1 ],
						"faces": {
							"down":  { "uv": [ 13, 5, 11, 6 ], "texture": "#body_front" },
							"north": { "uv": [ 11, 5, 13, 6 ], "texture": "#body_front" },
							"east":  { "uv": [ 11, 5, 12, 6 ], "texture": "#body_front" },
							"west":  { "uv": [ 12, 5, 13, 6 ], "texture": "#body_front" }
						}
					},
					{
						"shade": false,
						"light_emission": 15,
						"from": [ 7,  9, 0 ],
						"to":   [ 9, 10, 1 ],
						"faces": {
							"up":    { "uv": [ 13, 2, 11, 3 ], "texture": "#body_front" },
							"north": { "uv": [ 11, 2, 13, 3 ], "texture": "#body_front" },
							"east":  { "uv": [ 11, 2, 12, 3 ], "texture": "#body_front" },
							"west":  { "uv": [ 12, 2, 13, 3 ], "texture": "#body_front" }
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
			.pattern("iii", "lct", "bbb")
			.where('i', ConventionalItemTags.COPPER_INGOTS)
			.where('l', FunctionalItems.LENS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.where('t', FunctionalItems.COPPER_COIL)
			.where('b', ConventionalItemTags.IRON_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}