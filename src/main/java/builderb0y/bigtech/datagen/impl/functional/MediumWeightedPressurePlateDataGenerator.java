package builderb0y.bigtech.datagen.impl.functional;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
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
import builderb0y.bigtech.items.BigTechItemTags;

public class MediumWeightedPressurePlateDataGenerator extends BasicBlockDataGenerator {

	public MediumWeightedPressurePlateDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(Properties.POWER) != 0 ? "_down" : "_up").toString(),
			null,
			null
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_up")),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("pressure_plate_up"))
			.blockTexture("texture", this.getId())
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_down")),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("pressure_plate_down"))
			.blockTexture("texture", this.getId())
			.toString()
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return BigTechMod.modID("medium_weighted_pressure_plate_up");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.STONE).addElement(this.getId());
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BlockTags.PRESSURE_PLATES).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.PRESSURE_PLATES).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("cc")
			.where('c', ConventionalItemTags.COPPER_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}