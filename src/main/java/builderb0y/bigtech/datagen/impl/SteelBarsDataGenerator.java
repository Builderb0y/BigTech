package builderb0y.bigtech.datagen.impl;

import java.util.Arrays;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonMultipart;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;

@Dependencies(CommonBarsDataGenerator.class)
public class SteelBarsDataGenerator extends BasicBlockDataGenerator {

	public SteelBarsDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.IRON).addElement(this.getId());
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonMultipart.FORMAT)
			.addRow(new BlockStateJsonMultipart(
				null, null, "bigtech:block/steel_bars_center", null, null
			))
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER)
				.map((Direction direction) -> new BlockStateJsonMultipart(
					direction.asString(),
					"true",
					"bigtech:block/steel_bars_${direction.asString()}",
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
		for (String suffix : new String[] { "_center", "_north", "_east", "_south", "_west" }) {
			context.writeToFile(
				context.blockModelPath(context.suffixPath(this.getId(), suffix)),
				new RetexturedModelBuilder()
				.blockParent(BigTechMod.modID("template_bars${suffix}"))
				.blockTexture("bars", BigTechMod.modID("steel_bars"))
				.toString()
			);
		}
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		this.writeDefaultItemDefinition(context, context.prefixPath("item/", this.getId()));
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.getId()),
			new RetexturedModelBuilder()
			.itemParent(Identifier.ofVanilla("generated"))
			.blockTexture("layer0", BigTechMod.modID("steel_bars"))
			.toString()
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.CONDUCTS_LIGHTNING).addElement(this.getId());
		context.getTags(BigTechBlockTags.SHOCKS_ENTITIES).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.pattern("sss", "sss")
			.where('s', BigTechItemTags.STEEL_INGOTS)
			.result(this.getId())
			.count(16)
			.toString()
		);
	}
}