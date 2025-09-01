package builderb0y.bigtech.datagen.impl.functional.magnets;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.impl.functional.magnets.MagneticBlockDataGenerator.FerromagneticBlockDataGenerator;
import builderb0y.bigtech.items.MaterialItems;

public class FerromagneticTranslatorDataGenerator extends FerromagneticBlockDataGenerator {

	public FerromagneticTranslatorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		Direction direction = state.get(Properties.FACING);
		return new BlockStateJsonVariant(
			state,
			context.prefixPath("block/", this.getId()).toString(),
			BlockStateJsonVariant.xFromUp(direction),
			BlockStateJsonVariant.yFromNorth(direction)
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_bottom_top"))
			.blockTexture("bottom", BigTechMod.modID("ferromagnet_bottom"))
			.blockTexture("top", BigTechMod.modID("ferromagnet_top"))
			.blockTexture("side", BigTechMod.modID("ferromagnet_side"))
			.toString()
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_horizontal")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:magnets")
			.pattern("fff", "iii", "fff")
			.where('f', MaterialItems.FERROMAGNETIC_INGOT)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_vertical")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:magnets")
			.pattern("fif", "fif", "fif")
			.where('f', MaterialItems.FERROMAGNETIC_INGOT)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}