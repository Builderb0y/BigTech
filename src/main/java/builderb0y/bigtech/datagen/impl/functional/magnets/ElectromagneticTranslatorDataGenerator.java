package builderb0y.bigtech.datagen.impl.functional.magnets;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Models;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.impl.functional.magnets.MagneticBlockDataGenerator.ElectromagneticBlockDataGenerator;

public class ElectromagneticTranslatorDataGenerator extends ElectromagneticBlockDataGenerator {

	public ElectromagneticTranslatorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		Direction direction = state.get(Properties.FACING);
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(Properties.POWERED) ? "_on": "_off").toString(),
			BlockStateJsonVariant.xFromUp(direction),
			BlockStateJsonVariant.yFromNorth(direction)
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_on");
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_on")),
			new Models.block.cube_bottom_top()
			.bottom(BigTechMod.modID("electromagnet_bottom"))
			.top(BigTechMod.modID("electromagnet_top"))
			.side(BigTechMod.modID("electromagnet_side"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_off")),
			new Models.block.cube_bottom_top()
			.bottom(BigTechMod.modID("iron_coil_end"))
			.top(BigTechMod.modID("iron_coil_end"))
			.side(BigTechMod.modID("iron_coil_side"))
			.toString()
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_horizontal")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group(BigTechMod.modID("magnets"))
			.pattern("iii", "www", "iii")
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.where('w', ItemTags.PLANKS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_vertical")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:magnets")
			.pattern("iwi", "iwi", "iwi")
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.where('w', ItemTags.PLANKS)
			.result(this.getId())
			.toString()
		);
	}
}