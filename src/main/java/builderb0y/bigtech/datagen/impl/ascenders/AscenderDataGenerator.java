package builderb0y.bigtech.datagen.impl.ascenders;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class AscenderDataGenerator extends AbstractAscenderDataGenerator {

	public AscenderDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeAscenderModel(context, BigTechMod.modID("ascender_translucent"));
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("ascender_from_belts")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group(BigTechMod.modID("ascenders"))
			.pattern("bgb", "b b", "bgb")
			.where('b', FunctionalItems.BELT)
			.where('g', ConventionalItemTags.GLASS_BLOCKS)
			.result(FunctionalItems.ASCENDER)
			.count(6)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("ascender_from_descender")),
			new ShapelessRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group(BigTechMod.modID("ascenders"))
			.ingredient(FunctionalItems.DESCENDER)
			.result(FunctionalItems.ASCENDER)
			.toString()
		);
	}
}