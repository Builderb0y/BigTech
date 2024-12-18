package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class RedstoneReceiverDataGenerator extends RedstoneTransceiverDataGenerator {

	public RedstoneReceiverDataGenerator(BlockItem blockItem) {
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
					"base_bottom": "minecraft:block/smooth_stone",
					"base_top":    "bigtech:block/beam_emitter_base_top",
					"base_side":   "bigtech:block/beam_emitter_base_side",
					"body_top":    "bigtech:block/quartz_top_bottom",
					"body_front":  "bigtech:block/redstone_receiver_front",
					"body_side":   "bigtech:block/redstone_receiver_side",
					"particle":    "bigtech:block/redstone_receiver_side"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 3, 16 ],
						"faces": {
							"up":    { "uv": [ 0,  0, 16, 16 ], "texture": "#base_top"    },
							"down":  { "uv": [ 0,  0, 16, 16 ], "texture": "#base_bottom" },
							"north": { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"   },
							"south": { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"   },
							"east":  { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"   },
							"west":  { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"   }
						}
					},
					{
						"from": [  3, 3, 3 ],
						"to":   [ 13, 4, 5 ],
						"faces": {
							"up":    { "uv": [  3, 3, 13, 5 ], "texture": "#base_top"                  },
							"north": { "uv": [ 13, 3,  3, 4 ], "texture": "#base_top"                  },
							"south": { "uv": [  3, 4, 13, 5 ], "texture": "#base_top"                  },
							"east":  { "uv": [ 12, 3, 13, 5 ], "texture": "#base_top", "rotation":  90 },
							"west":  { "uv": [  3, 3,  4, 5 ], "texture": "#base_top", "rotation": 270 }
						}
					},
					{
						"from": [  3, 3, 11 ],
						"to":   [ 13, 4, 13 ],
						"faces": {
							"up":    { "uv": [  3, 11, 13, 13 ], "texture": "#base_top"                  },
							"north": { "uv": [ 13, 11,  3, 12 ], "texture": "#base_top"                  },
							"south": { "uv": [  3, 12, 13, 13 ], "texture": "#base_top"                  },
							"east":  { "uv": [ 12, 11, 13, 13 ], "texture": "#base_top", "rotation":  90 },
							"west":  { "uv": [  3, 11,  4, 13 ], "texture": "#base_top", "rotation": 270 }
						}
					},
					{
						"from": [  4,  4,  1 ],
						"to":   [ 12, 12, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 0, 15,  8 ], "texture": "#body_top",  "rotation": 90 },
							"down":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_top",  "rotation": 90 },
							"north": { "uv": [ 0, 0,  8,  8 ], "texture": "#body_front"                },
							"south": { "uv": [ 4, 8, 12, 16 ], "texture": "#body_front"                },
							"east":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_side"                 },
							"west":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_side"                 }
						}
					},
					{
						"from": [  6,  6, 0 ],
						"to":   [ 10, 10, 1 ],
						"faces": {
							"up":    { "uv": [ 6, 2, 2, 3 ], "texture": "#body_front" },
							"down":  { "uv": [ 6, 5, 2, 6 ], "texture": "#body_front" },
							"north": { "uv": [ 2, 2, 6, 6 ], "texture": "#body_front" },
							"east":  { "uv": [ 2, 2, 3, 6 ], "texture": "#body_front" },
							"west":  { "uv": [ 5, 2, 6, 6 ], "texture": "#body_front" }
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
					"base_bottom": "minecraft:block/smooth_stone",
					"base_top":    "bigtech:block/beam_emitter_base_top",
					"base_side":   "bigtech:block/beam_emitter_base_side",
					"body_top":    "bigtech:block/quartz_top_bottom",
					"body_front":  "bigtech:block/redstone_receiver_front",
					"body_side":   "bigtech:block/redstone_receiver_side",
					"particle":    "bigtech:block/redstone_receiver_side"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 3, 16 ],
						"faces": {
							"up":    { "uv": [ 0,  0, 16, 16 ], "texture": "#base_top"    },
							"down":  { "uv": [ 0,  0, 16, 16 ], "texture": "#base_bottom" },
							"north": { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"   },
							"south": { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"   },
							"east":  { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"   },
							"west":  { "uv": [ 0, 13, 16, 16 ], "texture": "#base_side"   }
						}
					},
					{
						"from": [  3, 3, 3 ],
						"to":   [ 13, 4, 5 ],
						"faces": {
							"up":    { "uv": [  3, 3, 13, 5 ], "texture": "#base_top"                  },
							"north": { "uv": [ 13, 3,  3, 4 ], "texture": "#base_top"                  },
							"south": { "uv": [  3, 4, 13, 5 ], "texture": "#base_top"                  },
							"east":  { "uv": [ 12, 3, 13, 5 ], "texture": "#base_top", "rotation":  90 },
							"west":  { "uv": [  3, 3,  4, 5 ], "texture": "#base_top", "rotation": 270 }
						}
					},
					{
						"from": [  3, 3, 11 ],
						"to":   [ 13, 4, 13 ],
						"faces": {
							"up":    { "uv": [  3, 11, 13, 13 ], "texture": "#base_top"                  },
							"north": { "uv": [ 13, 11,  3, 12 ], "texture": "#base_top"                  },
							"south": { "uv": [  3, 12, 13, 13 ], "texture": "#base_top"                  },
							"east":  { "uv": [ 12, 11, 13, 13 ], "texture": "#base_top", "rotation":  90 },
							"west":  { "uv": [  3, 11,  4, 13 ], "texture": "#base_top", "rotation": 270 }
						}
					},
					{
						"from": [  4,  4,  1 ],
						"to":   [ 12, 12, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 0, 15,  8 ], "texture": "#body_top",  "rotation": 90 },
							"down":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_top",  "rotation": 90 },
							"north": { "uv": [ 8, 0, 16,  8 ], "texture": "#body_front"                },
							"south": { "uv": [ 4, 8, 12, 16 ], "texture": "#body_front"                },
							"east":  { "uv": [ 1, 0, 15,  8 ], "texture": "#body_side"                 },
							"west":  { "uv": [ 1, 0, 15,  8 ], "texture": "#body_side"                 }
						}
					},
					{
						"shade": false,
						"light_emission": 15,
						"from": [  6,  6, 0 ],
						"to":   [ 10, 10, 1 ],
						"faces": {
							"up":    { "uv": [ 14, 2, 10, 3 ], "texture": "#body_front" },
							"down":  { "uv": [ 14, 5, 10, 6 ], "texture": "#body_front" },
							"north": { "uv": [ 10, 2, 14, 6 ], "texture": "#body_front" },
							"east":  { "uv": [ 10, 2, 11, 6 ], "texture": "#body_front" },
							"west":  { "uv": [ 13, 2, 14, 6 ], "texture": "#body_front" }
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
			.pattern("qqq", "l r", "bbb")
			.where('q', ConventionalItemTags.QUARTZ_GEMS)
			.where('l', FunctionalItems.LENS)
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.where('b', Items.SMOOTH_STONE_SLAB)
			.result(this.getId())
			.toString()
		);
	}
}