package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItems;

public class QuadLightningElectrodeDataGenerator extends BasicItemDataGenerator {

	public QuadLightningElectrodeDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.pattern("ee", "ee")
			.where('e', BigTechItems.LIGHTNING_ELECTRODE)
			.result(this.id)
			.toString()
		);
	}
}