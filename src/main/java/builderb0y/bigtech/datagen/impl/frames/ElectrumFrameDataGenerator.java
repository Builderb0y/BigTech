package builderb0y.bigtech.datagen.impl.frames;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class ElectrumFrameDataGenerator extends MetalFrameDataGenerator {

	public ElectrumFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(BigTechItemTags.ELECTRUM_INGOTS),
			new TagOrItem(BigTechItemTags.ELECTRUM_NUGGETS)
		);
	}
}