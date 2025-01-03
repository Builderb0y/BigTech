package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;

@Dependencies(ArcFurnaceRecipesDataGenerator.class)
public class ArcFurnaceElectrodeDataGenerator extends BasicBlockDataGenerator {

	public ArcFurnaceElectrodeDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixPath("block/", this.getId()).toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
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
			//language=json
			"""
			{
				"credit": "Made with Blockbench",
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"particle": "bigtech:block/arc_furnace_electrode_1",
					"1":        "bigtech:block/arc_furnace_electrode_1",
					"2":        "bigtech:block/arc_furnace_electrode_2"
				},
				"elements": [
					{
						"from": [ 0, 0,  4 ],
						"to":   [ 2, 4, 12 ],
						"faces": {
							"north": { "uv": [ 14, 12, 16, 16 ], "texture": "#1" },
							"east":  { "uv": [  8, 12, 16, 16 ], "texture": "#2" },
							"south": { "uv": [  0, 12,  2, 16 ], "texture": "#1" },
							"west":  { "uv": [  0, 12,  8, 16 ], "texture": "#2", "cullface": "west" },
							"down":  { "uv": [  8, 14, 16, 16 ], "texture": "#2", "cullface": "down", "rotation": 90 }
						}
					},
					{
						"from": [ 14, 0,  4 ],
						"to":   [ 16, 4, 12 ],
						"faces": {
							"north": { "uv": [  0, 12,  2, 16 ], "texture": "#1" },
							"east":  { "uv": [  0, 12,  8, 16 ], "texture": "#2", "cullface": "east" },
							"south": { "uv": [ 14, 12, 16, 16 ], "texture": "#1" },
							"west":  { "uv": [  8, 12, 16, 16 ], "texture": "#2" },
							"down":  { "uv": [  8, 14, 16, 16 ], "texture": "#2", "cullface": "down", "rotation": 90 }
						}
					},
					{
						"from": [  0,  4,  4 ],
						"to":   [ 16, 12, 12 ],
						"faces": {
							"north": { "uv": [ 0, 4, 16, 12 ], "texture": "#1" },
							"east":  { "uv": [ 0, 4,  8, 12 ], "texture": "#2", "cullface": "east" },
							"south": { "uv": [ 0, 4, 16, 12 ], "texture": "#1" },
							"west":  { "uv": [ 0, 4,  8, 12 ], "texture": "#2", "cullface": "west" },
							"up":    { "uv": [ 0, 0,  8, 16 ], "texture": "#2", "rotation": 90 },
							"down":  { "uv": [ 8, 0, 16, 16 ], "texture": "#2", "rotation": 90 }
						}
					},
					{
						"from": [  4, 12,  4 ],
						"to":   [ 12, 16, 12 ],
						"faces": {
							"north": { "uv": [ 4, 0, 12,  4 ], "texture": "#1" },
							"east":  { "uv": [ 0, 0,  8,  4 ], "texture": "#2" },
							"south": { "uv": [ 4, 0, 12,  4 ], "texture": "#1" },
							"west":  { "uv": [ 0, 0,  8,  4 ], "texture": "#2" },
							"up":    { "uv": [ 0, 4,  8, 12 ], "texture": "#2", "cullface": "up" }
						}
					},
					{
						"shade": false,
						"light_emission": 15,
						"from": [ 7, -8, 7 ],
						"to":   [ 9,  4, 9 ],
						"faces": {
							"north": { "uv": [  2, 16, 14, 14 ], "texture": "#1", "rotation": 90 },
							"east":  { "uv": [  2, 14, 14, 16 ], "texture": "#1", "rotation": 90 },
							"south": { "uv": [  2, 16, 14, 14 ], "texture": "#1", "rotation": 90 },
							"west":  { "uv": [  2, 14, 14, 16 ], "texture": "#1", "rotation": 90 },
							"down":  { "uv": [ 12, 12, 14, 14 ], "texture": "#1" }
						}
					}
				]
			}"""
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern(" c ", "cic", "iri")
			.where('c', BigTechItemTags.LIGHTNING_CABLES)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.where('r', Items.BLAZE_ROD)
			.result(this.getId())
			.toString()
		);
	}
}