package builderb0y.bigtech.datagen.impl.belts;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechProperties;
import builderb0y.bigtech.blocks.belts.DirectorBeltBlock.DirectorBeltMode;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;

public class DirectorBeltDataGenerator extends DirectionalBeltDataGenerator {

	public DirectorBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		boolean inverted = state.get(Properties.INVERTED);
		DirectorBeltMode mode = state.get(BigTechProperties.DIRECTOR_BELT_MODE);
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), '_' + (state.get(Properties.POWERED) == state.get(Properties.INVERTED) ? mode.regularName : mode.invertedName)).toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		super.writeBlockModels(context);
		this.writeBeltBlockModel(context, BigTechMod.modID("director_belt_left_right"));
		this.writeBeltBlockModel(context, BigTechMod.modID("director_belt_right_left"));
		this.writeBeltBlockModel(context, BigTechMod.modID("director_belt_left_front"));
		this.writeBeltBlockModel(context, BigTechMod.modID("director_belt_front_left"));
		this.writeBeltBlockModel(context, BigTechMod.modID("director_belt_right_front"));
		this.writeBeltBlockModel(context, BigTechMod.modID("director_belt_front_right"));
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeBeltRecipes(context, ConventionalItemTags.REDSTONE_DUSTS);
	}
}