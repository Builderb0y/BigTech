package builderb0y.bigtech.datagen.impl.functional.ascenders;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class DescenderDataGenerator extends AbstractAscenderDataGenerator {

	public DescenderDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeAscenderModel(context, BigTechMod.modID("descender_translucent"));
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("descender_from_ascender")),
			new ShapelessRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group(BigTechMod.modID("ascenders"))
			.ingredient(FunctionalItems.ASCENDER)
			.result(FunctionalItems.DESCENDER)
			.toString()
		);
	}
}