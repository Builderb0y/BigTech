package builderb0y.bigtech.datagen.impl.frames;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperFrameDataGenerator extends MetalFrameDataGenerator {

	public final CopperRegistrableCollection.Type type;

	public CopperFrameDataGenerator(BlockItem blockItem, CopperRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
	}

	@Override
	public Identifier getBaseTexture() {
		if (this.type.waxed) {
			return Registries.BLOCK.getId(DecoBlocks.COPPER_FRAMES.get(this.type.notWaxed()));
		}
		return super.getBaseTexture();
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.COPPER_FRAMES).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.COPPER_FRAMES).addElement(this.id);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		if (this.type == CopperRegistrableCollection.Type.COPPER) {
			this.writeFrameRecipe(
				context,
				new TagOrItem(ConventionalItemTags.COPPER_INGOTS),
				new TagOrItem(BigTechItemTags.COPPER_NUGGETS)
			);
		}
		else if (this.type.waxed) {
			context.writeToFile(
				context.recipePath(this.id),
				new ShapelessRecipeBuilder()
				.category(CraftingRecipeCategory.BUILDING)
				.group(BigTechMod.modID("frames"))
				.ingredient(DecoItems.COPPER_FRAMES.get(this.type.notWaxed()))
				.ingredient(Items.HONEYCOMB)
				.result(this.item)
				.toString()
			);
		}
	}
}