package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;

@Dependencies(CommonTransmuterDataGenerator.class)
public class TransmuterDataGenerator extends BasicBlockDataGenerator {

	public TransmuterDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "minecraft:block/block",
				"textures": {
					"transmuter": "bigtech:block/transmuter",
					"outer": "bigtech:block/transmuter_corners_outer",
					"inner": "bigtech:block/transmuter_corners_inner",
					"particle": "#transmuter"
				},
				"elements": [
					{
						"from": [  1,  1,  1 ],
						"to":   [ 15, 15, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 1, 15, 15 ], "texture": "#transmuter" },
							"down":  { "uv": [ 1, 1, 15, 15 ], "texture": "#transmuter" },
							"north": { "uv": [ 1, 1, 15, 15 ], "texture": "#transmuter" },
							"east":  { "uv": [ 1, 1, 15, 15 ], "texture": "#transmuter" },
							"south": { "uv": [ 1, 1, 15, 15 ], "texture": "#transmuter" },
							"west":  { "uv": [ 1, 1, 15, 15 ], "texture": "#transmuter" }
						}
					},
					{
						"from": [  0,  0, 0 ],
						"to":   [ 16, 16, 3 ],
						"faces": {
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "north" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                      }
						}
					},
					{
						"from": [ 13,  0,  0 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"east": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "east" },
							"west": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                     }
						}
					},
					{
						"from": [  0,  0, 13 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                      },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "south" }
						}
					},
					{
						"from": [ 0,  0,  0 ],
						"to":   [ 3, 16, 16 ],
						"faces": {
							"east": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                     },
							"west": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "west" }
						}
					},
					{
						"from": [  0, 13,  0 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"up":   { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "up" },
							"down": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                   }
						}
					},
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 3, 16 ],
						"faces": {
							"up":   { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"                     },
							"down": { "uv": [ 0, 0, 16, 16 ], "texture": "#outer", "cullface": "down" }
						}
					}
				]
			}"""
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.DIAMOND).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("fof", "o o", "fof")
			.where('f', DecoItems.IRON_FRAME)
			.where('o', BigTechItemTags.OBSIDIAN)
			.result(this.getId())
			.toString()
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.OBSIDIAN).add(Items.OBSIDIAN);
	}
}