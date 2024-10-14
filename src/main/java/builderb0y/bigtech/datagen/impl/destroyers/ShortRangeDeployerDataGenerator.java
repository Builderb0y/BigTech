package builderb0y.bigtech.datagen.impl.destroyers;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;

public class ShortRangeDeployerDataGenerator extends AbstractDestroyerDataGenerator {

	public ShortRangeDeployerDataGenerator(BlockItem blockItem) {
		super(blockItem);
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
			.pattern("ccc", "cpc", "crc")
			.where('c', ItemTags.STONE_CRAFTING_MATERIALS)
			.where('p', Items.PISTON)
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.result(this.getId())
			.toString()
		);
	}
}