package builderb0y.bigtech.datagen.impl.technoCrafters;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.FunctionalItems;

public class PortableTechnoCrafterDataGenerator extends BasicItemDataGenerator {

	public PortableTechnoCrafterDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("rcr", "rir", "rcr")
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.where('c', FunctionalItems.STONE_CRAFTING_TABLE)
			.where('i', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
			.result(this.getId())
			.toString()
		);
	}
}