package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;

public class EncasedRedstoneBlockDataGenerator extends BasicBlockDataGenerator {

	public EncasedRedstoneBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			BlockStateJsonVariant
			.streamStatesSorted(this.block)
			.map(state -> new BlockStateJsonVariant(
				state,
				context.prefixPath("block/", this.id).toString(),
				BlockStateJsonVariant.xFromUp(state.get(Properties.FACING)),
				BlockStateJsonVariant.yFromNorth(state.get(Properties.FACING))
			))
			.collect(Table.collector(BlockStateJsonVariant.FORMAT))
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.id),
			new RetexturedModelBuilder()
			.parent("minecraft:block/cube_bottom_top")
			.blockTexture("top",    context.suffixPath(this.id, "_front"))
			.texture     ("bottom", "minecraft:block/furnace_top")
			.blockTexture("side",   context.suffixPath(this.id, "_side"))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("crc", "c c", "ccc")
			.where('c', ItemTags.STONE_CRAFTING_MATERIALS)
			.where('r', BigTechItemTags.REDSTONE_BLOCKS)
			.result(this.id)
			.toString()
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.REDSTONE_BLOCKS).add(Items.REDSTONE_BLOCK);
	}
}