package builderb0y.bigtech.datagen.impl.belts;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItems;

public class NormalBeltDataGenerator extends DirectionalBeltDataGenerator {

	public NormalBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_paper")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("ppp", "i i")
			.where('p', Items.PAPER)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.result(BigTechItems.BELT)
			.count(3)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_leather")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("lll", "i i")
			.where('l', Items.LEATHER)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.result(BigTechItems.BELT)
			.count(6)
			.toString()
		);
	}
}