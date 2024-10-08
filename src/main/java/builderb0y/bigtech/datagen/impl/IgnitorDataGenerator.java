package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;

public class IgnitorDataGenerator extends BasicBlockDataGenerator {

	public IgnitorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(Properties.LIT) ? "_lit" : "").toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("orientable_with_bottom"))
			.blockTexture("front", BigTechMod.modID("ignitor_front"))
			.blockTexture("side", BigTechMod.modID("ignitor_side"))
			.blockTexture("top", BigTechMod.modID("ignitor_top"))
			.blockTexture("bottom", Identifier.ofVanilla("smooth_stone"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_lit")),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("orientable_with_bottom"))
			.blockTexture("front", BigTechMod.modID("ignitor_front_lit"))
			.blockTexture("side", BigTechMod.modID("ignitor_side"))
			.blockTexture("top", BigTechMod.modID("ignitor_top_lit"))
			.blockTexture("bottom", Identifier.ofVanilla("smooth_stone"))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.BELT_SUPPORT).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("did", "d d", "ddd")
			.where('d', Items.COBBLED_DEEPSLATE)
			.where('i', Items.IRON_BARS)
			.result(this.getId())
			.toString()
		);
	}
}