package builderb0y.bigtech.datagen.impl;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class TransmuterRecipesDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("charcoal_from_transmuter")),
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
			"""
			{
				"type": "bigtech:transmute",
				"input":  { "tag":  "minecraft:sand"  },
				"output": { "item": "minecraft:glass" },
				"energy": 40
			}"""
		);
	}
}