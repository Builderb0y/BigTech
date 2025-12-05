package builderb0y.bigtech.datagen.impl.material;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Models;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.MaterialItems;

public class CastIronBlockDataGenerator extends BasicBlockDataGenerator {

	public CastIronBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return "Block of Cast Iron";
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
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new Models.block.cube_all()
			.all(this.getId())
			.toString()
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(ConventionalBlockTags.STORAGE_BLOCKS_IRON).addElement(this.getId());
		context.getTags(BlockTags.BEACON_BASE_BLOCKS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(ConventionalItemTags.STORAGE_BLOCKS_IRON).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("cast_iron_ingots_to_block")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.pattern("iii", "iii", "iii")
			.where('i', MaterialItems.CAST_IRON_INGOT)
			.result(this.getId())
			.toString()
		);
	}
}