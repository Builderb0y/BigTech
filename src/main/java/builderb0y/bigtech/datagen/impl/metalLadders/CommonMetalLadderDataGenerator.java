package builderb0y.bigtech.datagen.impl.metalLadders;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningLevelTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;

public class CommonMetalLadderDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		this.writeBlockModels(context);
		this.writeTags(context);
	}

	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_ladder")),
			//language=json
			"""
			{
				"ambientocclusion": false,
				"textures": {
					"particle": "#ladder"
				},
				"elements": [
					{
						"from": [ 2,  0, 15 ],
						"to":   [ 4, 16, 16 ],
						"faces": {
							"up":    { "uv": [ 14,  0, 12,  1 ], "texture": "#ladder", "cullface": "up"   },
							"down":  { "uv": [ 14, 15, 12, 16 ], "texture": "#ladder", "cullface": "down" },
							"north": { "uv": [ 12,  0, 14, 16 ], "texture": "#ladder" },
							"east":  { "uv": [ 13,  0, 14, 16 ], "texture": "#ladder" },
							"south": { "uv": [ 14,  0, 12, 16 ], "texture": "#ladder", "cullface": "south" },
							"west":  { "uv": [ 13,  0, 14, 16 ], "texture": "#ladder" }
						}
					},
					{
						"from": [ 12,  0, 15 ],
						"to":   [ 14, 16, 16 ],
						"faces": {
							"up":    { "uv": [ 4,  0, 2,  1 ], "texture": "#ladder", "cullface": "up"   },
							"down":  { "uv": [ 4, 15, 2, 16 ], "texture": "#ladder", "cullface": "down" },
							"north": { "uv": [ 2,  0, 4, 16 ], "texture": "#ladder" },
							"east":  { "uv": [ 2,  0, 3, 16 ], "texture": "#ladder" },
							"south": { "uv": [ 4,  0, 2, 16 ], "texture": "#ladder", "cullface": "south" },
							"west":  { "uv": [ 3,  0, 4, 16 ], "texture": "#ladder" }
						}
					},
					{
						"from": [  1, 1, 14 ],
						"to":   [ 15, 3, 15 ],
						"faces": {
							"north": { "uv": [  1, 13, 15, 15 ], "texture": "#ladder" },
							"east":  { "uv": [  1, 13,  2, 15 ], "texture": "#ladder" },
							"south": { "uv": [ 15, 13,  1, 15 ], "texture": "#ladder" },
							"west":  { "uv": [ 14, 13, 15, 15 ], "texture": "#ladder" },
							"up":    { "uv": [ 15, 13,  1, 14 ], "texture": "#ladder" },
							"down":  { "uv": [ 15, 14,  1, 15 ], "texture": "#ladder" }
						}
					},
					{
						"from": [  1, 5, 14 ],
						"to":   [ 15, 7, 15 ],
						"faces": {
							"north": { "uv": [  1,  9, 15, 11 ], "texture": "#ladder" },
							"east":  { "uv": [  1,  9,  2, 11 ], "texture": "#ladder" },
							"south": { "uv": [ 15,  9,  1, 11 ], "texture": "#ladder" },
							"west":  { "uv": [ 14,  9, 15, 11 ], "texture": "#ladder" },
							"up":    { "uv": [ 15,  9,  1, 10 ], "texture": "#ladder" },
							"down":  { "uv": [ 15, 10,  1, 11 ], "texture": "#ladder" }
						}
					},
					{
						"from": [  1,  9, 14 ],
						"to":   [ 15, 11, 15 ],
						"faces": {
							"north": { "uv": [  1, 5, 15, 7 ], "texture": "#ladder" },
							"east":  { "uv": [  1, 5,  2, 7 ], "texture": "#ladder" },
							"south": { "uv": [ 15, 5,  1, 7 ], "texture": "#ladder" },
							"west":  { "uv": [ 14, 5, 15, 7 ], "texture": "#ladder" },
							"up":    { "uv": [ 15, 5,  1, 6 ], "texture": "#ladder" },
							"down":  { "uv": [ 15, 6,  1, 7 ], "texture": "#ladder" }
						}
					},
					{
						"from": [  1, 13, 14 ],
						"to":   [ 15, 15, 15 ],
						"faces": {
							"north": { "uv": [  1, 1, 15, 3 ], "texture": "#ladder" },
							"east":  { "uv": [  1, 1,  2, 3 ], "texture": "#ladder" },
							"south": { "uv": [ 15, 1,  1, 3 ], "texture": "#ladder" },
							"west":  { "uv": [ 14, 1, 15, 3 ], "texture": "#ladder" },
							"up":    { "uv": [ 15, 1,  1, 2 ], "texture": "#ladder" },
							"down":  { "uv": [ 15, 2,  1, 3 ], "texture": "#ladder" }
						}
					}
				]
			}"""
		);
	}

	public void writeTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.METAL_LADDERS).addAll(
			new TagOrItem(DecoBlocks.IRON_LADDER),
			new TagOrItem(BigTechBlockTags.COPPER_LADDERS)
		);
		context.getTags(BigTechBlockTags.LADDERS).addAll(
			new TagOrItem(BigTechBlockTags.METAL_LADDERS),
			new TagOrItem(Blocks.LADDER)
		);
		context.getTags(BigTechItemTags.METAL_LADDERS).addAll(
			new TagOrItem(DecoItems.IRON_LADDER),
			new TagOrItem(BigTechItemTags.COPPER_LADDERS)
		);
		context.getTags(BigTechItemTags.LADDERS).addAll(
			new TagOrItem(BigTechItemTags.METAL_LADDERS),
			new TagOrItem(Items.LADDER)
		);
		context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.METAL_LADDERS);
		context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.METAL_LADDERS);
		context.getTags(BlockTags.CLIMBABLE).add(BigTechBlockTags.METAL_LADDERS);
	}
}