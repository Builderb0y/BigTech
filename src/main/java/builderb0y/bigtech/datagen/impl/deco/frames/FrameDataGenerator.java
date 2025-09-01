package builderb0y.bigtech.datagen.impl.deco.frames;

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
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.group("bigtech:frames")
			.pattern("ini", "n n", "ini")
			.where('i', cornerIngredient)
			.where('n', edgeIngredient)
			.result(this.getId())
			.count(4)
			.toString()
		);
	}

	public Identifier getBaseTexture() {
		return this.getId();
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_frame"))
			.blockTexture("outer", context.suffixPath(this.getBaseTexture(), "_outer"))
			.blockTexture("inner", context.suffixPath(this.getBaseTexture(), "_inner"))
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
		context.getTags(this.getBlock().<FrameBlock>as().sticksTo).addElement(this.getId());
	}
}