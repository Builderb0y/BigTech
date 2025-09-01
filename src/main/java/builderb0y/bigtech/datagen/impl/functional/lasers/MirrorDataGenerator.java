package builderb0y.bigtech.datagen.impl.functional.lasers;

import java.util.Map;
import java.util.Objects;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;

public class MirrorDataGenerator extends BasicBlockDataGenerator {

	public MirrorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			"bigtech:block/mirror_${state.get(Properties.ATTACHED) ? \"attached\" : \"unattached\"}_${state.get(builderb0y.bigtech.blocks.BigTechProperties.ROTATION_0_7)}",
			BlockStateJsonVariant.xFromDown(state.get(Properties.FACING)),
			Objects.requireNonNullElse(BlockStateJsonVariant.yFromNorth(state.get(Properties.FACING)), 0)
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		record Rotation(int index, float degrees) {}
		for (Rotation rotation : new Rotation[] {
			new Rotation(0,   0.0F),
			new Rotation(1,  22.5F),
			new Rotation(2,  45.0F),
			new Rotation(7, -22.5F)
		}) {
			final String unattached = (
				//language=json
				"""
				{
					"parent": "block/block",
					"textures": {
						"particle": "bigtech:block/mirror",
						"mirror": "bigtech:block/mirror"
					},
					"elements": [
						{
							"from": [  2,  4, 7.5 ],
							"to":   [ 14, 12, 8.5 ],
							"rotation": {
								"origin": [ 8, 8, 8 ],
								"axis": "y",
								"angle": %ANGLE,
								"rescale": false
							},
							"faces": {
								"up":    { "uv": [  2,  4, 14,  5 ], "texture": "#mirror" },
								"down":  { "uv": [  2, 11, 14, 12 ], "texture": "#mirror" },
								"north": { "uv": [ 14,  4,  2, 12 ], "texture": "#mirror" },
								"south": { "uv": [  2,  4, 14, 12 ], "texture": "#mirror" },
								"east":  { "uv": [ 13,  4, 14, 12 ], "texture": "#mirror" },
								"west":  { "uv": [  2,  4,  3, 12 ], "texture": "#mirror" }
							}
						}%LEGS
					]
				}"""
			);
			final String legs = (
				//language=json
				"""
				,
						{
							"from": [ 5, 0, 7.5 ],
							"to":   [ 6, 4, 8.5 ],
							"rotation": {
								"origin": [ 8, 8, 8 ],
								"axis": "y",
								"angle": %ANGLE,
								"rescale": false
							},
							"faces": {
								"down":  { "uv": [ 5, 12, 6, 13 ], "texture": "#mirror", "cullface": "down" },
								"north": { "uv": [ 5, 12, 6, 16 ], "texture": "#mirror"                     },
								"south": { "uv": [ 5, 12, 6, 16 ], "texture": "#mirror"                     },
								"east":  { "uv": [ 5, 12, 6, 16 ], "texture": "#mirror"                     },
								"west":  { "uv": [ 5, 12, 6, 16 ], "texture": "#mirror"                     }
							}
						},
						{
							"from": [ 10, 0, 7.5 ],
							"to":   [ 11, 4, 8.5 ],
							"rotation": {
								"origin": [ 8, 8, 8 ],
								"axis": "y",
								"angle": %ANGLE,
								"rescale": false
							},
							"faces": {
								"down":  { "uv": [ 10, 12, 11, 13 ], "texture": "#mirror", "cullface": "down" },
								"north": { "uv": [ 10, 12, 11, 16 ], "texture": "#mirror"                     },
								"south": { "uv": [ 10, 12, 11, 16 ], "texture": "#mirror"                     },
								"east":  { "uv": [ 10, 12, 11, 16 ], "texture": "#mirror"                     },
								"west":  { "uv": [ 10, 12, 11, 16 ], "texture": "#mirror"                     }
							}
						}"""
			);
			context.writeToFile(
				context.blockModelPath(context.suffixPath(this.getId(), "_unattached_${rotation.index}")),
				context.replace(
					unattached,
					Map.of(
						"ANGLE", Float.toString(rotation.degrees),
						"LEGS", ""
					)
				)
			);
			context.writeToFile(
				context.blockModelPath(context.suffixPath(this.getId(), "_attached_${rotation.index}")),
				context.replace(
					unattached,
					Map.of(
						"ANGLE", Float.toString(rotation.degrees),
						"LEGS", context.replace(
							legs,
							Map.of(
								"ANGLE",
								Float.toString(rotation.degrees)
							)
						)
					)
				)
			);
		}
		for (Rotation rotation : new Rotation[] {
			new Rotation(3, -22.5F),
			new Rotation(4,   0.0F),
			new Rotation(5,  22.5F),
			new Rotation(6,  45.0F)
		}) {
			final String unattached = (
				//language=json
				"""
				{
					"parent": "block/block",
					"textures": {
						"particle": "bigtech:block/mirror",
						"mirror": "bigtech:block/mirror"
					},
					"elements": [
						{
							"from": [ 7.5,  4,  2 ],
							"to":   [ 8.5, 12, 14 ],
							"rotation": {
								"origin": [ 8, 8, 8 ],
								"axis": "y",
								"angle": %ANGLE,
								"rescale": false
							},
							"faces": {
								"up":    { "uv": [  2,  4, 14,  5 ], "texture": "#mirror", "rotation":  90 },
								"down":  { "uv": [  2, 11, 14, 12 ], "texture": "#mirror", "rotation": 270 },
								"north": { "uv": [  2,  4,  3, 12 ], "texture": "#mirror"                  },
								"south": { "uv": [ 13,  4, 14, 12 ], "texture": "#mirror"                  },
								"east":  { "uv": [ 14,  4,  2, 12 ], "texture": "#mirror"                  },
								"west":  { "uv": [  2,  4, 14, 12 ], "texture": "#mirror"                  }
							}
						}%LEGS
					]
				}"""
			);
			final String legs = (
				//language=json
				"""
				,
						{
							"from": [ 7.5, 0, 5 ],
							"to":   [ 8.5, 4, 6 ],
							"rotation": {
								"origin": [ 8, 8, 8 ],
								"axis": "y",
								"angle": %ANGLE,
								"rescale": false
							},
							"faces": {
								"down":  { "uv": [ 5, 12, 6, 13 ], "texture": "#mirror", "cullface": "down", "rotation": 270 },
								"north": { "uv": [ 5, 12, 6, 16 ], "texture": "#mirror"                                      },
								"south": { "uv": [ 5, 12, 6, 16 ], "texture": "#mirror"                                      },
								"east":  { "uv": [ 5, 12, 6, 16 ], "texture": "#mirror"                                      },
								"west":  { "uv": [ 5, 12, 6, 16 ], "texture": "#mirror"                                      }
							}
						},
						{
							"from": [ 7.5, 0, 10 ],
							"to":   [ 8.5, 4, 11 ],
							"rotation": {
								"origin": [ 8, 8, 8 ],
								"axis": "y",
								"angle": %ANGLE,
								"rescale": false
							},
							"faces": {
								"down":  { "uv": [ 10, 12, 11, 13 ], "texture": "#mirror", "cullface": "down", "rotation": 270 },
								"north": { "uv": [ 10, 12, 11, 16 ], "texture": "#mirror"                                      },
								"south": { "uv": [ 10, 12, 11, 16 ], "texture": "#mirror"                                      },
								"east":  { "uv": [ 10, 12, 11, 16 ], "texture": "#mirror"                                      },
								"west":  { "uv": [ 10, 12, 11, 16 ], "texture": "#mirror"                                      }
							}
						}"""
			);
			context.writeToFile(
				context.blockModelPath(context.suffixPath(this.getId(), "_unattached_${rotation.index}")),
				context.replace(
					unattached,
					Map.of(
						"ANGLE", Float.toString(rotation.degrees),
						"LEGS", ""
					)
				)
			);
			context.writeToFile(
				context.blockModelPath(context.suffixPath(this.getId(), "_attached_${rotation.index}")),
				context.replace(
					unattached,
					Map.of(
						"ANGLE", Float.toString(rotation.degrees),
						"LEGS", context.replace(
							legs,
							Map.of(
								"ANGLE",
								Float.toString(rotation.degrees)
							)
						)
					)
				)
			);
		}
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_attached_4");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
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
			.pattern("ggg", "ggg", " i ")
			.where('g', ConventionalItemTags.GLASS_PANES)
			.where('i', BigTechItemTags.SILVER_INGOTS)
			.result(this.getId())
			.count(6)
			.toString()
		);
	}
}