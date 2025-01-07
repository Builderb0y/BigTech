package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class LightningElectrodeDataGenerator extends BasicItemDataGenerator {

	public LightningElectrodeDataGenerator(Item item) {
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
			.pattern("i", "i")
			.where('i', FunctionalItems.FERROMAGNETIC_INGOT)
			.result(this.getId())
			.toString()
		);
	}
}