package builderb0y.bigtech.datagen.impl.functional.belts;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;

public class DetectorBeltDataGenerator extends DirectionalBeltDataGenerator {

	public DetectorBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeBeltBlockModel(context, context.suffixPath(this.getId(), "_off"));
		this.writeBeltBlockModel(context, context.suffixPath(this.getId(), "_on"));
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_off");
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath(
				"block/",
				this.getId(),
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
			new TagOrItem(ItemTags.WOODEN_PRESSURE_PLATES),
			new TagOrItem(Items.STONE_PRESSURE_PLATE),
			new TagOrItem(Items.LIGHT_WEIGHTED_PRESSURE_PLATE),
			new TagOrItem(Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
		));
	}
}