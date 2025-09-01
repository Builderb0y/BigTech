package builderb0y.bigtech.datagen.impl.deco.frames;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class SteelFrameDataGenerator extends MetalFrameDataGenerator {

	public SteelFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(BigTechItemTags.STEEL_INGOTS),
			new TagOrItem(BigTechItemTags.STEEL_NUGGETS)
		);
	}
}