package builderb0y.bigtech.datagen.impl.frames;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class GoldFrameDataGenerator extends MetalFrameDataGenerator {

	public GoldFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(ConventionalItemTags.GOLD_INGOTS),
			new TagOrItem(BigTechItemTags.GOLD_NUGGETS)
		);
	}
}