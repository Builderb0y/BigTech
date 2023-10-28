package builderb0y.bigtech.datagen.impl.catwalkStairs;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.BigTechItemTags;

public abstract class MetalCatwalkStairsDataGenerator extends CatwalkStairsDataGenerator {

	public MetalCatwalkStairsDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.METAL_CATWALK_STAIRS).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.METAL_CATWALK_STAIRS).addElement(this.id);
	}
}