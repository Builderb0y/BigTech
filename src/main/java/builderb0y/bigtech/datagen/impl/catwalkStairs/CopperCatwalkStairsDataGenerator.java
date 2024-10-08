package builderb0y.bigtech.datagen.impl.catwalkStairs;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperCatwalkStairsDataGenerator extends MetalCatwalkStairsDataGenerator {

	public final CopperRegistrableCollection.Type type;

	public CopperCatwalkStairsDataGenerator(BlockItem blockItem, CopperRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeCatwalkStairsBlockModels(
			context,
			BigTechMod.modID(this.type.notWaxed().copperPrefix + "slab"),
			BigTechMod.modID(this.type.notWaxed().copperPrefix + "catwalk_stairs")
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeCatwalkStairsItemModels(
			context,
			BigTechMod.modID(this.type.notWaxed().copperPrefix + "slab"),
			BigTechMod.modID(this.type.notWaxed().copperPrefix + "catwalk_stairs")
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.COPPER_CATWALK_STAIRS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.COPPER_CATWALK_STAIRS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		if (this.type == CopperRegistrableCollection.Type.COPPER) {
			this.writeCatwalkStairsRecipe(
				context,
				new TagOrItem(FunctionalItems.MEDIUM_WEIGHTED_PRESSURE_PLATE),
				new TagOrItem(DecoItems.COPPER_BARS.copper)
			);
		}
		else if (this.type.waxed) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapelessRecipeBuilder()
				.category(CraftingRecipeCategory.BUILDING)
				.group("bigtech:waxed_copper_catwalk_stairs")
				.ingredient(DecoItems.COPPER_CATWALK_STAIRS.get(this.type.notWaxed()))
				.ingredient(Items.HONEYCOMB)
				.result(this.getId())
				.toString()
			);
		}
	}
}