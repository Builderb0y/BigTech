package builderb0y.bigtech.datagen.impl.frames;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

public class WoodenFrameDataGenerator extends FrameDataGenerator {

	public Item planks;

	public WoodenFrameDataGenerator(BlockItem blockItem, WoodRegistrableCollection.Type type) {
		super(blockItem);
		this.planks = BigTechBlocks.VANILLA_PLANKS.get(type).asItem();
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(this.planks),
			new TagOrItem(ConventionalItemTags.WOODEN_RODS)
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		super.setupOtherBlockTags(context);
		context.getTags(BigTechBlockTags.WOODEN_FRAMES).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.WOODEN_FRAMES).addElement(this.getId());
	}
}