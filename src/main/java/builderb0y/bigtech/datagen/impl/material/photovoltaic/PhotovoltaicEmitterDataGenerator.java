package builderb0y.bigtech.datagen.impl.material.photovoltaic;

import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

public class PhotovoltaicEmitterDataGenerator extends PhotovoltaicCellDataGenerator {

	public PhotovoltaicEmitterDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.group(BigTechMod.MODID + ":photovoltaics")
			.pattern("l", "g", "r")
			.where('l', FunctionalItems.LENS)
			.where('g', BigTechItemTags.GLOWSTONE_ALLOY_INGOTS)
			.where('r', BigTechItemTags.REDSTONE_ALLOY_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}