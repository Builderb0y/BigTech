package builderb0y.bigtech.datagen.impl.catwalkPlatforms;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;

public class IronCatwalkPlatformDataGenerator extends MetalCatwalkPlatformDataGenerator {

	public IronCatwalkPlatformDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeCatwalkPlatformBlockModels(
			context,
			BigTechMod.modID("iron_catwalk_platform_base"),
			BigTechMod.modID("iron_catwalk_platform_rail")
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeCatwalkPlatformItemModels(
			context,
			BigTechMod.modID("iron_catwalk_platform_base"),
			BigTechMod.modID("iron_catwalk_platform_rail")
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeCatwalkPlatformRecipe(
			context,
			new TagOrItem(Items.HEAVY_WEIGHTED_PRESSURE_PLATE),
			new TagOrItem(Items.IRON_BARS)
		);
	}
}