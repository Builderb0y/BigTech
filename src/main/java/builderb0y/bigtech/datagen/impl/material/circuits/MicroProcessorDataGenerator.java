package builderb0y.bigtech.datagen.impl.material.circuits;

import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.util.BigTechMath;

public class MicroProcessorDataGenerator extends BasicBlockDataGenerator {

	public MicroProcessorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put("block.bigtech.milli_processor", "Milli Processor");
		context.lang.put("block.bigtech.processor", "Processor");
		context.lang.put("block.bigtech.kilo_processor", "Kilo Processor");
		context.lang.put("block.bigtech.mega_processor", "Mega Processor");
		context.lang.put("block.bigtech.giga_processor", "Giga Processor");
		context.lang.put("block.bigtech.tera_processor", "Tera Processor");
		context.lang.put("block.bigtech.peta_processor", "Peta Processor");
		context.lang.put("block.bigtech.over_processor", "Please stop now");
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
						"type": "minecraft:select",
						"property": "bigtech:circuit_rotation",
						"cases": [
							{
								"when": "default",
								"model": {
									"type": "minecraft:model",
									"model": "%PATH_0"
								}
							},
							{
								"when": "rotate_right",
								"model": {
									"type": "minecraft:model",
									"model": "%PATH_90"
								}
							},
							{
								"when": "rotate_180",
								"model": {
									"type": "minecraft:model",
									"model": "%PATH_180"
								}
							},
							{
								"when": "rotate_left",
								"model": {
									"type": "minecraft:model",
									"model": "%PATH_270"
								}
							}
						],
						"fallback": {
							"type": "minecraft:model",
							"model": "%PATH_0"
						}
					}
				}""",
				Map.of("PATH", context.prefixPath("item/", this.getId()).toString())
			)
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		for (int rotation = 0; rotation < 360; rotation += 90) {
			context.writeToFile(
				context.itemModelPath(context.suffixPath(this.getId(), "_" + rotation)),
				context.replace(
					//language=json
					"""
					{
						"parent": "minecraft:item/generated",
						"textures": {
							"layer0": "%ID"
						},
						"display": {
							"gui": {
								"rotation":    [ 0, 0, %ROT ],
								"translation": [ 0, 0, 0 ],
								"scale":       [ 1, 1, 1 ]
							}
						}
					}""",
					Map.of(
						"ID", context.prefixPath("item/", this.getId()).toString(),
						"ROT", Integer.toString(BigTechMath.modulus_BP(-rotation, 360))
					)
				)
			);
		}
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixPath("block/", this.getId()).toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"credit": "Made with Blockbench",
				"parent": "minecraft:block/thin_block",
				"ambientocclusion": false,
				"textures": {
					"chip": "bigtech:block/micro_processor",
					"slab": "minecraft:block/smooth_stone",
					"particle": "bigtech:block/micro_processor"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 2, 16 ],
						"faces": {
							"north": { "uv": [ 0, 14, 16, 16 ], "texture": "#slab" },
							"east":  { "uv": [ 0, 14, 16, 16 ], "texture": "#slab" },
							"south": { "uv": [ 0, 14, 16, 16 ], "texture": "#slab" },
							"west":  { "uv": [ 0, 14, 16, 16 ], "texture": "#slab" },
							"up":    { "uv": [ 0,  0, 16, 16 ], "texture": "#chip" },
							"down":  { "uv": [ 0,  0, 16, 16 ], "texture": "#slab" }
						}
					},
					{
						"from": [  3, 2,  3 ],
						"to":   [ 13, 3, 13 ],
						"faces": {
							"north": { "uv": [  3,  3, 13,  4 ], "texture": "#chip", "rotation": 180 },
							"east":  { "uv": [ 12,  3, 13, 13 ], "texture": "#chip", "rotation":  90 },
							"south": { "uv": [  3, 12, 13, 13 ], "texture": "#chip"                  },
							"west":  { "uv": [  3,  3,  4, 13 ], "texture": "#chip", "rotation": 270 },
							"up":    { "uv": [  3,  3, 13, 13 ], "texture": "#chip"                  }
						}
					},
					{
						"from": [ 4, 2, 2 ],
						"to":   [ 5, 3, 3 ],
						"rotation": { "angle": -45, "axis": "x", "origin": [ 4, 3, 3 ], "rescale": true },
						"faces": {
							"east": { "uv": [ 4, 2, 5, 3 ], "texture": "#chip" },
							"west": { "uv": [ 4, 2, 5, 3 ], "texture": "#chip" },
							"up":   { "uv": [ 4, 2, 5, 3 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 6, 2, 2 ],
						"to":   [ 7, 3, 3 ],
						"rotation": { "angle": -45, "axis": "x", "origin": [ 6, 3, 3 ], "rescale": true },
						"faces": {
							"east": { "uv": [ 6, 2, 7, 3 ], "texture": "#chip" },
							"west": { "uv": [ 6, 2, 7, 3 ], "texture": "#chip" },
							"up":   { "uv": [ 6, 2, 7, 3 ], "texture": "#chip" }
						}
					},
					{
						"from": [  9, 2, 2 ],
						"to":   [ 10, 3, 3 ],
						"rotation": { "angle": -45, "axis": "x", "origin": [ 9, 3, 3 ], "rescale": true },
						"faces": {
							"east": { "uv": [ 9, 2, 10, 3 ], "texture": "#chip" },
							"west": { "uv": [ 9, 2, 10, 3 ], "texture": "#chip" },
							"up":   { "uv": [ 9, 2, 10, 3 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 11, 2, 2 ],
						"to":   [ 12, 3, 3 ],
						"rotation": { "angle": -45, "axis": "x", "origin": [ 11, 3, 3 ], "rescale": true },
						"faces": {
							"east": { "uv": [ 11, 2, 12, 3 ], "texture": "#chip" },
							"west": { "uv": [ 11, 2, 12, 3 ], "texture": "#chip" },
							"up":   { "uv": [ 11, 2, 12, 3 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 13, 2, 4 ],
						"to":   [ 14, 3, 5 ],
						"rotation": { "angle": -45, "axis": "z", "origin": [ 13, 3, 3 ], "rescale": true },
						"faces": {
							"north": { "uv": [ 13, 4, 14, 5 ], "texture": "#chip" },
							"south": { "uv": [ 13, 4, 14, 5 ], "texture": "#chip" },
							"up":    { "uv": [ 13, 4, 14, 5 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 13, 2, 6 ],
						"to":   [ 14, 3, 7 ],
						"rotation": { "angle": -45, "axis": "z", "origin": [ 13, 3, 5 ], "rescale": true },
						"faces": {
							"north": { "uv": [ 13, 6, 14, 7 ], "texture": "#chip" },
							"south": { "uv": [ 13, 6, 14, 7 ], "texture": "#chip" },
							"up":    { "uv": [ 13, 6, 14, 7 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 13, 2,  9 ],
						"to":   [ 14, 3, 10 ],
						"rotation": { "angle": -45, "axis": "z", "origin": [ 13, 3, 8 ], "rescale": true },
						"faces": {
							"north": { "uv": [ 13, 9, 14, 10 ], "texture": "#chip" },
							"south": { "uv": [ 13, 9, 14, 10 ], "texture": "#chip" },
							"up":    { "uv": [ 13, 9, 14, 10 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 13, 2, 11 ],
						"to":   [ 14, 3, 12 ],
						"rotation": { "angle": -45, "axis": "z", "origin": [ 13, 3, 10 ], "rescale": true },
						"faces": {
							"north": { "uv": [ 13, 11, 14, 12 ], "texture": "#chip" },
							"south": { "uv": [ 13, 11, 14, 12 ], "texture": "#chip" },
							"up":    { "uv": [ 13, 11, 14, 12 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 11, 2, 13 ],
						"to":   [ 12, 3, 14 ],
						"rotation": { "angle": 45, "axis": "x", "origin": [ 10, 3, 13 ], "rescale": true },
						"faces": {
							"east": { "uv": [ 11, 13, 12, 14 ], "texture": "#chip" },
							"west": { "uv": [ 11, 13, 12, 14 ], "texture": "#chip" },
							"up":   { "uv": [ 11, 13, 12, 14 ], "texture": "#chip" }
						}
					},
					{
						"from": [  9, 2, 13 ],
						"to":   [ 10, 3, 14 ],
						"rotation": { "angle": 45, "axis": "x", "origin": [ 8, 3, 13 ], "rescale": true },
						"faces": {
							"east": { "uv": [ 9, 13, 10, 14 ], "texture": "#chip" },
							"west": { "uv": [ 9, 13, 10, 14 ], "texture": "#chip" },
							"up":   { "uv": [ 9, 13, 10, 14 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 6, 2, 13 ],
						"to":   [ 7, 3, 14 ],
						"rotation": { "angle": 45, "axis": "x", "origin": [ 5, 3, 13 ], "rescale": true },
						"faces": {
							"east": { "uv": [ 6, 13, 7, 14 ], "texture": "#chip" },
							"west": { "uv": [ 6, 13, 7, 14 ], "texture": "#chip" },
							"up":   { "uv": [ 6, 13, 7, 14 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 4, 2, 13 ],
						"to":   [ 5, 3, 14 ],
						"rotation": { "angle": 45, "axis": "x", "origin": [ 3, 3, 13 ], "rescale": true },
						"faces": {
							"east": { "uv": [ 4, 13, 5, 14 ], "texture": "#chip" },
							"west": { "uv": [ 4, 13, 5, 14 ], "texture": "#chip" },
							"up":   { "uv": [ 4, 13, 5, 14 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 2, 2, 11 ],
						"to":   [ 3, 3, 12 ],
						"rotation": { "angle": 45, "axis": "z", "origin": [ 3, 3, 10 ], "rescale": true },
						"faces": {
							"north": { "uv": [ 2, 11, 3, 12 ], "texture": "#chip" },
							"south": { "uv": [ 2, 11, 3, 12 ], "texture": "#chip" },
							"up":    { "uv": [ 2, 11, 3, 12 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 2, 2,  9 ],
						"to":   [ 3, 3, 10 ],
						"rotation": { "angle": 45, "axis": "z", "origin": [ 3, 3, 8 ], "rescale": true },
						"faces": {
							"north": { "uv": [ 2, 9, 3, 10 ], "texture": "#chip" },
							"south": { "uv": [ 2, 9, 3, 10 ], "texture": "#chip" },
							"up":    { "uv": [ 2, 9, 3, 10 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 2, 2, 6 ],
						"to":   [ 3, 3, 7 ],
						"rotation": { "angle": 45, "axis": "z", "origin": [ 3, 3, 5 ], "rescale": true },
						"faces": {
							"north": { "uv": [ 2, 6, 3, 7 ], "texture": "#chip" },
							"south": { "uv": [ 2, 6, 3, 7 ], "texture": "#chip" },
							"up":    { "uv": [ 2, 6, 3, 7 ], "texture": "#chip" }
						}
					},
					{
						"from": [ 2, 2, 4 ],
						"to":   [ 3, 3, 5 ],
						"rotation": { "angle": 45, "axis": "z", "origin": [ 3, 3, 3 ], "rescale": true },
						"faces": {
							"north": { "uv": [ 2, 4, 3, 5 ], "texture": "#chip" },
							"south": { "uv": [ 2, 4, 3, 5 ], "texture": "#chip" },
							"up":    { "uv": [ 2, 4, 3, 5 ], "texture": "#chip" }
						}
					}
				],
				"groups": [
					0,
					1,
					{
						"name": "north",
						"origin": [ 8, 8, 8 ],
						"color": 0,
						"children": [ 2, 3, 4, 5 ]
					},
					{
						"name": "east",
						"origin": [ 8, 8, 8 ],
						"color": 0,
						"children": [ 6, 7, 8, 9 ]
					},
					{
						"name": "south",
						"origin": [ 8, 8, 8 ],
						"color": 0,
						"children": [ 10, 11, 12, 13 ]
					},
					{
						"name": "west",
						"origin": [ 8, 8, 8 ],
						"color": 0,
						"children": [ 14, 15, 16, 17 ]
					}
				]
			}"""
		);
	}

	@Override
	public void writeLootTableJson(DataGenContext context) {
		this.writeBlockEntityComponentCopyingLootTableJson(context, DataComponentTypes.ITEM_NAME, BigTechDataComponents.CIRCUIT);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//hand is fine.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//empty hand is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		//made in assembler, not crafting table.
	}
}