package builderb0y.bigtech.datagen.impl.deco.frames;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class IronFrameDataGenerator extends MetalFrameDataGenerator {

	public IronFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(ConventionalItemTags.IRON_INGOTS),
			new TagOrItem(BigTechItemTags.IRON_NUGGETS)
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		super.setupOtherItemTags(context);
		context.getTags(BigTechItemTags.IRON_NUGGETS).add(Items.IRON_NUGGET);
	}
}