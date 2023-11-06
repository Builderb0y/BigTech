package builderb0y.bigtech.datagen.impl;

import java.util.Arrays;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

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

	public LightningCableDataGenerator(BlockItem blockItem) {
		super(blockItem);
		this.ingot = TagKey.of(RegistryKeys.ITEM, new Identifier("c", this.id.path.substring(0, this.id.path.length() - "_lightning_cable".length()) + "_ingots"));
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonMultipart.FORMAT)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.FACING_ORDER)
				.map(direction -> new BlockStateJsonMultipart(
					direction.asString(),
					"true",
					context.prefixSuffixPath("block/", this.id, "_connection").toString(),
					BlockStateJsonVariant.xFromUp(direction),
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.FACING_ORDER)
				.map(direction -> new BlockStateJsonMultipart(
					direction.asString(),
					"false",
					context.prefixSuffixPath("block/", this.id, "_center").toString(),
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
			context.blockModelPath(context.suffixPath(this.id, "_center")),
			new RetexturedModelBuilder()
			.parent("bigtech:block/template_lightning_cable_center")
			.blockTexture("cable", this.id)
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_connection")),
			new RetexturedModelBuilder()
			.parent("bigtech:block/template_lightning_cable_connection")
			.blockTexture("cable", this.id)
			.toString()
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.id),
			new RetexturedModelBuilder()
			.parent("bigtech:item/template_lightning_cable")
			.blockTexture("cable", this.id)
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.SHEARS).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.LIGHTNING_CABLES).addElement(this.id);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.MISC)
			.group("bigtech:lightning_cables")
			.pattern("www", "iii", "www")
			.where('w', ItemTags.WOOL)
			.where('i', this.ingot)
			.result(this.id)
			.count(6)
			.toString()
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.LIGHTNING_CABLES).addElement(this.id);
	}
}