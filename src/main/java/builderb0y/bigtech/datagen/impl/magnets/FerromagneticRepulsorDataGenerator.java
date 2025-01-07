package builderb0y.bigtech.datagen.impl.magnets;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.impl.magnets.MagneticBlockDataGenerator.FerromagneticBlockDataGenerator;
import builderb0y.bigtech.items.FunctionalItems;

public class FerromagneticRepulsorDataGenerator extends FerromagneticBlockDataGenerator {

	public FerromagneticRepulsorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_all"))
			.blockTexture("all", BigTechMod.modID("ferromagnet_top"))
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
			.where('a', FunctionalItems.FERROMAGNETIC_ATTRACTOR)
			.result(FunctionalItems.FERROMAGNETIC_REPULSOR)
			.toString()
		);
	}
}