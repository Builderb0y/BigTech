package builderb0y.bigtech.datagen.impl;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CommonTransmuterDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("charcoal_from_transmuter")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input":  { "tag":  "minecraft:logs" },
				"output": { "item": "minecraft:charcoal" },
				"energy": 40
			}"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("popped_chorus_fruit_from_transmuter")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input":  { "item": "minecraft:chorus_fruit" },
				"output": { "item": "minecraft:popped_chorus_fruit" },
				"energy": 10
			}"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("glass_from_transmuter")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input":  { "tag":  "minecraft:sand"  },
				"output": { "item": "minecraft:glass" },
				"energy": 40
			}"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("crystal_cluster_from_transmuter")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input": { "item": "bigtech:crystalline_sand" },
				"output": [
					{ "item": "bigtech:red_crystal_cluster"     },
					{ "item": "bigtech:yellow_crystal_cluster"  },
					{ "item": "bigtech:green_crystal_cluster"   },
					{ "item": "bigtech:cyan_crystal_cluster"    },
					{ "item": "bigtech:blue_crystal_cluster"    },
					{ "item": "bigtech:magenta_crystal_cluster" },
					{ "item": "bigtech:black_crystal_cluster"   },
					{ "item": "bigtech:white_crystal_cluster"   }
				],
				"energy": 500
			}"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("glowstone_dust_from_transmuter")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input": { "tag": "c:dusts/redstone" },
				"output": { "item": "minecraft:glowstone_dust" },
				"energy": 100
			}"""
		);
	}
}