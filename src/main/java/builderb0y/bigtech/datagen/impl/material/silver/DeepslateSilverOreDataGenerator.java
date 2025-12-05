package builderb0y.bigtech.datagen.impl.material.silver;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Models;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class DeepslateSilverOreDataGenerator extends BasicBlockDataGenerator {

	public DeepslateSilverOreDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeLootTableJson(DataGenContext context) {
		context.writeToFile(
			context.blockLootTablePath(this.getId()),
			//language=json
			"""
			{
				"type": "minecraft:block",
				"pools": [{
					"rolls": 1.0,
					"entries": [{
						"type": "minecraft:alternatives",
						"children": [
							{
								"type": "minecraft:item",
								"name": "bigtech:deepslate_silver_ore",
								"conditions": [{
									"condition": "minecraft:match_tool",
									"predicate": {
										"predicates": {
											"minecraft:enchantments": [{
												"enchantments": "minecraft:silk_touch",
												"levels": { "min": 1 }
											}]
										}
									}
								}]
							},
							{
								"type": "minecraft:item",
								"name": "bigtech:raw_silver",
								"functions": [
									{
										"function": "minecraft:apply_bonus",
										"enchantment": "minecraft:fortune",
										"formula": "minecraft:ore_drops"
									},
									{
										"function": "minecraft:explosion_decay"
									}
								]
							}
						]
					}]
				}]
			}"""
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
		context.getTags(BigTechBlockTags.SILVER_ORES).addElement(this.getId());
		context.getTags(ConventionalBlockTags.ORES).add(BigTechBlockTags.SILVER_ORES);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.SILVER_ORES).addElement(this.getId());
		context.getTags(ConventionalItemTags.ORES).add(BigTechItemTags.SILVER_ORES);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		//no-op.
	}
}