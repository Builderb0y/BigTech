package builderb0y.bigtech.datagen.impl.material.electrum;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class ElectrumBlockDataGenerator extends BasicBlockDataGenerator {

	public ElectrumBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
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
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_all"))
			.blockTexture("all", this.getId())
			.toString()
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.ELECTRUM_BLOCKS).addElement(this.getId());
		context.getTags(BigTechBlockTags.CONDUCTS_LIGHTNING).add(BigTechBlockTags.ELECTRUM_BLOCKS);
		context.getTags(BigTechBlockTags.SHOCKS_ENTITIES).add(BigTechBlockTags.ELECTRUM_BLOCKS);
		context.getTags(ConventionalBlockTags.STORAGE_BLOCKS).add(BigTechBlockTags.ELECTRUM_BLOCKS);
		context.getTags(BlockTags.BEACON_BASE_BLOCKS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.ELECTRUM_BLOCKS).addElement(this.getId());
		context.getTags(ConventionalItemTags.STORAGE_BLOCKS).add(BigTechItemTags.ELECTRUM_BLOCKS);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("sss", "sss", "sss")
			.where('s', BigTechItemTags.ELECTRUM_INGOTS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("electrum_block_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.STORAGE_BLOCKS_GOLD)
			.input(BigTechItemTags.SILVER_BLOCKS)
			.slowCoolResult(this.getId())
			.slowCoolQuantity(2)
			.energy(12000)
			.coolingRate(15)
			.toString()
		);
	}
}