package builderb0y.bigtech.datagen.impl.deco.frames;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.BigTechItemTags;

public abstract class MetalFrameDataGenerator extends FrameDataGenerator {

	public MetalFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		super.setupOtherBlockTags(context);
		context.getTags(BigTechBlockTags.METAL_FRAMES).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.METAL_FRAMES).addElement(this.getId());
	}
}