package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class LensDataGenerator extends BasicItemDataGenerator {

	public LensDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.pattern(" i ", "igi", " i ")
			.where('i', BigTechItemTags.IRON_NUGGETS)
			.where('g', ConventionalItemTags.GLASS_PANES)
			.result(this.id)
			.toString()
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}
}