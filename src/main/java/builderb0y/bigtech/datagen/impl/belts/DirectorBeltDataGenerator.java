package builderb0y.bigtech.datagen.impl.belts;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItems;

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
		context.writeToFile(
			context.recipePath(BigTechMod.modID("director_belt_from_paper")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("ppp", "iri")
			.itemIngredient('p', Items.PAPER)
			.tagIngredient('i', ConventionalItemTags.IRON_INGOTS)
			.tagIngredient('r', ConventionalItemTags.REDSTONE_DUSTS)
			.result(BigTechItems.DIRECTOR_BELT)
			.count(3)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("director_belt_from_leather")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("lll", "iri")
			.itemIngredient('l', Items.LEATHER)
			.tagIngredient('i', ConventionalItemTags.IRON_INGOTS)
			.tagIngredient('r', ConventionalItemTags.REDSTONE_DUSTS)
			.result(BigTechItems.DIRECTOR_BELT)
			.count(6)
			.toString()
		);
	}
}