package builderb0y.bigtech.datagen.impl.magnets;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.impl.magnets.MagneticBlockDataGenerator.FerromagneticBlockDataGenerator;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.items.MaterialItems;

public class FerromagneticAttractorDataGenerator extends FerromagneticBlockDataGenerator {

	public FerromagneticAttractorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_all"))
			.blockTexture("all", BigTechMod.modID("ferromagnet_bottom"))
			.toString()
		);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_ingots")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:magnets")
			.pattern("fff", "fif", "fff")
			.where('f', MaterialItems.FERROMAGNETIC_INGOT)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_conversion")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:magnets")
			.pattern("r")
			.where('r', FunctionalItems.FERROMAGNETIC_REPULSOR)
			.result(FunctionalItems.FERROMAGNETIC_ATTRACTOR)
			.toString()
		);
	}
}