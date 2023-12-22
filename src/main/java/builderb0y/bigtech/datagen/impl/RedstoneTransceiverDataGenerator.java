package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public abstract class RedstoneTransceiverDataGenerator extends BasicBlockDataGenerator {

	public RedstoneTransceiverDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.block)
				.map((BlockState state) -> new BlockStateJsonVariant(
					state,
					context.prefixSuffixPath("block/", this.id, state.get(Properties.POWERED) ? "_on" : "_off").toString(),
					null,
					BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.id, "_off");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood pickaxe is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}
}