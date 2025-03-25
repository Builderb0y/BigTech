package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class ElectrumNuggetDataGenerator extends BasicItemDataGenerator {

	public ElectrumNuggetDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.ELECTRUM_NUGGETS).addElement(this.getId());
		context.getTags(ConventionalItemTags.NUGGETS).add(BigTechItemTags.ELECTRUM_NUGGETS);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("s")
			.where('s', BigTechItemTags.ELECTRUM_INGOTS)
			.result(this.getId())
			.count(9)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("electrum_nugget_arc_furnace")),
			//language=json
			"""
			{
				"type": "bigtech:arc_furnace",
				"inputs": [
					"#c:nuggets/gold",
					"#c:nuggets/silver"
				],
				"slow_cool_result": { "id": "bigtech:electrum_nugget", "count": 2 },
				"energy": 200,
				"cooling_rate": 15
			}"""
		);
	}
}