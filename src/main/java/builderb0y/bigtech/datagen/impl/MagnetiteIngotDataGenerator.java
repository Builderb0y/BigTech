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
			context.recipePath(context.suffixPath(this.id, "_from_nuggets")),
			new ShapedRecipeBuilder()
			.pattern("nnn", "nnn", "nnn")
			.tagIngredient('n', BigTechItemTags.MAGNETITE_NUGGETS)
			.result(this.id)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_block")),
			new ShapedRecipeBuilder()
			.pattern("b")
			.tagIngredient('b', BigTechItemTags.MAGNETITE_BLOCKS)
			.result(this.id)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_transmute")),
			"""
			{
				"type": "bigtech:transmute",
				"input":  { "tag":  "c:iron_ingots"           },
				"output": { "item": "bigtech:magnetite_ingot" },
				"energy": 90
			}"""
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.MAGNETITE_NUGGETS).addElement(this.id);
		context.getTags(ItemTags.BEACON_PAYMENT_ITEMS).addElement(this.id);
	}
}