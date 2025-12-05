package builderb0y.bigtech.datagen.impl.material.redstone;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Models;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class RedstoneAlloyBlockDataGenerator extends BasicBlockDataGenerator {

	public RedstoneAlloyBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
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
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.IRON).addElement(this.getId());
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.REDSTONE_ALLOY_BLOCKS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.REDSTONE_ALLOY_BLOCKS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.STORAGE_BLOCKS_REDSTONE)
			.input(ConventionalItemTags.STORAGE_BLOCKS_COPPER)
			.slowCoolResult(this.getId())
			.energy(4000)
			.coolingRate(10)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_ingots")),
			new ShapedRecipeBuilder()
			.pattern("iii", "iii", "iii")
			.where('i', BigTechItemTags.REDSTONE_ALLOY_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}