package builderb0y.bigtech.datagen.impl.metalLadders;

import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

@Dependencies(CommonMetalLadderDataGenerator.class)
public abstract class MetalLadderDataGenerator extends BasicBlockDataGenerator {

	public MetalLadderDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.getBlock())
				.map(state -> new BlockStateJsonVariant(
					state,
					context.prefixPath("block/", this.getId()).toString(),
					null,
					BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
				))
				::iterator
			)
			.toString()
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