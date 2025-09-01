package builderb0y.bigtech.datagen.impl.deco.frames;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class SilverFrameDataGenerator extends MetalFrameDataGenerator {

	public SilverFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(BigTechItemTags.SILVER_INGOTS),
			new TagOrItem(BigTechItemTags.SILVER_NUGGETS)
		);
	}
}