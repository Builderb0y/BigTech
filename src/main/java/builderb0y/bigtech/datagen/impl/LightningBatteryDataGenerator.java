package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class LightningBatteryDataGenerator extends BasicItemDataGenerator {

	public LightningBatteryDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern(" g ", "geg", "gcg")
			.where('g', ConventionalItemTags.GLASS_PANES)
			.where('e', FunctionalItems.LIGHTNING_ELECTRODE)
			.where('c', ConventionalItemTags.COPPER_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}