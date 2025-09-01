package builderb0y.bigtech.datagen.impl.functional.lasers;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.items.MaterialItems;

@Dependencies(CommonBeamEmitterDataGenerator.class)
public class RedstoneTransmitterDataGenerator extends RedstoneTransceiverDataGenerator {

	public RedstoneTransmitterDataGenerator(BlockItem blockItem) {
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
					"body_front":  "bigtech:block/redstone_transmitter_front",
					"body_side":   "bigtech:block/redstone_transmitter_side",
					"particle":    "bigtech:block/redstone_transmitter_side"
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
						"from": [ 7, 7, 0 ],
						"to":   [ 9, 9, 1 ],
						"faces": {
							"up":    { "uv": [ 5, 3, 3, 4 ], "texture": "#body_front" },
							"down":  { "uv": [ 5, 4, 3, 5 ], "texture": "#body_front" },
							"north": { "uv": [ 3, 3, 5, 5 ], "texture": "#body_front" },
							"east":  { "uv": [ 3, 3, 4, 5 ], "texture": "#body_front" },
							"west":  { "uv": [ 4, 3, 5, 5 ], "texture": "#body_front" }
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
					"body_front":  "bigtech:block/redstone_transmitter_front",
					"body_side":   "bigtech:block/redstone_transmitter_side",
					"particle":    "bigtech:block/redstone_transmitter_side"
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
						"from": [ 7, 7, 0 ],
						"to":   [ 9, 9, 1 ],
						"faces": {
							"up":    { "uv": [ 13, 3, 11, 4 ], "texture": "#body_front" },
							"down":  { "uv": [ 13, 4, 11, 5 ], "texture": "#body_front" },
							"north": { "uv": [ 11, 3, 13, 5 ], "texture": "#body_front" },
							"east":  { "uv": [ 11, 3, 12, 5 ], "texture": "#body_front" },
							"west":  { "uv": [ 12, 3, 13, 5 ], "texture": "#body_front" }
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
			.pattern("qqq", "lcr", "bbb")
			.where('q', ConventionalItemTags.QUARTZ_GEMS)
			.where('l', FunctionalItems.LENS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.where('r', MaterialItems.PHOTOVOLTAIC_EMITTER)
			.where('b', Items.SMOOTH_STONE_SLAB)
			.result(this.getId())
			.toString()
		);
	}
}