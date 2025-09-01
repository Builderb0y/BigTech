package builderb0y.bigtech.datagen.impl.material.silver;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class SilverNuggetDataGenerator extends BasicItemDataGenerator {

	public SilverNuggetDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.SILVER_NUGGETS).addElement(this.getId());
		context.getTags(ConventionalItemTags.NUGGETS).add(BigTechItemTags.SILVER_NUGGETS);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("s")
			.where('s', BigTechItemTags.SILVER_INGOTS)
			.result(this.getId())
			.count(9)
			.toString()
		);
	}
}