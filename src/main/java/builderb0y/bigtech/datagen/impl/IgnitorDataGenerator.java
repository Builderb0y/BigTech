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
import builderb0y.bigtech.datagen.tables.Table;

public class IgnitorDataGenerator extends BasicBlockDataGenerator {

	public IgnitorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.block)
				.map((BlockState state) -> new BlockStateJsonVariant(
					state,
					context.prefixSuffixPath("block/", this.id, state.get(Properties.LIT) ? "_lit" : "").toString(),
					null,
					BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.id),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "orientable_with_bottom"))
			.blockTexture("front", BigTechMod.modID("ignitor_front"))
			.blockTexture("side", BigTechMod.modID("ignitor_side"))
			.blockTexture("top", BigTechMod.modID("ignitor_top"))
			.blockTexture("bottom", new Identifier("minecraft", "smooth_stone"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_lit")),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "orientable_with_bottom"))
			.blockTexture("front", BigTechMod.modID("ignitor_front_lit"))
			.blockTexture("side", BigTechMod.modID("ignitor_side"))
			.blockTexture("top", BigTechMod.modID("ignitor_top_lit"))
			.blockTexture("bottom", new Identifier("minecraft", "smooth_stone"))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.BELT_SUPPORT).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.pattern("did", "d d", "ddd")
			.where('d', Items.COBBLED_DEEPSLATE)
			.where('i', Items.IRON_BARS)
			.result(this.id)
			.toString()
		);
	}
}