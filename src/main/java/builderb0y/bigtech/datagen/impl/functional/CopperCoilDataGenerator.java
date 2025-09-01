package builderb0y.bigtech.datagen.impl.functional;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;

public class CopperCoilDataGenerator extends CommonCoilDataGenerator {

	public CopperCoilDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("cpc", "cpc", "cpc")
			.where('c', ConventionalItemTags.COPPER_INGOTS)
			.where('p', ItemTags.PLANKS)
			.result(this.getId())
			.toString()
		);
	}
}