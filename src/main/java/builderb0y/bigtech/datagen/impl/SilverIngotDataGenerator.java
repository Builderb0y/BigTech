package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class SilverIngotDataGenerator extends BasicItemDataGenerator {

	public SilverIngotDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.SILVER_INGOTS).addElement(this.getId());
		context.getTags(ConventionalItemTags.INGOTS).add(BigTechItemTags.SILVER_INGOTS);
		context.getTags(BigTechItemTags.SILVER_DOUBLEABLES).addAll(BigTechItemTags.SILVER_ORES, BigTechItemTags.RAW_SILVER);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "packing")),
			new ShapedRecipeBuilder()
			.pattern("sss", "sss", "sss")
			.where('s', BigTechItemTags.SILVER_NUGGETS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "unpacking")),
			new ShapedRecipeBuilder()
			.pattern("s")
			.where('s', BigTechItemTags.SILVER_BLOCKS)
			.result(this.getId())
			.count(9)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("silver_doubling")),
			//language=json
			"""
			{
				"type": "bigtech:arc_furnace",
				"inputs": [ "#bigtech:silver_doubleables" ],
				"slow_cool_result": { "id": "bigtech:silver_ingot", "count": 2 },
				"energy": 1000,
				"cooling_rate": 10
			}"""
		);
	}
}