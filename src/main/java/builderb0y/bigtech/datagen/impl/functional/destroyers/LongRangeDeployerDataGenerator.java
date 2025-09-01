package builderb0y.bigtech.datagen.impl.functional.destroyers;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

public class LongRangeDeployerDataGenerator extends AbstractDestroyerDataGenerator {

	public LongRangeDeployerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put("container.bigtech.long_range_deployer.everywhere", "Everywhere");
	}

	@Override
	public boolean hasBack() {
		return false;
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("sss", "lcd", "sss")
			.where('s', Items.SMOOTH_STONE)
			.where('l', FunctionalItems.LENS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.where('d', FunctionalItems.SHORT_RANGE_DEPLOYER)
			.result(this.getId())
			.toString()
		);
	}
}