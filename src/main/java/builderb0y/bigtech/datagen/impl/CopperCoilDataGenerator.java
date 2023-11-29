package builderb0y.bigtech.datagen.impl;

import java.util.Arrays;
import java.util.Objects;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.damageTypes.BigTechDamageTypes;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public class CopperCoilDataGenerator extends BasicBlockDataGenerator {

	public CopperCoilDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.FACING_ORDER)
				.map(direction -> new BlockStateJsonVariant(
					"facing=${direction.getName()}",
					context.prefixPath("block/", this.id).toString(),
					BlockStateJsonVariant.xFromUp(direction),
					Objects.requireNonNullElse(BlockStateJsonVariant.yFromNorth(direction), 0)
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
			.blockParent(new Identifier("minecraft", "cube_column"))
			.blockTexture("side", context.suffixPath(this.id, "_side"))
			.blockTexture("end", context.suffixPath(this.id, "_end"))
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

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.pattern("cpc", "cpc", "cpc")
			.where('c', ConventionalItemTags.COPPER_INGOTS)
			.where('p', ItemTags.PLANKS)
			.result(this.id)
			.toString()
		);
	}
}