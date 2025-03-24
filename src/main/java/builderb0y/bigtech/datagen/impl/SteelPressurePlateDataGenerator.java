package builderb0y.bigtech.datagen.impl;

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

public class SteelPressurePlateDataGenerator extends BasicBlockDataGenerator {

	public SteelPressurePlateDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(Properties.POWERED) ? "_down" : "_up").toString(),
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
			.blockTexture("texture", BigTechMod.modID("steel_block"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_down")),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("pressure_plate_down"))
			.blockTexture("texture", BigTechMod.modID("steel_block"))
			.toString()
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return BigTechMod.modID("steel_pressure_plate_up");
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
			.pattern("ss")
			.where('s', BigTechItemTags.STEEL_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}