package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class RawSilverDataGenerator extends BasicItemDataGenerator {

	public RawSilverDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.RAW_SILVER).addElement(this.getId());
		context.getTags(ConventionalItemTags.RAW_MATERIALS).add(BigTechItemTags.RAW_SILVER);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("s")
			.where('s', BigTechItemTags.RAW_SILVER_BLOCKS)
			.result(this.getId())
			.count(9)
			.toString()
		);
	}
}