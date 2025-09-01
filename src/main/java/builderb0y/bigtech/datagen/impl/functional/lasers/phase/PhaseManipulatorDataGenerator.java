package builderb0y.bigtech.datagen.impl.functional.lasers.phase;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.impl.functional.lasers.CommonPhaseManipulatorDataGenerator;
import builderb0y.bigtech.items.BigTechItemTags;

@Dependencies(CommonPhaseManipulatorDataGenerator.class)
public abstract class PhaseManipulatorDataGenerator extends BasicBlockDataGenerator {

	public PhaseManipulatorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("phase_manipulator"))
			.blockTexture("texture", this.getId())
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {

	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {

	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.PHASE_MANIPULATORS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.PHASE_MANIPULATORS).addElement(this.getId());
	}

	public void writePhaseAdjusterRecipe(DataGenContext context, Item pane, Item crystal) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.group(BigTechMod.modID("phase_manipulator"))
			.pattern("ggg", "gcg", "ggg")
			.where('g', pane)
			.where('c', crystal)
			.result(this.getId())
			.toString()
		);
	}
}