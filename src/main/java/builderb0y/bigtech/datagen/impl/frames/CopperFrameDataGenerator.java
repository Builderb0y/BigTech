package builderb0y.bigtech.datagen.impl.frames;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class CopperFrameDataGenerator extends MetalFrameDataGenerator {

	public CopperFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(ConventionalItemTags.COPPER_INGOTS),
			new TagOrItem(BigTechItemTags.COPPER_NUGGETS)
		);
	}
}