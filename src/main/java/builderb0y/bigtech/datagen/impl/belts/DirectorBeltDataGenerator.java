package builderb0y.bigtech.datagen.impl.belts;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;

public class DirectorBeltDataGenerator extends DirectionalBeltDataGenerator {

	public DirectorBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.id, state.get(Properties.POWERED) == state.get(Properties.INVERTED) ? "_left" : "_right").toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		super.writeBlockModels(context);
		this.writeBeltBlockModel(context, BigTechMod.modID("director_belt_left"));
		this.writeBeltBlockModel(context, BigTechMod.modID("director_belt_right"));
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeBeltRecipes(context, ConventionalItemTags.REDSTONE_DUSTS);
	}
}