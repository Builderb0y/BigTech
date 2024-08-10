package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class MagnetiteNuggetDataGenerator extends BasicItemDataGenerator {

	public MagnetiteNuggetDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_ingot")),
			new ShapedRecipeBuilder()
			.pattern("i")
			.where('i', BigTechItemTags.MAGNETITE_INGOTS)
			.result(this.getId())
			.count(9)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_transmute")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input":  { "tag":  "c:iron_nuggets"           },
				"output": { "item": "bigtech:magnetite_nugget" },
				"energy": 10
			}"""
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.MAGNETITE_NUGGETS).addElement(this.getId());
	}
}