package builderb0y.bigtech.datagen.impl.deco.catwalkPlatforms;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;

public class SteelCatwalkPlatformDataGenerator extends MetalCatwalkPlatformDataGenerator {

	public SteelCatwalkPlatformDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeCatwalkPlatformBlockModels(
			context,
			BigTechMod.modID("steel_block"),
			BigTechMod.modID("steel_catwalk_platform_rail")
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeCatwalkPlatformItemModels(
			context,
			BigTechMod.modID("steel_block"),
			BigTechMod.modID("steel_catwalk_platform_rail")
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeCatwalkPlatformRecipe(
			context,
			new TagOrItem(FunctionalBlocks.STEEL_PRESSURE_PLATE),
			new TagOrItem(DecoBlocks.STEEL_BARS)
		);
	}
}