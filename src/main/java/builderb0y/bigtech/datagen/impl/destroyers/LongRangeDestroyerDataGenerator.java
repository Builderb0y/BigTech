package builderb0y.bigtech.datagen.impl.destroyers;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

public class LongRangeDestroyerDataGenerator extends AbstractDestroyerDataGenerator {

	public LongRangeDestroyerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("sss", "lcd", "sss")
			.where('s', Items.SMOOTH_STONE)
			.where('l', FunctionalItems.LENS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.where('d', FunctionalItems.SHORT_RANGE_DESTROYER)
			.result(this.id)
			.toString()
		);
	}
}