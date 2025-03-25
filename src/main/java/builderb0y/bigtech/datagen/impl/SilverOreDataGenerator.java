package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class SilverOreDataGenerator extends BasicBlockDataGenerator {

	public SilverOreDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void run(DataGenContext context) {
		super.run(context);
		this.writeWorldgen(context);
	}

	public void writeWorldgen(DataGenContext context) {
		context.writeToFile(
			"data/bigtech/worldgen/configured_feature/ore_silver_buried.json",
			//language=json
			"""
			{
				"type": "minecraft:ore",
				"config": {
					"discard_chance_on_air_exposure": 0.5,
					"size": 9,
					"targets": [
						{
							"state": {
								"Name": "bigtech:silver_ore"
							},
							"target": {
								"predicate_type": "minecraft:tag_match",
								"tag": "minecraft:stone_ore_replaceables"
							}
						},
						{
							"state": {
								"Name": "bigtech:deepslate_silver_ore"
							},
							"target": {
								"predicate_type": "minecraft:tag_match",
								"tag": "minecraft:deepslate_ore_replaceables"
							}
						}
					]
				}
			}"""
		);
		context.writeToFile(
			"data/bigtech/worldgen/placed_feature/ore_silver.json",
			//language=json
			"""
			{
				"feature": "bigtech:ore_silver_buried",
				"placement": [
					{
						"type": "minecraft:count",
						"count": 4
					},
					{
						"type": "minecraft:in_square"
					},
					{
						"type": "minecraft:height_range",
						"height": {
							"type": "minecraft:trapezoid",
							"max_inclusive": {
								"absolute": 32
							},
							"min_inclusive": {
								"absolute": -64
							}
						}
					},
					{
						"type": "minecraft:biome"
					}
				]
			}"""
		);
		context.writeToFile(
			"data/bigtech/worldgen/placed_feature/ore_silver_lower.json",
			//language=json
			"""
			{
				"feature": "bigtech:ore_silver_buried",
				"placement": [
					{
						"type": "minecraft:count",
						"count": {
							"type": "minecraft:uniform",
							"max_inclusive": 1,
							"min_inclusive": 0
						}
					},
					{
						"type": "minecraft:in_square"
					},
					{
						"type": "minecraft:height_range",
						"height": {
							"type": "minecraft:uniform",
							"max_inclusive": {
								"absolute": -48
							},
							"min_inclusive": {
								"absolute": -64
							}
						}
					},
					{
						"type": "minecraft:biome"
					}
				]
			 }"""
		);
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
								"name": "bigtech:silver_ore",
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
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_all"))
			.blockTexture("all", this.getId())
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