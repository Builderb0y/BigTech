package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.items.MaterialItems;

public class CopperNuggetDataGenerator extends BasicItemDataGenerator {

	public CopperNuggetDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.MISC)
			.pattern("i")
			.where('i', ConventionalItemTags.COPPER_INGOTS)
			.result(MaterialItems.COPPER_NUGGET)
			.count(9)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("copper_ingot_from_nuggets")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.MISC)
			.pattern("nnn", "nnn", "nnn")
			.where('n', BigTechItemTags.COPPER_NUGGETS)
			.result(Items.COPPER_INGOT)
			.count(9)
			.toString()
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.COPPER_NUGGETS).addElement(this.getId());
	}
}