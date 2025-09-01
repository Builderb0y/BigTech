package builderb0y.bigtech.datagen.impl.deco.catwalkPlatforms;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperCatwalkPlatformDataGenerator extends MetalCatwalkPlatformDataGenerator {

	public final CopperRegistrableCollection.Type type;

	public CopperCatwalkPlatformDataGenerator(BlockItem blockItem, CopperRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
	}

	@Override
	public Identifier getBlockModelIdentifier() {
		return BigTechMod.modID(this.type.notWaxed().copperPrefix + "catwalk_platform");
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		if (!this.type.waxed) {
			this.writeCatwalkPlatformBlockModels(
				context,
				Registries.BLOCK.getId(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(this.type.notWaxed())),
				BigTechMod.modID(this.type.notWaxed().copperPrefix + "catwalk_platform_rail")
			);
		}
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeCatwalkPlatformItemModels(
			context,
			Registries.BLOCK.getId(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(this.type.notWaxed())),
			BigTechMod.modID(this.type.notWaxed().copperPrefix + "catwalk_platform_rail")
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.COPPER_CATWALK_PLATFORMS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.COPPER_CATWALK_PLATFORMS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		if (this.type == CopperRegistrableCollection.Type.COPPER) {
			this.writeCatwalkPlatformRecipe(
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
				.group("bigtech:waxed_copper_catwalk_platforms")
				.ingredient(DecoItems.COPPER_CATWALK_PLATFORMS.get(this.type.notWaxed()))
				.ingredient(Items.HONEYCOMB)
				.result(this.getId())
				.toString()
			);
		}
	}
}