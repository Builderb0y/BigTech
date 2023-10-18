package builderb0y.bigtech.datagen.impl.belts;

import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.impl.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;

public class BeltDataGenerator extends BasicBlockDataGenerator {

	public BeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.block)
				.map(state -> new BlockStateJsonVariant(
					state,
					context.prefixPath("block/", this.id).toString(),
					null,
					BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_belt")),
			"""
			{
				"parent": "block/thin_block",
				"textures": {
					"particle": "#belt",
					"bottom": "bigtech:block/belts/bottom"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 1, 16 ],
						"faces": {
							"up":    { "uv": [  0,  0, 16, 16 ], "texture": "#belt"                                         },
							"down":  { "uv": [  0,  0, 16, 16 ], "texture": "#bottom", "cullface": "down"                   },
							"north": { "uv": [  0,  0, 16,  1 ], "texture": "#belt",   "cullface": "north", "rotation": 180 },
							"south": { "uv": [  0, 15, 16, 16 ], "texture": "#belt",   "cullface": "south"                  },
							"east":  { "uv": [ 15,  0, 16, 16 ], "texture": "#belt",   "cullface": "east",  "rotation": 90  },
							"west":  { "uv": [  0,  0,  1, 16 ], "texture": "#belt",   "cullface": "west",  "rotation": 270 }
						}
					}
				]
			}
			"""
		);
		context.writeToFile(
			context.blockModelPath(this.id),
			"""
			{
				"parent": "bigtech:block/template_belt",
				"textures": {
					"belt": "bigtech:block/belts/normal"
				}
			}
			"""
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.BELTS).add(this.id.toString());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.BELTS).add(this.id.toString());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("belt_from_paper")),
			"""
			{
				"type": "minecraft:crafting_shaped",
				"category": "redstone",
				"group": "bigtech:belts",
				"pattern": [
					"ppp",
					"i i"
				],
				"key": {
					"p": { "item": "minecraft:paper" },
					"i": { "tag": "c:iron_ingots" }
				},
				"result": {
					"item": "bigtech:belt",
					"count": 3
				}
			}
			"""
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("belt_from_leather")),
			"""
			{
				"type": "minecraft:crafting_shaped",
				"category": "redstone",
				"group": "bigtech:belts",
				"pattern": [
					"lll",
					"i i"
				],
				"key": {
					"l": { "item": "minecraft:leather" },
					"i": { "tag": "c:iron_ingots" }
				},
				"result": {
					"item": "bigtech:belt",
					"count": 6
				}
			}
			"""
		);
	}
}