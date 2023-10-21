package builderb0y.bigtech.datagen.impl.ascenders;

import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public abstract class AbstractAscenderDataGenerator extends BasicBlockDataGenerator {

	public AbstractAscenderDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	public void writeAscenderModel(DataGenContext context, Identifier translucentLayer) {
		context.writeToFile(
			context.blockModelPath(this.id),
			new RetexturedModelBuilder()
			.jmxl(true)
			.blockParent(BigTechMod.modID("template_ascender"))
			.blockTexture("translucent", translucentLayer)
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.ASCENDERS).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.ASCENDERS).addElement(this.id);
	}
}