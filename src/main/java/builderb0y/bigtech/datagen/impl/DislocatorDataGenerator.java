package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;

public class DislocatorDataGenerator extends BasicItemDataGenerator {

	public DislocatorDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(ItemTags. DURABILITY_ENCHANTABLE).addElement(this.getId());
		context.getTags(ItemTags.MINING_LOOT_ENCHANTABLE).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.EQUIPMENT)
			.pattern(" gp", " gg", "d  ")
			.where('g', ConventionalItemTags.GOLD_INGOTS)
			.where('p', ConventionalItemTags.ENDER_PEARLS)
			.where('d', ConventionalItemTags.DIAMOND_GEMS)
			.result(this.getId())
			.toString()
		);
	}
}