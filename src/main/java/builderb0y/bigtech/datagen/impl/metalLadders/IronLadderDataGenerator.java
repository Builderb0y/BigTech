package builderb0y.bigtech.datagen.impl.metalLadders;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class IronLadderDataGenerator extends MetalLadderDataGenerator {

	public IronLadderDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.METAL_LADDERS).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.METAL_LADDERS).addElement(this.id);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.pattern("i i", "iii", "i i")
			.where('i', BigTechItemTags.IRON_NUGGETS)
			.result(this.id)
			.count(3)
			.toString()
		);
	}
}