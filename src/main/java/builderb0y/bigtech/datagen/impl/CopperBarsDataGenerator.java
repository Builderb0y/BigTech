package builderb0y.bigtech.datagen.impl;

import java.util.Arrays;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
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
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonMultipart;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

@Dependencies(CommonBarsDataGenerator.class)
public class CopperBarsDataGenerator extends BasicBlockDataGenerator {

	public final CopperRegistrableCollection.Type type;

	public CopperBarsDataGenerator(BlockItem blockItem, CopperRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonMultipart.FORMAT)
			.addRow(new BlockStateJsonMultipart(
				null, null, "bigtech:block/${this.type.notWaxed().copperPrefix}bars_center", null, null
			))
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER)
				.map((Direction direction) -> new BlockStateJsonMultipart(
					direction.getName(),
					"true",
					"bigtech:block/${this.type.notWaxed().copperPrefix}bars_${direction.getName()}",
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
		if (!this.type.waxed) {
			for (String suffix : new String[] { "_center", "_north", "_east", "_south", "_west" }) {
				context.writeToFile(
					context.blockModelPath(context.suffixPath(this.getId(), suffix)),
					new RetexturedModelBuilder()
					.blockParent(BigTechMod.modID("template_bars${suffix}"))
					.blockTexture("bars", BigTechMod.modID("${this.type.copperPrefix}bars"))
					.toString()
				);
			}
		}
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.getId()),
			new RetexturedModelBuilder()
			.itemParent(Identifier.ofVanilla("generated"))
			.blockTexture("layer0", BigTechMod.modID(this.type.notWaxed().copperPrefix + "bars"))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		if (this.type == CopperRegistrableCollection.Type.COPPER) {
			context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.COPPER_BARS);
		}
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		if (this.type == CopperRegistrableCollection.Type.COPPER) {
			context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.COPPER_BARS);
		}
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.COPPER_BARS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.COPPER_BARS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		if (this.type == CopperRegistrableCollection.Type.COPPER) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapedRecipeBuilder()
				.category(CraftingRecipeCategory.BUILDING)
				.pattern("ccc", "ccc")
				.where('c', ConventionalItemTags.COPPER_INGOTS)
				.result(this.getId())
				.count(16)
				.toString()
			);
		}
		else if (this.type.waxed) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapelessRecipeBuilder()
				.category(CraftingRecipeCategory.BUILDING)
				.group(BigTechMod.modID("waxed_copper_bars"))
				.ingredient(DecoItems.COPPER_BARS.get(this.type.notWaxed()))
				.ingredient(Items.HONEYCOMB)
				.result(this.getId())
				.toString()
			);
		}
	}
}