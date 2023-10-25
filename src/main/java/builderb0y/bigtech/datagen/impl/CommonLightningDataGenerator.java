package builderb0y.bigtech.datagen.impl;

import java.util.List;

import net.minecraft.block.Blocks;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.TagOrItem;

public class CommonLightningDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechBlockTags.CONDUCTS_LIGHTNING).addAll(List.of(
			new TagOrItem(Blocks.IRON_BLOCK),
			new TagOrItem(Blocks.IRON_BARS),
			new TagOrItem(Blocks.IRON_DOOR),
			new TagOrItem(Blocks.IRON_TRAPDOOR),
			new TagOrItem(Blocks.ANVIL),
			new TagOrItem(Blocks.CHIPPED_ANVIL),
			new TagOrItem(Blocks.DAMAGED_ANVIL),
			new TagOrItem(Blocks.CHAIN),
			new TagOrItem(Blocks.GOLD_BLOCK),
			new TagOrItem(BigTechBlockTags.METAL_FRAMES)
		));
		context.getTags(BigTechBlockTags.SHOCKS_ENTITIES).addAll(List.of(
			new TagOrItem(Blocks.IRON_BLOCK),
			new TagOrItem(Blocks.IRON_BARS),
			new TagOrItem(Blocks.IRON_DOOR),
			new TagOrItem(Blocks.IRON_TRAPDOOR),
			new TagOrItem(Blocks.ANVIL),
			new TagOrItem(Blocks.CHIPPED_ANVIL),
			new TagOrItem(Blocks.DAMAGED_ANVIL),
			new TagOrItem(Blocks.CHAIN),
			new TagOrItem(Blocks.GOLD_BLOCK),
			new TagOrItem(BigTechBlockTags.METAL_FRAMES)
		));
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_lightning_cable_center")),
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"particle": "#cable"
				},
				"elements": [
					{
						"from": [  4,  4,  4 ],
						"to":   [ 12, 12, 12 ],
						"faces": {
							"up": { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_lightning_cable_connection")),
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"particle": "#cable"
				},
				"elements": [
					{
						"from": [  4, 12,  4 ],
						"to":   [ 12, 16, 12 ],
						"faces": {
							"up":    { "uv": [ 8, 0, 16,  8 ], "texture": "#cable", "cullface": "up" },
							"north": { "uv": [ 8, 8, 16, 12 ], "texture": "#cable"                   },
							"east":  { "uv": [ 8, 8, 16, 12 ], "texture": "#cable"                   },
							"south": { "uv": [ 8, 8, 16, 12 ], "texture": "#cable"                   },
							"west":  { "uv": [ 8, 8, 16, 12 ], "texture": "#cable"                   }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.itemModelPath(BigTechMod.modID("template_lightning_cable")),
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"particle": "#cable"
				},
				"elements": [
					{
						"from": [  4,  4,  4 ],
						"to":   [ 12, 12, 12 ],
						"faces": {
							"up":    { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"down":  { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"north": { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"east":  { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"south": { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"west":  { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" }
						}
					}
				]
			}"""
		);
	}
}