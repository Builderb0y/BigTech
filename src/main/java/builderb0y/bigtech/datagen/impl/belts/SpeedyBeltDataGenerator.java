package builderb0y.bigtech.datagen.impl.belts;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItems;

public class SpeedyBeltDataGenerator extends DirectionalBeltDataGenerator {

	public SpeedyBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			"bigtech:block/belts/speedy",
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeBeltBlockModel(context, "speedy");
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeBeltItemModel(context, "speedy");
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("speedy_belt_from_paper")),
			new ShapedRecipeBuilder()
			.group("bigtech:belts")
			.pattern("ppp", "i i")
			.itemIngredient('p', Items.PAPER)
			.tagIngredient('i', ConventionalItemTags.GOLD_INGOTS)
			.result(BigTechItems.SPEEDY_BELT)
			.count(3)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("speedy_belt_from_leather")),
			new ShapedRecipeBuilder()
			.group("bigtech:belts")
			.pattern("lll", "i i")
			.itemIngredient('l', Items.LEATHER)
			.tagIngredient('i', ConventionalItemTags.GOLD_INGOTS)
			.result(BigTechItems.SPEEDY_BELT)
			.count(6)
			.toString()
		);
	}
}