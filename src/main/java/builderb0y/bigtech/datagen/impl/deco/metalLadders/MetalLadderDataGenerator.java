package builderb0y.bigtech.datagen.impl.deco.metalLadders;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;

@Dependencies(CommonMetalLadderDataGenerator.class)
public abstract class MetalLadderDataGenerator extends BasicBlockDataGenerator {

	public MetalLadderDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixPath("block/", this.getId()).toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	public Identifier getTexture(DataGenContext context) {
		return this.getId();
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_ladder"))
			.blockTexture("ladder", this.getTexture(context))
			.toString()
		);
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		this.writeDefaultItemDefinition(context, context.prefixPath("item/", this.getId()));
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.getId()),
			new RetexturedModelBuilder()
			.itemParent(Identifier.ofVanilla("generated"))
			.blockTexture("layer0", this.getTexture(context))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//handled by dependency.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//handled by dependency.
	}
}