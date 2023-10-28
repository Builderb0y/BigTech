package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;

public class MediumWeightedPressurePlateDataGenerator extends BasicBlockDataGenerator {

	public MediumWeightedPressurePlateDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant.streamStatesSorted(this.block).map(state -> new BlockStateJsonVariant(
					state,
					context.prefixSuffixPath("block/", this.id, state.get(Properties.POWER) != 0 ? "_down" : "_up").toString(),
					null,
					null
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_up")),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "pressure_plate_up"))
			.blockTexture("texture", BigTechMod.modID("medium_weighted_pressure_plate"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_down")),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "pressure_plate_down"))
			.blockTexture("texture", BigTechMod.modID("medium_weighted_pressure_plate"))
			.toString()
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.id),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("medium_weighted_pressure_plate_up"))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.STONE).addElement(this.id);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BlockTags.PRESSURE_PLATES).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.PRESSURE_PLATES).addElement(this.id);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("cc")
			.tagIngredient('c', ConventionalItemTags.COPPER_INGOTS)
			.result(this.id)
			.toString()
		);
	}
}