package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
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
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

public class SpotlightDataGenerator extends BasicBlockDataGenerator {

	public SpotlightDataGenerator(BlockItem blockItem) {
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
					context.prefixSuffixPath("block/", this.id, state.get(Properties.POWERED) ? "_on" : "_off").toString(),
					BlockStateJsonVariant.xFromUp(state.get(Properties.FACING)),
					BlockStateJsonVariant.yFromNorth(state.get(Properties.FACING))
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_off")),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "cube_bottom_top"))
			.blockTexture("top", BigTechMod.modID("spotlight_top_off"))
			.blockTexture("bottom", BigTechMod.modID("spotlight_bottom"))
			.blockTexture("side", BigTechMod.modID("spotlight_side"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_on")),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "cube_bottom_top"))
			.blockTexture("top", BigTechMod.modID("spotlight_top_on"))
			.blockTexture("bottom", BigTechMod.modID("spotlight_bottom"))
			.blockTexture("side", BigTechMod.modID("spotlight_side"))
			.toString()
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.id, "_off");
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

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.MISC)
			.pattern("iii", "lcg", "iii")
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.where('l', FunctionalItems.LENS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.where('g', Items.GLOWSTONE)
			.result(this.id)
			.toString()
		);
	}
}