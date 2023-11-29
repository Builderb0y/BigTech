package builderb0y.bigtech.datagen.impl.catwalkStairs;

import java.util.List;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningLevelTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class CommonCatwalkStairsDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechBlockTags.CONDUCTS_LIGHTNING).add(BigTechBlockTags.METAL_CATWALK_STAIRS);
		context.getTags(BigTechBlockTags.SHOCKS_ENTITIES).add(BigTechBlockTags.METAL_CATWALK_STAIRS);
		context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.METAL_CATWALK_STAIRS);
		context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.METAL_CATWALK_STAIRS);
		context.getTags(MiningToolTags.AXE).add(BigTechBlockTags.WOODEN_CATWALK_STAIRS);
		context.getTags(BigTechBlockTags.CATWALK_STAIRS).addAll(List.of(
			new TagOrItem(BigTechBlockTags.WOODEN_CATWALK_STAIRS),
			new TagOrItem(BigTechBlockTags.METAL_CATWALK_STAIRS)
		));
		context.getTags(BigTechItemTags.CATWALK_STAIRS).addAll(List.of(
			new TagOrItem(BigTechItemTags.WOODEN_CATWALK_STAIRS),
			new TagOrItem(BigTechItemTags.METAL_CATWALK_STAIRS)
		));
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_catwalk_stairs_base")),
			//language=json
			"""
			{
				"textures": {
					"particle": "#stairs"
				},
				"elements": [
					{
						"from": [  0, 7,  8 ],
						"to":   [ 16, 8, 16 ],
						"faces": {
							"up":    { "uv": [  0,  8, 16, 16 ], "texture": "#base"                                       },
							"down":  { "uv": [  0, 16, 16,  8 ], "texture": "#base"                                       },
							"north": { "uv": [  0,  8, 16,  9 ], "texture": "#base", "rotation": 180                      },
							"east":  { "uv": [ 15,  8, 16, 16 ], "texture": "#base", "rotation":  90, "cullface": "east"  },
							"south": { "uv": [  0, 15, 16, 16 ], "texture": "#base",                  "cullface": "south" },
							"west":  { "uv": [  0,  8,  1, 16 ], "texture": "#base", "rotation": 270, "cullface": "west"  }
						}
					},
					{
						"from": [  0, 15, 0 ],
						"to":   [ 16, 16, 8 ],
						"faces": {
							"up":    { "uv": [  0, 0, 16, 8 ], "texture": "#base"                                       },
							"down":  { "uv": [  0, 8, 16, 0 ], "texture": "#base"                                       },
							"north": { "uv": [  0, 0, 16, 1 ], "texture": "#base", "rotation": 180, "cullface": "north" },
							"east":  { "uv": [ 15, 0, 16, 8 ], "texture": "#base", "rotation":  90, "cullface": "east"  },
							"south": { "uv": [  0, 7, 16, 8 ], "texture": "#base"                                       },
							"west":  { "uv": [  0, 0,  1, 8 ], "texture": "#base", "rotation": 270, "cullface": "west"  }
						}
					},
					{
						"from": [  6, 7,  0 ],
						"to":   [ 10, 8, 16 ],
						"rotation": {
							"origin": [ 8, 7, 8 ],
							"axis": "x",
							"angle": 45,
							"rescale": true
						},
						"faces": {
							"up":    { "uv": [  0, 0, 16, 4 ], "texture": "#stairs", "rotation": 270  },
							"down":  { "uv": [ 16, 0,  0, 4 ], "texture": "#stairs", "rotation": 270  },
							"north": { "uv": [ 15, 0, 16, 4 ], "texture": "#stairs", "rotation": 90   },
							"east":  { "uv": [  0, 3, 16, 4 ], "texture": "#stairs"                   },
							"south": { "uv": [  0, 0,  1, 4 ], "texture": "#stairs", "rotation": 270  },
							"west":  { "uv": [  0, 0, 16, 1 ], "texture": "#stairs", "rotation": 180  }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_catwalk_stairs_rail_left")),
			//language=json
			"""
			{
				"textures": {
					"particle": "#stairs"
				},
				"elements": [
					{
						"from": [ 0,  8,  8 ],
						"to":   [ 1, 20, 16 ],
						"faces": {
							"east":  { "uv": [ 0, 4, 8, 16 ], "texture": "#stairs"                                       },
							"south": { "uv": [ 0, 4, 1, 16 ], "texture": "#stairs", "cullface": "south"                  },
							"west":  { "uv": [ 8, 4, 0, 16 ], "texture": "#stairs"                                       },
							"up":    { "uv": [ 0, 4, 8,  5 ], "texture": "#stairs",                      "rotation": 270 }
						}
					},
					{
						"from": [ 0, 16, 0 ],
						"to":   [ 1, 28, 8 ],
						"faces": {
							"north": { "uv": [ 15, 4, 16, 16 ], "texture": "#stairs", "cullface": "north"                  },
							"east":  { "uv": [  8, 4, 16, 16 ], "texture": "#stairs"                                       },
							"south": { "uv": [  8, 4,  9, 16 ], "texture": "#stairs"                                       },
							"west":  { "uv": [ 16, 4,  8, 16 ], "texture": "#stairs"                                       },
							"up":    { "uv": [  8, 4, 16,  5 ], "texture": "#stairs",                      "rotation": 270 }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_catwalk_stairs_rail_right")),
			//language=json
			"""
			{
				"textures": {
					"particle": "#stairs"
				},
				"elements": [
					{
						"from": [ 15,  8,  8 ],
						"to":   [ 16, 20, 16 ],
						"faces": {
							"east":  { "uv": [ 0, 4, 8, 16 ], "texture": "#stairs"                                       },
							"south": { "uv": [ 0, 4, 1, 16 ], "texture": "#stairs", "cullface": "south"                  },
							"west":  { "uv": [ 8, 4, 0, 16 ], "texture": "#stairs"                                       },
							"up":    { "uv": [ 0, 4, 8,  5 ], "texture": "#stairs",                      "rotation": 270 }
						}
					},
					{
						"from": [ 15, 16, 0 ],
						"to":   [ 16, 28, 8 ],
						"faces": {
							"north": { "uv": [ 15, 4, 16, 16 ], "texture": "#stairs", "cullface": "north"                  },
							"east":  { "uv": [  8, 4, 16, 16 ], "texture": "#stairs"                                       },
							"south": { "uv": [  8, 4,  9, 16 ], "texture": "#stairs"                                       },
							"west":  { "uv": [ 16, 4,  8, 16 ], "texture": "#stairs"                                       },
							"up":    { "uv": [  8, 4, 16,  5 ], "texture": "#stairs",                      "rotation": 270 }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.itemModelPath(BigTechMod.modID("template_catwalk_stairs")),
			//language=json
			"""
			{
				"parent": "block/block",
				"elements": [
					{
						"from": [  0, 7,  8 ],
						"to":   [ 16, 8, 16 ],
						"faces": {
							"north": { "uv": [  0,  8, 16,  9 ], "texture": "#base", "rotation": 180 },
							"east":  { "uv": [ 15,  8, 16, 16 ], "texture": "#base", "rotation":  90 },
							"south": { "uv": [  0, 15, 16, 16 ], "texture": "#base"                  },
							"west":  { "uv": [  0,  8,  1, 16 ], "texture": "#base", "rotation": 270 },
							"up":    { "uv": [  0,  8, 16, 16 ], "texture": "#base"                  },
							"down":  { "uv": [  0, 16, 16,  8 ], "texture": "#base"                  }
						}
					},
					{
						"from": [  0, 15, 0 ],
						"to":   [ 16, 16, 8 ],
						"faces": {
							"north": { "uv": [  0, 0, 16, 1 ], "texture": "#base", "rotation": 180 },
							"east":  { "uv": [ 15, 0, 16, 8 ], "texture": "#base", "rotation":  90 },
							"south": { "uv": [  0, 7, 16, 8 ], "texture": "#base"                  },
							"west":  { "uv": [  0, 0,  1, 8 ], "texture": "#base", "rotation": 270 },
							"up":    { "uv": [  0, 0, 16, 8 ], "texture": "#base"                  },
							"down":  { "uv": [  0, 8, 16, 0 ], "texture": "#base"                  }
						}
					},
					{
						"from": [  6, 7,  0 ],
						"to":   [ 10, 8, 16 ],
						"rotation": {
							"origin": [ 8, 7, 8 ],
							"axis": "x",
							"angle": 45,
							"rescale": true
						},
						"faces": {
							"north": { "uv": [  0, 0,  1, 4 ], "texture": "#stairs", "rotation": 270 },
							"east":  { "uv": [  0, 0, 16, 1 ], "texture": "#stairs", "rotation": 180 },
							"south": { "uv": [ 15, 0, 16, 4 ], "texture": "#stairs", "rotation":  90 },
							"west":  { "uv": [  0, 3, 16, 4 ], "texture": "#stairs"                  },
							"up":    { "uv": [  0, 0, 16, 4 ], "texture": "#stairs", "rotation":  90 },
							"down":  { "uv": [ 16, 0,  0, 4 ], "texture": "#stairs", "rotation":  90 }
						}
					},
					{
						"from": [ 0,  8,  8 ],
						"to":   [ 1, 20, 16 ],
						"faces": {
							"east":  { "uv": [ 0, 4, 8, 16 ], "texture": "#stairs"                  },
							"south": { "uv": [ 0, 4, 1, 16 ], "texture": "#stairs"                  },
							"west":  { "uv": [ 8, 4, 0, 16 ], "texture": "#stairs"                  },
							"up":    { "uv": [ 0, 4, 8,  5 ], "texture": "#stairs", "rotation": 270 }
						}
					},
					{
						"from": [ 0, 16, 0 ],
						"to":   [ 1, 28, 8 ],
						"faces": {
							"north": { "uv": [ 15, 4, 16, 16 ], "texture": "#stairs"                  },
							"east":  { "uv": [  8, 4, 16, 16 ], "texture": "#stairs"                  },
							"south": { "uv": [  8, 4,  9, 16 ], "texture": "#stairs"                  },
							"west":  { "uv": [ 16, 4,  8, 16 ], "texture": "#stairs"                  },
							"up":    { "uv": [  8, 4, 16,  5 ], "texture": "#stairs", "rotation": 270 }
						}
					},
					{
						"from": [ 15,  8,  8 ],
						"to":   [ 16, 20, 16 ],
						"faces": {
							"east":  { "uv": [ 0, 4, 8, 16 ], "texture": "#stairs"                  },
							"south": { "uv": [ 0, 4, 1, 16 ], "texture": "#stairs"                  },
							"west":  { "uv": [ 8, 4, 0, 16 ], "texture": "#stairs"                  },
							"up":    { "uv": [ 0, 4, 8,  5 ], "texture": "#stairs", "rotation": 270 }
						}
					},
					{
						"from": [ 15, 16, 0 ],
						"to":   [ 16, 28, 8 ],
						"faces": {
							"north": { "uv": [ 15, 4, 16, 16 ], "texture": "#stairs"                  },
							"east":  { "uv": [  8, 4, 16, 16 ], "texture": "#stairs"                  },
							"south": { "uv": [  8, 4,  9, 16 ], "texture": "#stairs"                  },
							"west":  { "uv": [ 16, 4,  8, 16 ], "texture": "#stairs"                  },
							"up":    { "uv": [  8, 4, 16,  5 ], "texture": "#stairs", "rotation": 270 }
						}
					}
				],
				"display": {
					"gui": {
						"rotation":    [ 30.0, 315.0,   0.0   ],
						"translation": [  0.0,  -2.0,   0.0   ],
						"scale":       [  0.4,   0.4,   0.4   ]
					},
					"ground": {
						"rotation":    [  0.0,   0.0,   0.0   ],
						"translation": [  0.0,   3.0,   0.0   ],
						"scale":       [  0.25,  0.25,  0.25  ]
					},
					"fixed": {
						"rotation":    [  0.0,   0.0,   0.0   ],
						"translation": [  0.0,   0.0,   0.0   ],
						"scale":       [  0.5,   0.5,   0.5   ]
					},
					"thirdperson_righthand": {
						"rotation":    [ 75.0,   0.0,   0.0   ],
						"translation": [  0.0,   2.5,   0.0   ],
						"scale":       [  0.375, 0.375, 0.375 ]
					},
					"firstperson_righthand": {
						"rotation":    [  0.0, -15.0,   0.0   ],
						"translation": [  0.0,   0.0,   0.0   ],
						"scale":       [  0.4,   0.4,   0.4   ]
					},
					"firstperson_lefthand": {
						"rotation":    [  0.0, -15.0,   0.0   ],
						"translation": [  0.0,   0.0,   0.0   ],
						"scale":       [  0.4,   0.4,   0.4   ]
					}
				}
			}"""
		);
	}
}