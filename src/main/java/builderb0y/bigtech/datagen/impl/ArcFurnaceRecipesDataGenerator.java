package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.items.BigTechItemTags;

public class ArcFurnaceRecipesDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechItemTags.IRON_DOUBLEABLES).addAll(ConventionalItemTags.IRON_RAW_MATERIALS, ConventionalItemTags.IRON_ORES);
		context.getTags(BigTechItemTags.COPPER_DOUBLEABLES).addAll(ConventionalItemTags.COPPER_RAW_MATERIALS, ConventionalItemTags.IRON_ORES);
		context.getTags(BigTechItemTags.GOLD_DOUBLEABLES).addAll(ConventionalItemTags.GOLD_RAW_MATERIALS, ConventionalItemTags.IRON_ORES);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("iron_doubling")),
			//language=json
			"""
			{
				"type": "bigtech:arc_furnace",
				"inputs": [ "#bigtech:iron_doubleables" ],
				"slow_cool_result": { "id": "minecraft:iron_ingot", "count": 2 },
				"fast_cool_result": { "id": "bigtech:cast_iron_ingot", "count": 2 },
				"energy": 1000,
				"cooling_rate": 10
			}"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("iron_ingot_conversion")),
			//language=json
			"""
			{
				"type": "bigtech:arc_furnace",
				"inputs": [ "#c:ingots/iron" ],
				"slow_cool_result": { "id": "minecraft:iron_ingot" },
				"fast_cool_result": { "id": "bigtech:cast_iron_ingot" },
				"energy": 500,
				"cooling_rate": 10
			}"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("iron_block_conversion")),
			//language=json
			"""
			{
				"type": "bigtech:arc_furnace",
				"inputs": [ "#c:storage_blocks/iron" ],
				"slow_cool_result": { "id": "minecraft:iron_block" },
				"fast_cool_result": { "id": "bigtech:cast_iron_block" },
				"energy": 4000,
				"cooling_rate": 10
			}"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("copper_doubling")),
			//language=json
			"""
			{
				"type": "bigtech:arc_furnace",
				"inputs": [ "#bigtech:copper_doubleables" ],
				"slow_cool_result": { "id": "minecraft:copper_ingot", "count": 2 },
				"energy": 1000,
				"cooling_rate": 10
			}"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("gold_doubling")),
			//language=json
			"""
			{
				"type": "bigtech:arc_furnace",
				"inputs": [ "#bigtech:gold_doubleables" ],
				"slow_cool_result": { "id": "minecraft:gold_ingot", "count": 2 },
				"energy": 1000,
				"cooling_rate": 10
			}"""
		);
	}
}