package builderb0y.bigtech.datagen.impl.catwalkPlatforms;

import java.util.List;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningLevelTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class CommonCatwalkPlatformDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechBlockTags.CONDUCTS_LIGHTNING).add(BigTechBlockTags.METAL_CATWALK_PLATFORMS);
		context.getTags(BigTechBlockTags.SHOCKS_ENTITIES).add(BigTechBlockTags.METAL_CATWALK_PLATFORMS);
		context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.METAL_CATWALK_PLATFORMS);
		context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.METAL_CATWALK_PLATFORMS);
		context.getTags(MiningToolTags.AXE).add(BigTechBlockTags.WOODEN_CATWALK_PLATFORMS);
		context.getTags(BigTechBlockTags.METAL_CATWALK_PLATFORMS).add(BigTechBlockTags.COPPER_CATWALK_PLATFORMS);
		context.getTags(BigTechItemTags.METAL_CATWALK_PLATFORMS).add(BigTechItemTags.COPPER_CATWALK_PLATFORMS);
		context.getTags(BigTechBlockTags.CATWALK_PLATFORMS).addAll(List.of(
			new TagOrItem(BigTechBlockTags.WOODEN_CATWALK_PLATFORMS),
			new TagOrItem(BigTechBlockTags.METAL_CATWALK_PLATFORMS)
		));
		context.getTags(BigTechItemTags.CATWALK_PLATFORMS).addAll(List.of(
			new TagOrItem(BigTechItemTags.WOODEN_CATWALK_PLATFORMS),
			new TagOrItem(BigTechItemTags.METAL_CATWALK_PLATFORMS)
		));
		context.getTags(BigTechBlockTags.WOODEN_CATWALK_PLATFORMS);
		context.getTags(BigTechItemTags.WOODEN_CATWALK_PLATFORMS);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_catwalk_platform_base")),
			//language=json
			"""
			{
				"parent": "block/block",
				"textures": {
					"particle": "#base"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 1, 16 ],
						"faces": {
							"up":    { "uv": [  0,  0, 16, 16 ], "texture": "#base"                                       },
							"down":  { "uv": [  0,  0, 16, 16 ], "texture": "#base", "cullface": "down"                   },
							"north": { "uv": [  0,  0, 16,  1 ], "texture": "#base", "cullface": "north", "rotation": 180 },
							"south": { "uv": [  0, 15, 16, 16 ], "texture": "#base", "cullface": "south"                  },
							"east":  { "uv": [ 15,  0, 16, 16 ], "texture": "#base", "cullface": "east",  "rotation":  90 },
							"west":  { "uv": [  0,  0,  1, 16 ], "texture": "#base", "cullface": "west",  "rotation": 270 }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_catwalk_platform_rail")),
			//language=json
			"""
			{
				"parent": "block/block",
				"textures": {
					"particle": "#rail"
				},
				"elements": [
					{
						"from": [  0,  1, 0 ],
						"to":   [ 16, 12, 1 ],
						"faces": {
							"up":    { "uv": [ 0, 4, 16,  5 ], "texture": "#rail"                      },
							"north": { "uv": [ 0, 4, 16, 15 ], "texture": "#rail", "cullface": "north" },
							"south": { "uv": [ 0, 4, 16, 15 ], "texture": "#rail"                      }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.itemModelPath(BigTechMod.modID("template_catwalk_platform")),
			//language=json
			"""
			{
				"parent": "block/block",
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 1, 16 ],
						"faces": {
							"up":    { "uv": [  0,  0, 16, 16 ], "texture": "#base"                  },
							"down":  { "uv": [  0,  0, 16, 16 ], "texture": "#base"                  },
							"north": { "uv": [  0,  0, 16,  1 ], "texture": "#base", "rotation": 180 },
							"south": { "uv": [  0, 15, 16, 16 ], "texture": "#base"                  },
							"east":  { "uv": [ 15,  0, 16, 16 ], "texture": "#base", "rotation":  90 },
							"west":  { "uv": [  0,  0,  1, 16 ], "texture": "#base", "rotation": 270 }
						}
					},
					{
						"from": [  0,  1, 0 ],
						"to":   [ 16, 12, 1 ],
						"faces": {
							"up":    { "uv": [ 0, 4, 16,  5 ], "texture": "#rail" },
							"north": { "uv": [ 0, 4, 16, 15 ], "texture": "#rail" },
							"south": { "uv": [ 0, 4, 16, 15 ], "texture": "#rail" }
						}
					},
					{
						"from": [  0,  1, 15 ],
						"to":   [ 16, 12, 16 ],
						"faces": {
							"up":    { "uv": [ 0, 4, 16,  5 ], "texture": "#rail" },
							"north": { "uv": [ 0, 4, 16, 15 ], "texture": "#rail" },
							"south": { "uv": [ 0, 4, 16, 15 ], "texture": "#rail" }
						}
					},
					{
						"from": [ 0,  1,  0 ],
						"to":   [ 1, 12, 16 ],
						"faces": {
							"up":    { "uv": [ 0, 4, 16,  5 ], "texture": "#rail", "rotation": 90 },
							"east":  { "uv": [ 0, 4, 16, 15 ], "texture": "#rail"                 },
							"west":  { "uv": [ 0, 4, 16, 15 ], "texture": "#rail"                 }
						}
					},
					{
						"from": [ 15,  1,  0 ],
						"to":   [ 16, 12, 16 ],
						"faces": {
							"up":    { "uv": [ 0, 4, 16,  5 ], "texture": "#rail", "rotation": 90 },
							"east":  { "uv": [ 0, 4, 16, 15 ], "texture": "#rail"                 },
							"west":  { "uv": [ 0, 4, 16, 15 ], "texture": "#rail"                 }
						}
					}
				],
				"display": {
					"firstperson_righthand": {
						"rotation":    [ 0.0,  45.0, 0.0 ],
						"translation": [ 0.0,   2.0, 0.0 ],
						"scale":       [ 0.4,   0.4, 0.4 ]
					},
					"firstperson_lefthand": {
						"rotation":    [ 0.0, 225.0, 0.0 ],
						"translation": [ 0.0,   2.0, 0.0 ],
						"scale":       [ 0.4,   0.4, 0.4 ]
					}
				}
			}"""
		);
	}
}