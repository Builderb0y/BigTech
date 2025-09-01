package builderb0y.bigtech.datagen.impl.functional;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class ElectrumCoilDataGenerator extends CommonCoilDataGenerator {

	public ElectrumCoilDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("cpc", "cpc", "cpc")
			.where('c', BigTechItemTags.ELECTRUM_INGOTS)
			.where('p', ItemTags.PLANKS)
			.result(this.getId())
			.toString()
		);
	}
}