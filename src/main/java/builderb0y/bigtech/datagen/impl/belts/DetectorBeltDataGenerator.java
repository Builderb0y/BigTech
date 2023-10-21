package builderb0y.bigtech.datagen.impl.belts;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;

public class DetectorBeltDataGenerator extends DirectionalBeltDataGenerator {

	public DetectorBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeBeltBlockModel(context, context.suffixPath(this.id, "_off"));
		this.writeBeltBlockModel(context, context.suffixPath(this.id, "_on"));
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeBeltItemModel(context, context.suffixPath(this.id, "_off"));
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath(
				"block/",
				this.id,
				state.get(Properties.POWER) != 0 ? "_on" : "_off"
			)
			.toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeBeltRecipes(context, BigTechItemTags.PRESSURE_PLATES);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		super.setupOtherItemTags(context);
		context.getTags(BigTechItemTags.PRESSURE_PLATES).addAll(List.of(
			"#minecraft:wooden_pressure_plates",
			"minecraft:stone_pressure_plate",
			"minecraft:light_weighted_pressure_plate",
			"minecraft:heavy_weighted_pressure_plate"
		));
	}
}