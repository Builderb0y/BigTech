package builderb0y.bigtech.datagen.impl.frames;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.FrameBlock;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;

@Dependencies(CommonFrameDataGenerator.class)
public abstract class FrameDataGenerator extends BasicBlockDataGenerator {

	public FrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	public void writeFrameRecipe(DataGenContext context, TagOrItem cornerIngredient, TagOrItem edgeIngredient) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.group("bigtech:frames")
			.pattern("ini", "n n", "ini")
			.ingredient('i', cornerIngredient)
			.ingredient('n', edgeIngredient)
			.result(this.id)
			.count(4)
			.toString()
		);
	}

	public Identifier getBaseTexture() {
		return this.id;
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.id),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_frame"))
			.blockTexture("outer", context.suffixPath(this.baseTexture, "_outer"))
			.blockTexture("inner", context.suffixPath(this.baseTexture, "_inner"))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//handled by CommonFrameDataGenerator.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//handled by CommonFrameDataGenerator.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(this.block.<FrameBlock>as().sticksTo).addElement(this.id);
	}
}