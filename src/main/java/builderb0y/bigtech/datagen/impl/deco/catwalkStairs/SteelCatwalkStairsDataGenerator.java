package builderb0y.bigtech.datagen.impl.deco.catwalkStairs;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.items.FunctionalItems;

public class SteelCatwalkStairsDataGenerator extends MetalCatwalkStairsDataGenerator {

	public SteelCatwalkStairsDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeCatwalkStairsBlockModels(
			context,
			BigTechMod.modID("steel_catwalk_stairs_base"),
			BigTechMod.modID("steel_catwalk_stairs")
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeCatwalkStairsItemModels(
			context,
			BigTechMod.modID("steel_catwalk_stairs_base"),
			BigTechMod.modID("steel_catwalk_stairs")
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeCatwalkStairsRecipe(
			context,
			new TagOrItem(FunctionalItems.STEEL_PRESSURE_PLATE),
			new TagOrItem(DecoItems.STEEL_BARS)
		);
	}
}