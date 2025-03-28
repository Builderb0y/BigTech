package builderb0y.bigtech.datagen.impl.ascenders;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CommonAscenderDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechBlockTags.BELT_SUPPORT).add(BigTechBlockTags.ASCENDERS);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_ascender")),
			//language=json
			"""
			{
				"ambientocclusion": false,
				"parent": "minecraft:block/block",
				"textures": {
					"outer":      "bigtech:block/ascender_outer",
					"inner":      "bigtech:block/ascender_inner",
					"top_bottom": "bigtech:block/ascender_top_bottom",
					"particle":   "bigtech:block/ascender_outer"
				},
				"elements": [
					{
						"from": [ 0,  0, 0 ],
						"to":   [ 2, 16, 2 ],
						"faces": {
							"up":    { "uv": [  0,  0,  2,  2 ], "texture": "#top_bottom", "cullface": "up"    },
							"down":  { "uv": [  0, 14,  2, 16 ], "texture": "#top_bottom", "cullface": "down"  },
							"north": { "uv": [ 14,  0, 16, 16 ], "texture": "#outer",      "cullface": "north" },
							"east":  { "uv": [ 14,  0, 16, 16 ], "texture": "#inner"                           },
							"south": { "uv": [  0,  0,  2, 16 ], "texture": "#inner"                           },
							"west":  { "uv": [  0,  0,  2, 16 ], "texture": "#outer",      "cullface": "west"  }
						}
					},
					{
						"from": [ 14,  0, 0 ],
						"to":   [ 16, 16, 2 ],
						"faces": {
							"up":    { "uv": [ 14,  0, 16,  2 ], "texture": "#top_bottom", "cullface": "up"    },
							"down":  { "uv": [ 14, 14, 16, 16 ], "texture": "#top_bottom", "cullface": "down"  },
							"north": { "uv": [  0,  0,  2, 16 ], "texture": "#outer",      "cullface": "north" },
							"east":  { "uv": [ 14,  0, 16, 16 ], "texture": "#outer",      "cullface": "east"  },
							"south": { "uv": [ 14,  0, 16, 16 ], "texture": "#inner"                           },
							"west":  { "uv": [  0,  0,  2, 16 ], "texture": "#inner"                           }
						}
					},
					{
						"from": [ 14,  0, 14 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"up":    { "uv": [ 14, 14, 16, 16 ], "texture": "#top_bottom", "cullface": "up"    },
							"down":  { "uv": [ 14,  0, 16,  2 ], "texture": "#top_bottom", "cullface": "down"  },
							"north": { "uv": [  0,  0,  2, 16 ], "texture": "#inner"                           },
							"east":  { "uv": [  0,  0,  2, 16 ], "texture": "#outer",      "cullface": "east"  },
							"south": { "uv": [ 14,  0, 16, 16 ], "texture": "#outer",      "cullface": "south" },
							"west":  { "uv": [ 14,  0, 16, 16 ], "texture": "#inner"                           }
						}
					},
					{
						"from": [ 0,  0, 14 ],
						"to":   [ 2, 16, 16 ],
						"faces": {
							"up":    { "uv": [  0, 14,  2, 16 ], "texture": "#top_bottom", "cullface": "up"    },
							"down":  { "uv": [  0,  0,  2,  2 ], "texture": "#top_bottom", "cullface": "down"  },
							"north": { "uv": [ 14,  0, 16, 16 ], "texture": "#inner"                           },
							"east":  { "uv": [  0,  0,  2, 16 ], "texture": "#inner"                           },
							"south": { "uv": [  0,  0,  2, 16 ], "texture": "#outer",      "cullface": "south" },
							"west":  { "uv": [ 14,  0, 16, 16 ], "texture": "#outer",      "cullface": "west"  }
						}
					},
					{
						"from": [  1,  0,  1 ],
						"to":   [ 15, 16, 15 ],
						"faces": {
							"north": { "uv": [ 1, 0, 15, 16 ], "texture": "#translucent" },
							"east":  { "uv": [ 1, 0, 15, 16 ], "texture": "#translucent" },
							"south": { "uv": [ 1, 0, 15, 16 ], "texture": "#translucent" },
							"west":  { "uv": [ 1, 0, 15, 16 ], "texture": "#translucent" }
						}
					}
				]
			}"""
		);
	}
}