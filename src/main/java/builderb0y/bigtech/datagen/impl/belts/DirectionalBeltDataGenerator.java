package builderb0y.bigtech.datagen.impl.belts;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public abstract class DirectionalBeltDataGenerator extends BeltDataGenerator {

	public DirectionalBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixPath("block/", this.id).toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			BlockStateJsonVariant
			.streamStatesSorted(this.block)
			.map(state -> this.createVariant(context, state))
			.collect(Table.collector(BlockStateJsonVariant.FORMAT))
			.toString()
		);
	}
}