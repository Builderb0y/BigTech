package builderb0y.bigtech.datagen.impl.magnets;

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
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.impl.magnets.MagneticBlockDataGenerator.ElectromagneticBlockDataGenerator;

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
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_bottom_top"))
			.blockTexture("bottom", BigTechMod.modID("electromagnet_bottom"))
			.blockTexture("top", BigTechMod.modID("electromagnet_top"))
			.blockTexture("side", BigTechMod.modID("electromagnet_side"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_off")),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_bottom_top"))
			.blockTexture("bottom", BigTechMod.modID("iron_coil_end"))
			.blockTexture("top", BigTechMod.modID("iron_coil_end"))
			.blockTexture("side", BigTechMod.modID("iron_coil_side"))
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