package builderb0y.bigtech.datagen.impl.functional.lasers;

import java.util.Map;
import java.util.Objects;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.FunctionalItems;

public class BeamInterceptorDataGenerator extends BasicBlockDataGenerator {

	public BeamInterceptorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put("bigtech.beam_interceptor.unlocked", "Now detecting all colors");
		context.lang.put("bigtech.beam_interceptor.locked", "Now detecting %s");
		context.lang.put("bigtech.beam_interceptor.current", "Current color: %s");
		context.lang.put("bigtech.beam_interceptor.cant_lock", "§cCannot lock without any beams");
		context.lang.put("bigtech.beam_interceptor.no_color", "none");
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(Properties.POWERED) ? "_on" : "_off").toString(),
			BlockStateJsonVariant.xFromUp(state.get(Properties.FACING).getOpposite()),
			Objects.requireNonNullElse(BlockStateJsonVariant.yFromNorth(state.get(Properties.FACING).getOpposite()), 0)
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_transforms")),
			//language=json
			"""
			{
				"parent": "block/block",
				"display": {
					"gui": {
						"rotation":    [ 30,    225,     0     ],
						"translation": [  0,      3,     0     ],
						"scale":       [  1.0,    1.0,   1.0   ]
					},
					"ground": {
						"rotation":    [  0,      0,     0     ],
						"translation": [  0,      3,     0     ],
						"scale":       [  0.25,   0.25,  0.25  ]
					},
					"fixed": {
						"rotation":    [  0,      0,     0     ],
						"translation": [  0,      0,     0     ],
						"scale":       [  0.5,    0.5,   0.5   ]
					},
					"thirdperson_righthand": {
						"rotation":    [ 75,     45,     0     ],
						"translation": [  0,      2.5,   0     ],
						"scale":       [  0.375,  0.375, 0.375 ]
					},
					"firstperson_righthand": {
						"rotation":    [  0,     45,     0     ],
						"translation": [  0,      4,     0     ],
						"scale":       [  0.40,   0.40,  0.40  ]
					},
					"firstperson_lefthand": {
						"rotation":    [  0,    225,     0     ],
						"translation": [  0,      4,     0     ],
						"scale":       [  0.40,   0.40,  0.40  ]
					}
				}
			}"""
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_off")),
			//language=json
			"""
			{
				"parent": "bigtech:block/beam_interceptor_transforms",
				"textures": {
					"particle": "bigtech:block/beam_interceptor",
					"detector": "bigtech:block/beam_interceptor"
				},
				"elements": [
					{
						"from": [  4, 0,  4 ],
						"to":   [ 12, 2, 12 ],
						"faces": {
							"up":    { "uv": [ 0, 0,  8,  8  ], "texture": "#detector"                     },
							"down":  { "uv": [ 0, 8,  8,  16 ], "texture": "#detector", "cullface": "down" },
							"north": { "uv": [ 8, 14, 16, 16 ], "texture": "#detector"                     },
							"south": { "uv": [ 8, 14, 16, 16 ], "texture": "#detector"                     },
							"east":  { "uv": [ 8, 14, 16, 16 ], "texture": "#detector"                     },
							"west":  { "uv": [ 8, 14, 16, 16 ], "texture": "#detector"                     }
						}
					},
					{
						"from": [ 7, 2, 7 ],
						"to":   [ 9, 7, 9 ],
						"faces": {
							"north": { "uv": [ 9, 9, 11, 14 ], "texture": "#detector" },
							"south": { "uv": [ 9, 9, 11, 14 ], "texture": "#detector" },
							"east":  { "uv": [ 9, 9, 11, 14 ], "texture": "#detector" },
							"west":  { "uv": [ 9, 9, 11, 14 ], "texture": "#detector" }
						}
					},
					{
						"from": [ 7, 7, 7 ],
						"to":   [ 9, 9, 9 ],
						"faces": {
							"up":    { "uv": [ 9, 7, 11, 9 ], "texture": "#detector", "tintindex": 0 },
							"north": { "uv": [ 9, 7, 11, 9 ], "texture": "#detector", "tintindex": 0 },
							"south": { "uv": [ 9, 7, 11, 9 ], "texture": "#detector", "tintindex": 0 },
							"east":  { "uv": [ 9, 7, 11, 9 ], "texture": "#detector", "tintindex": 0 },
							"west":  { "uv": [ 9, 7, 11, 9 ], "texture": "#detector", "tintindex": 0 }
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
				"parent": "bigtech:block/beam_interceptor_transforms",
				"textures": {
					"particle": "bigtech:block/beam_interceptor",
					"detector": "bigtech:block/beam_interceptor"
				},
				"elements": [
					{
						"from": [  4, 0,  4 ],
						"to":   [ 12, 2, 12 ],
						"faces": {
							"up":    { "uv": [ 0, 0,  8,  8  ], "texture": "#detector"                     },
							"down":  { "uv": [ 0, 8,  8,  16 ], "texture": "#detector", "cullface": "down" },
							"north": { "uv": [ 8, 14, 16, 16 ], "texture": "#detector"                     },
							"south": { "uv": [ 8, 14, 16, 16 ], "texture": "#detector"                     },
							"east":  { "uv": [ 8, 14, 16, 16 ], "texture": "#detector"                     },
							"west":  { "uv": [ 8, 14, 16, 16 ], "texture": "#detector"                     }
						}
					},
					{
						"from": [ 7, 2, 7 ],
						"to":   [ 9, 6, 9 ],
						"shade": false,
						"faces": {
							"north": { "uv": [ 13, 10, 15, 14 ], "texture": "#detector" },
							"south": { "uv": [ 13, 10, 15, 14 ], "texture": "#detector" },
							"east":  { "uv": [ 13, 10, 15, 14 ], "texture": "#detector" },
							"west":  { "uv": [ 13, 10, 15, 14 ], "texture": "#detector" }
						}
					},
					{
						"from": [  6,  6, 7 ],
						"to":   [ 10, 10, 9 ],
						"shade": false,
						"faces": {
							"north": { "uv": [ 12, 6, 16, 10 ], "texture": "#detector", "tintindex": 0 },
							"south": { "uv": [ 12, 6, 16, 10 ], "texture": "#detector", "tintindex": 0 }
						}
					},
					{
						"from": [ 7,  6,  6 ],
						"to":   [ 9, 10, 10 ],
						"shade": false,
						"faces": {
							"east": { "uv": [ 12, 6, 16, 10 ], "texture": "#detector", "tintindex": 0 },
							"west": { "uv": [ 12, 6, 16, 10 ], "texture": "#detector", "tintindex": 0 }
						}
					},
					{
						"from": [ 7, 9, 7 ],
						"to":   [ 9, 9, 9 ],
						"shade": false,
						"faces": {
							"up": { "uv": [ 13, 7, 15, 9 ], "texture": "#detector", "tintindex": 0 }
						}
					}
				]
			}"""
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_off");
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		context.writeToFile(
			context.itemDefinitionPath(this.getId()),
			context.replace(
				//language=json
				"""
				{
					"model": {
						"type": "minecraft:model",
						"model": "%PATH",
						"tints": [{
							"type": "bigtech:beam_interceptor"
						}]
					}
				}""",
				Map.of(
					"PATH", context.prefixPath("block/", this.getItemModelParent(context)).toString()
				)
			)
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {

	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {

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
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("l", "t", "b")
			.where('l', FunctionalItems.LENS)
			.where('t', Items.REDSTONE_TORCH)
			.where('b', Items.SMOOTH_STONE_SLAB)
			.result(this.getId())
			.toString()
		);
	}
}