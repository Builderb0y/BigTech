package builderb0y.bigtech.datagen.impl.belts;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class SpeedyBeltDataGenerator extends DirectionalBeltDataGenerator {

	public SpeedyBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_paper")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("ppp", "i i")
			.where('p', Items.PAPER)
			.where('i', ConventionalItemTags.GOLD_INGOTS)
			.result(FunctionalItems.SPEEDY_BELT)
			.count(3)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_leather")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("lll", "i i")
			.where('l', Items.LEATHER)
			.where('i', ConventionalItemTags.GOLD_INGOTS)
			.result(FunctionalItems.SPEEDY_BELT)
			.count(6)
			.toString()
		);
	}
}