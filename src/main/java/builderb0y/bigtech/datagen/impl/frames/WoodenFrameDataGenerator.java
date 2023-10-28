package builderb0y.bigtech.datagen.impl.frames;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;

public class WoodenFrameDataGenerator extends FrameDataGenerator {

	public Item planks;

	public WoodenFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
		this.planks = Registries.ITEM.get(new Identifier("minecraft", this.id.path.substring(0, this.id.path.length() - "_frame".length()) + "_planks"));
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(this.planks),
			new TagOrItem(BigTechItemTags.STICKS)
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		super.setupOtherBlockTags(context);
		context.getTags(BigTechBlockTags.WOODEN_FRAMES).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.WOODEN_FRAMES).addElement(this.id);
	}
}