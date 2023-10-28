package builderb0y.bigtech.datagen.impl.catwalkPlatforms;

import java.util.Arrays;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonMultipart;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

@Dependencies(CommonCatwalkPlatformDataGenerator.class)
public abstract class CatwalkPlatformDataGenerator extends BasicBlockDataGenerator {

	public CatwalkPlatformDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonMultipart.FORMAT)
			.addRow(new BlockStateJsonMultipart(
				null,
				null,
				context.prefixSuffixPath("block/", this.id, "_base").toString(),
				null,
				null
			))
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER)
				.map(direction -> new BlockStateJsonMultipart(
					direction.name,
					"true",
					context.prefixSuffixPath("block/", this.id, "_rail").toString(),
					null,
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public abstract void writeBlockModels(DataGenContext context);

	public void writeCatwalkPlatformBlockModels(DataGenContext context, Identifier baseTexture, Identifier railTexture) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_base")),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_catwalk_platform_base"))
			.blockTexture("base", baseTexture)
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_rail")),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_catwalk_platform_rail"))
			.blockTexture("rail", railTexture)
			.toString()
		);
	}

	@Override
	public abstract void writeItemModels(DataGenContext context);

	public void writeCatwalkPlatformItemModels(DataGenContext context, Identifier baseTexture, Identifier railTexture) {
		context.writeToFile(
			context.itemModelPath(this.id),
			new RetexturedModelBuilder()
			.itemParent(BigTechMod.modID("template_catwalk_platform"))
			.blockTexture("base", baseTexture)
			.blockTexture("rail", railTexture)
			.toString()
		);
	}

	public void writeCatwalkPlatformRecipe(DataGenContext context, TagOrItem baseItem, TagOrItem railItem) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.group("bigtech:catwalk_platforms")
			.pattern("r r", "bbb")
			.ingredient('b', baseItem)
			.ingredient('r', railItem)
			.result(this.id)
			.count(4)
			.toString()
		);
	}
}