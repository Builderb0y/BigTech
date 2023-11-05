package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

@Dependencies(CommonBeamEmitterDataGenerator.class)
public class RedstoneTransmitterDataGenerator extends BasicBlockDataGenerator {

	public RedstoneTransmitterDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.block)
				.map(state -> new BlockStateJsonVariant(
					state,
					context.prefixSuffixPath("block/", this.id, state.get(Properties.POWERED) ? "_on" : "_off").toString(),
					null,
					BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_off")),
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
					"body_side":   "bigtech:block/redstone_transmitter_side"
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
							"east":  { "uv": [ 12, 3, 13, 5 ], "texture": "#base_top", "rotation": 90  },
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
							"east":  { "uv": [ 12, 11, 13, 13 ], "texture": "#base_top", "rotation": 90  },
							"west":  { "uv": [  3, 11,  4, 13 ], "texture": "#base_top", "rotation": 270 }
						}
					},
					{
						"from": [  4,  4,  1 ],
						"to":   [ 12, 12, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 0, 15,  8 ], "texture": "#body_top", "rotation": 90 },
							"down":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_top", "rotation": 90 },
							"north": { "uv": [ 0, 0,  8,  8 ], "texture": "#body_front"               },
							"south": { "uv": [ 4, 8, 12, 16 ], "texture": "#body_front"               },
							"east":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_side"                },
							"west":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_side"                }
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
			context.blockModelPath(context.suffixPath(this.id, "_on")),
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
					"body_side":   "bigtech:block/redstone_transmitter_side"
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
							"east":  { "uv": [ 12, 3, 13, 5 ], "texture": "#base_top", "rotation": 90  },
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
							"east":  { "uv": [ 12, 11, 13, 13 ], "texture": "#base_top", "rotation": 90  },
							"west":  { "uv": [  3, 11,  4, 13 ], "texture": "#base_top", "rotation": 270 }
						}
					},
					{
						"from": [  4,  4,  1 ],
						"to":   [ 12, 12, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 0, 15,  8 ], "texture": "#body_top", "rotation": 90 },
							"down":  { "uv": [ 1, 8, 15, 16 ], "texture": "#body_top", "rotation": 90 },
							"north": { "uv": [ 8, 0, 16,  8 ], "texture": "#body_front" },
							"south": { "uv": [ 4, 8, 12, 16 ], "texture": "#body_front" },
							"east":  { "uv": [ 1, 0, 15,  8 ], "texture": "#body_side"  },
							"west":  { "uv": [ 1, 0, 15,  8 ], "texture": "#body_side"  }
						}
					},
					{
						"from": [ 7, 7, 0 ],
						"to":   [ 9, 9, 1 ],
						"shade": false,
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
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.id, "_off");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood pickaxe is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		//todo: recipe involving lens. (need to add lens)
	}
}