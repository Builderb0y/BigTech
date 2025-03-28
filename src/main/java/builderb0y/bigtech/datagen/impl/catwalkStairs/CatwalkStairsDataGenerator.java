package builderb0y.bigtech.datagen.impl.catwalkStairs;

import java.util.Arrays;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonMultipart3;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

@Dependencies(CommonCatwalkStairsDataGenerator.class)
public abstract class CatwalkStairsDataGenerator extends BasicBlockDataGenerator {

	public CatwalkStairsDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonMultipart3.FORMAT)
			.addRows(
				Arrays.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER).map((Direction direction) -> new BlockStateJsonMultipart3(
					"half",
					"lower",
					"facing",
					direction.asString(),
					null,
					null,
					context.prefixSuffixPath("block/", this.getId(), "_base").toString(),
					null,
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.addRows(
				Arrays.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER).map((Direction direction) -> new BlockStateJsonMultipart3(
					"half",
					"lower",
					"facing",
					direction.asString(),
					"left",
					"true",
					context.prefixSuffixPath("block/", this.getId(), "_rail_left").toString(),
					null,
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.addRows(
				Arrays.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER).map((Direction direction) -> new BlockStateJsonMultipart3(
					"half",
					"lower",
					"facing",
					direction.asString(),
					"right",
					"true",
					context.prefixSuffixPath("block/", this.getId(), "_rail_right").toString(),
					null,
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.toString()
		);
	}

	public void writeCatwalkStairsBlockModels(DataGenContext context, Identifier baseTexture, Identifier stairsTexture) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_base")),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_catwalk_stairs_base"))
			.blockTexture("base", baseTexture)
			.blockTexture("stairs", stairsTexture)
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_rail_left")),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_catwalk_stairs_rail_left"))
			.blockTexture("stairs", stairsTexture)
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_rail_right")),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_catwalk_stairs_rail_right"))
			.blockTexture("stairs", stairsTexture)
			.toString()
		);
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		this.writeDefaultItemDefinition(context, context.prefixPath("item/", this.getId()));
	}

	@Override
	public abstract void writeItemModels(DataGenContext context);

	public void writeCatwalkStairsItemModels(DataGenContext context, Identifier baseTexture, Identifier stairsTexture) {
		context.writeToFile(
			context.itemModelPath(this.getId()),
			new RetexturedModelBuilder()
			.itemParent(BigTechMod.modID("template_catwalk_stairs"))
			.blockTexture("base", baseTexture)
			.blockTexture("stairs", stairsTexture)
			.toString()
		);
	}

	public void writeCatwalkStairsRecipe(DataGenContext context, TagOrItem baseItem, TagOrItem railItem) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.group("bigtech:catwalk_stairs")
			.pattern("r ", "br", " b")
			.where('b', baseItem)
			.where('r', railItem)
			.result(this.getId())
			.count(2)
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//handled by CommonCatwalkStairsDataGenerator.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//handled by CommonCatwalkStairsDataGenerator.
	}
}