package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class MagnetiteIngotDataGenerator extends BasicItemDataGenerator {

	public MagnetiteIngotDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_nuggets")),
			new ShapedRecipeBuilder()
			.pattern("nnn", "nnn", "nnn")
			.where('n', BigTechItemTags.MAGNETITE_NUGGETS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_transmute")),
			//language=json
			"""
			{
				"type": "bigtech:transmute",
				"input": "#c:ingots/iron",
				"output": { "item": "bigtech:magnetite_ingot" },
				"energy": 90
			}"""
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.MAGNETITE_INGOTS).addElement(this.getId());
		context.getTags(ItemTags.BEACON_PAYMENT_ITEMS).addElement(this.getId());
	}
}