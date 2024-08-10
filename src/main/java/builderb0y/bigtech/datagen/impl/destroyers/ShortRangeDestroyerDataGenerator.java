package builderb0y.bigtech.datagen.impl.destroyers;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;

public class ShortRangeDestroyerDataGenerator extends AbstractDestroyerDataGenerator {

	public ShortRangeDestroyerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("ccc", "cpc", "crc")
			.where('c', ItemTags.STONE_CRAFTING_MATERIALS)
			.where('p', ConventionalItemTags.TOOLS)
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.result(this.getId())
			.toString()
		);
	}
}