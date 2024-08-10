package builderb0y.bigtech.datagen.impl.catwalkPlatforms;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.BigTechItemTags;

public abstract class MetalCatwalkPlatformDataGenerator extends CatwalkPlatformDataGenerator {

	public MetalCatwalkPlatformDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//handled by CommonCatwalkPlatformDataGenerator.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//handled by CommonCatwalkPlatformDataGenerator.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.METAL_CATWALK_PLATFORMS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.METAL_CATWALK_PLATFORMS).addElement(this.getId());
	}
}