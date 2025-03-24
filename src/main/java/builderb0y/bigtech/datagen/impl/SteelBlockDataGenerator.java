package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class SteelBlockDataGenerator extends BasicBlockDataGenerator {

	public SteelBlockDataGenerator(BlockItem blockItem) {
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
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.STEEL_BLOCKS).addElement(this.getId());
		context.getTags(BlockTags.BEACON_BASE_BLOCKS).add(BigTechBlockTags.STEEL_BLOCKS);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.STEEL_BLOCKS).addElement(this.getId());
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
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("steel_block_crafting")),
			new ShapedRecipeBuilder()
			.pattern("iii", "iii", "iii")
			.where('i', BigTechItemTags.STEEL_INGOTS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("steel_block_arc_furnace")),
			//language=json
			"""
			{
				"type": "bigtech:arc_furnace",
				"inputs": [
					"#c:storage_blocks/iron",
					"#c:storage_blocks/coal"
				],
				"slow_cool_result": { "id": "bigtech:steel_block" },
				"energy": 16000,
				"cooling_rate": 20
			}"""
		);
	}
}