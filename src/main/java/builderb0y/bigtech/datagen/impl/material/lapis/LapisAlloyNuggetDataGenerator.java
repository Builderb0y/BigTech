package builderb0y.bigtech.datagen.impl.material.lapis;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class LapisAlloyNuggetDataGenerator extends BasicItemDataGenerator {

	public LapisAlloyNuggetDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.LAPIS_ALLOY_NUGGETS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_ingot")),
			new ShapelessRecipeBuilder()
			.ingredient(BigTechItemTags.LAPIS_ALLOY_INGOTS)
			.result(this.getId())
			.count(9)
			.toString()
		);
	}
}