package builderb0y.bigtech.datagen.impl.metalLadders;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperLadderDataGenerator extends MetalLadderDataGenerator {

	public final CopperRegistrableCollection.Type type;

	public CopperLadderDataGenerator(BlockItem blockItem, CopperRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
	}

	@Override
	public Identifier getTexture(DataGenContext context) {
		return Registries.BLOCK.getId(DecoBlocks.COPPER_LADDERS.get(this.type.notWaxed()));
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.COPPER_LADDERS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.COPPER_LADDERS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		if (this.type == CopperRegistrableCollection.Type.COPPER) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapedRecipeBuilder()
				.category(CraftingRecipeCategory.BUILDING)
				.group(BigTechMod.modID("copper_ladders"))
				.pattern("c c", "ccc", "c c")
				.where('c', BigTechItemTags.COPPER_NUGGETS)
				.result(this.getId())
				.count(3)
				.toString()
			);
		}
		else if (this.type.waxed) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapelessRecipeBuilder()
				.group(BigTechMod.modID("copper_ladders"))
				.ingredient(DecoItems.COPPER_LADDERS.get(this.type.notWaxed()))
				.ingredient(DecoItems.COPPER_LADDERS.get(this.type.notWaxed()))
				.ingredient(DecoItems.COPPER_LADDERS.get(this.type.notWaxed()))
				.ingredient(Items.HONEYCOMB)
				.result(this.getId())
				.count(3)
				.toString()
			);
		}
	}
}