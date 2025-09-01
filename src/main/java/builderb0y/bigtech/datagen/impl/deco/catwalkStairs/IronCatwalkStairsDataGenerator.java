package builderb0y.bigtech.datagen.impl.deco.catwalkStairs;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;

public class IronCatwalkStairsDataGenerator extends MetalCatwalkStairsDataGenerator {

	public IronCatwalkStairsDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeCatwalkStairsBlockModels(
			context,
			BigTechMod.modID("iron_catwalk_platform_base"),
			BigTechMod.modID("iron_catwalk_stairs")
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeCatwalkStairsItemModels(
			context,
			BigTechMod.modID("iron_catwalk_platform_base"),
			BigTechMod.modID("iron_catwalk_stairs")
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeCatwalkStairsRecipe(
			context,
			new TagOrItem(Items.HEAVY_WEIGHTED_PRESSURE_PLATE),
			new TagOrItem(Items.IRON_BARS)
		);
	}
}