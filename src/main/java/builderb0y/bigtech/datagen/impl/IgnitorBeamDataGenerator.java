package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

public class IgnitorBeamDataGenerator extends BasicBlockDataGenerator {

	public IgnitorBeamDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return "Thermal Discouragement Beam";
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.getBlock())
				.map((BlockState state) -> new BlockStateJsonVariant(
					state,
					context.prefixSuffixPath("block/", this.getId(), state.get(Properties.LIT) ? "_lit" : "").toString(),
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
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "bigtech:block/beam_emitter_base_transforms",
				"textures": {
					"base_bottom": "minecraft:block/smooth_stone",
					"base_top":    "bigtech:block/beam_emitter_base_top",
					"base_side":   "bigtech:block/beam_emitter_base_side",
					"body_top":    "bigtech:block/ignitor_beam_side",
					"body_front":  "bigtech:block/ignitor_beam_front",
					"body_side":   "bigtech:block/ignitor_beam_side",
					"particle":    "bigtech:block/ignitor_beam_side"
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
			context.blockModelPath(context.suffixPath(this.getId(), "_lit")),
			//language=json
			"""
			{
				"parent": "bigtech:block/beam_emitter_base_transforms",
				"textures": {
					"base_bottom": "minecraft:block/smooth_stone",
					"base_top":    "bigtech:block/beam_emitter_base_top",
					"base_side":   "bigtech:block/beam_emitter_base_side",
					"body_top":    "bigtech:block/ignitor_beam_side",
					"body_front":  "bigtech:block/ignitor_beam_front",
					"body_side":   "bigtech:block/ignitor_beam_side",
					"particle":    "bigtech:block/ignitor_beam_side"
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
						"from": [ 6, 6, 0 ],
						"to": [ 10, 10, 1 ],
						"shade": false,
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
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
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
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("ddd", "lci", "bbb")
			.where('d', Items.COBBLED_DEEPSLATE)
			.where('l', FunctionalItems.LENS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.where('i', FunctionalItems.IGNITOR)
			.where('b', Items.SMOOTH_STONE_SLAB)
			.result(this.getId())
			.toString()
		);
	}
}