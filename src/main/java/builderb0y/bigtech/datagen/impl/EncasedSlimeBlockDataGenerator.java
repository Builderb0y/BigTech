package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.EncasedSlimeBlock;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public class EncasedSlimeBlockDataGenerator extends BasicBlockDataGenerator {

	public EncasedSlimeBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.getBlock())
				.map((BlockState state) -> new BlockStateJsonVariant(
					state,
					context.prefixPath("block/", this.getId()).toString(),
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
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_bottom_top"))
			.blockTexture("top", context.suffixPath(this.getId(), "_front"))
			.texture("bottom", "minecraft:block/furnace_top")
			.texture("side", "bigtech:block/encased_redstone_block_side")
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:encased_slime_blocks")
			.pattern("csc", "c c", "ccc")
			.where('c', ItemTags.STONE_CRAFTING_MATERIALS)
			.where('s', this.getBlock().<EncasedSlimeBlock>as().honey ? Items.HONEY_BLOCK : Items.SLIME_BLOCK)
			.result(this.getId())
			.toString()
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {}
}