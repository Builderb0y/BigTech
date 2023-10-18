package builderb0y.bigtech.datagen.impl.belts;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public abstract class DirectionalBeltDataGenerator extends BeltDataGenerator {

	public DirectionalBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	public abstract BlockStateJsonVariant createVariant(DataGenContext context, BlockState state);

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.block)
				.map(state -> this.createVariant(context, state))
				::iterator
			)
			.toString()
		);
	}
}