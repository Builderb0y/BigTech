package builderb0y.bigtech.datagen.impl.belts;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;

public class TrapdoorBeltDataGenerator extends DirectionalBeltDataGenerator {

	public TrapdoorBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeBeltBlockModel(context, context.suffixPath(this.id, "_closed"));
		this.writeBeltBlockModel(context, context.suffixPath(this.id, "_open"));
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.id, "_closed");
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath(
				"block/",
				this.id,
				state.get(Properties.POWERED) == state.get(Properties.INVERTED) ? "_closed" : "_open"
			)
			.toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeBeltRecipes(context, ItemTags.TRAPDOORS);
	}
}