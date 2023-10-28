package builderb0y.bigtech.datagen.impl.frames;

import java.util.List;

import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningLevelTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class CommonFrameDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechBlockTags.CONDUCTS_LIGHTNING).add(BigTechBlockTags.METAL_FRAMES);
		context.getTags(BigTechBlockTags.SHOCKS_ENTITIES).add(BigTechBlockTags.METAL_FRAMES);
		context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.METAL_FRAMES);
		context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.METAL_FRAMES);
		context.getTags(MiningToolTags.AXE).add(BigTechBlockTags.WOODEN_FRAMES);

		context.getTags(BigTechBlockTags.METAL_FRAMES).add(BigTechBlockTags.COPPER_FRAMES);
		context.getTags(BigTechItemTags.METAL_FRAMES).add(BigTechItemTags.COPPER_FRAMES);
		context.getTags(BigTechBlockTags.FRAMES).addAll(List.of(
			new TagOrItem(BigTechBlockTags.WOODEN_FRAMES),
			new TagOrItem(BigTechBlockTags.METAL_FRAMES)
		));
		context.getTags(BigTechItemTags.FRAMES).addAll(List.of(
			new TagOrItem(BigTechBlockTags.WOODEN_FRAMES),
			new TagOrItem(BigTechBlockTags.METAL_FRAMES)
		));

		context.getTags(BigTechItemTags.STICKS).add(Items.STICK);
		context.getTags(BigTechItemTags.GOLD_NUGGETS).add(Items.GOLD_NUGGET);
		context.getTags(BlockTags.CLIMBABLE).add(BigTechBlockTags.FRAMES);
		context.getTags(BigTechBlockTags.STICKS_TO_COPPER_FRAME).add(BigTechBlockTags.COPPER_FRAMES);

		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_frame")),
			//language=json
			"""
			{
				"parent": "block/block",
				"textures": {
					"particle": "#outer"
				},
				"elements": [
					{
						"from": [  0,  0, 0 ],
						"to":   [ 16, 16, 3 ],
						"faces": {
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "north" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                      }
						}
					},
					{
						"from": [ 13,  0,  0 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"east": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "east" },
							"west": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                     }
						}
					},
					{
						"from": [  0,  0, 13 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                      },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "south" }
						}
					},
					{
						"from": [ 0,  0,  0 ],
						"to":   [ 3, 16, 16 ],
						"faces": {
							"east": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                     },
							"west": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "west" }
						}
					},
					{
						"from": [  0, 13,  0 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"up":   { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "up" },
							"down": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                   }
						}
					},
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 3, 16 ],
						"faces": {
							"up":   { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                     },
							"down": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "down" }
						}
					}
				]
			}
			"""
		);
	}
}