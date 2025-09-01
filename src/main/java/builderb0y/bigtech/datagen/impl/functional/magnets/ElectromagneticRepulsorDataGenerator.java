package builderb0y.bigtech.datagen.impl.functional.magnets;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.impl.functional.magnets.MagneticBlockDataGenerator.ElectromagneticBlockDataGenerator;
import builderb0y.bigtech.items.FunctionalItems;

public class ElectromagneticRepulsorDataGenerator extends ElectromagneticBlockDataGenerator {

	public ElectromagneticRepulsorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(Properties.POWERED) ? "_on" : "_off").toString(),
			null,
			null
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
			.blockParent(Identifier.ofVanilla("cube_all"))
			.blockTexture("all", BigTechMod.modID("electromagnet_top"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_off")),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_all"))
			.blockTexture("all", BigTechMod.modID("iron_coil_end"))
			.toString()
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_conversion")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:magnets")
			.pattern("a")
			.where('a', FunctionalItems.ELECTROMAGNETIC_ATTRACTOR)
			.result(FunctionalItems.ELECTROMAGNETIC_REPULSOR)
			.toString()
		);
	}
}