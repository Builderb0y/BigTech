package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class SteelNuggetDataGenerator extends BasicItemDataGenerator {

	public SteelNuggetDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.STEEL_NUGGETS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("steel_nugget_uncrafting")),
			new ShapedRecipeBuilder()
			.pattern("i")
			.where('i', BigTechItemTags.STEEL_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}