package builderb0y.bigtech.datagen.impl.belts;

import java.util.List;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.BigTechItems;

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
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_paper")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("ppp", "isi")
			.itemIngredient('p', Items.PAPER)
			.tagIngredient('i', ConventionalItemTags.IRON_INGOTS)
			.tagIngredient('s', BigTechItemTags.PRESSURE_PLATES)
			.result(BigTechItems.DETECTOR_BELT)
			.count(3)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_leather")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("lll", "isi")
			.itemIngredient('l', Items.LEATHER)
			.tagIngredient('i', ConventionalItemTags.IRON_INGOTS)
			.tagIngredient('s', BigTechItemTags.PRESSURE_PLATES)
			.result(BigTechItems.DETECTOR_BELT)
			.count(6)
			.toString()
		);
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