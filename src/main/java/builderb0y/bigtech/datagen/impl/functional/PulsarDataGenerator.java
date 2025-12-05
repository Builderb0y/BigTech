package builderb0y.bigtech.datagen.impl.functional;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Models;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;

public class PulsarDataGenerator extends BasicBlockDataGenerator {

	public PulsarDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(Properties.POWERED) ? "_on" : "_off").toString(),
			null,
			null
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		for (String suffix : new String[] { "_on", "_off" }) {
			context.writeToFile(
				context.blockModelPath(context.suffixPath(this.getId(), suffix)),
				new Models.block.cube_all()
				.all(context.suffixPath(this.getId(), suffix))
				.toString()
			);
		}
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_on");
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

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("wrw", "rqr", "wrw")
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.where('w', ItemTags.PLANKS)
			.where('q', Items.QUARTZ)
			.result(this.getId())
			.toString()
		);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put(PulsarTexts.TITLE, "Pulsar");
		context.lang.put(PulsarTexts.ON_TIME, "On Time:");
		context.lang.put(PulsarTexts.OFF_TIME, "Off Time:");
		context.lang.put(PulsarTexts.RELATIVE_TO, "Relative To:");
		context.lang.put(PulsarTexts.WORLD_AGE, "World Age");
		context.lang.put(PulsarTexts.DAY_NIGHT, "Day/Night");
		context.lang.put(PulsarTexts.OFFSET, "Offset:");
		context.lang.put(PulsarTexts.DONE, "Done");
		context.lang.put(PulsarTexts.CANCEL, "Cancel");
	}

	public static class PulsarTexts {

		public static final String PREFIX = "gui.bigtech.pulsar.";

		public static final String
			TITLE       = "${PREFIX}title",
			ON_TIME     = "${PREFIX}on_time",
			OFF_TIME    = "${PREFIX}off_time",
			RELATIVE_TO = "${PREFIX}relative_to",
			WORLD_AGE   = "${PREFIX}world_age",
			DAY_NIGHT   = "${PREFIX}day_night",
			OFFSET      = "${PREFIX}offset",
			DONE        = "${PREFIX}done",
			CANCEL      = "${PREFIX}cancel";
	}
}