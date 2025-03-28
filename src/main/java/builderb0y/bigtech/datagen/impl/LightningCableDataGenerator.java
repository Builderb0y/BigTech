package builderb0y.bigtech.datagen.impl;

import java.util.Arrays;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Direction;

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

@Dependencies(CommonLightningDataGenerator.class)
public class LightningCableDataGenerator extends BasicBlockDataGenerator {

	public final TagKey<Item> ingot;

	public LightningCableDataGenerator(BlockItem blockItem, TagKey<Item> ingot) {
		super(blockItem);
		this.ingot = ingot;
	}

	public static class Iron extends LightningCableDataGenerator {

		public Iron(BlockItem blockItem) {
			super(blockItem, ConventionalItemTags.IRON_INGOTS);
		}
	}

	public static class Steel extends LightningCableDataGenerator {

		public Steel(BlockItem blockItem) {
			super(blockItem, BigTechItemTags.STEEL_INGOTS);
		}
	}

	public static class Copper extends LightningCableDataGenerator {

		public Copper(BlockItem blockItem) {
			super(blockItem, ConventionalItemTags.COPPER_INGOTS);
		}
	}

	public static class Gold extends LightningCableDataGenerator {

		public Gold(BlockItem blockItem) {
			super(blockItem, ConventionalItemTags.GOLD_INGOTS);
		}
	}

	public static class Silver extends LightningCableDataGenerator {

		public Silver(BlockItem blockItem) {
			super(blockItem, BigTechItemTags.SILVER_INGOTS);
		}
	}

	public static class Electrum extends LightningCableDataGenerator {

		public Electrum(BlockItem blockItem) {
			super(blockItem, BigTechItemTags.ELECTRUM_INGOTS);
		}
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonMultipart.FORMAT)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.FACING_ORDER)
				.map((Direction direction) -> new BlockStateJsonMultipart(
					direction.asString(),
					"true",
					context.prefixSuffixPath("block/", this.getId(), "_connection").toString(),
					BlockStateJsonVariant.xFromUp(direction),
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.FACING_ORDER)
				.map((Direction direction) -> new BlockStateJsonMultipart(
					direction.asString(),
					"false",
					context.prefixSuffixPath("block/", this.getId(), "_center").toString(),
					BlockStateJsonVariant.xFromUp(direction),
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_center")),
			new RetexturedModelBuilder()
			.parent("bigtech:block/template_lightning_cable_center")
			.blockTexture("cable", this.getId())
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_connection")),
			new RetexturedModelBuilder()
			.parent("bigtech:block/template_lightning_cable_connection")
			.blockTexture("cable", this.getId())
			.toString()
		);
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
			.parent("bigtech:item/template_lightning_cable")
			.blockTexture("cable", this.getId())
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.SHEARS).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.LIGHTNING_CABLES).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.MISC)
			.group("bigtech:lightning_cables")
			.pattern("www", "iii", "www")
			.where('w', ItemTags.WOOL)
			.where('i', this.ingot)
			.result(this.getId())
			.count(6)
			.toString()
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.LIGHTNING_CABLES).addElement(this.getId());
	}
}