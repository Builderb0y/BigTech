package builderb0y.bigtech.datagen.impl.functional;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

@Dependencies(ConductiveAnvilDataGenerator.CommonConductiveAnvilDataGenerator.class)
public class ConductiveAnvilDataGenerator extends BasicBlockDataGenerator {

	public final CopperRegistrableCollection.Type type;

	public ConductiveAnvilDataGenerator(BlockItem blockItem, CopperRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
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
	public void writeBlockModels(DataGenContext context) {
		String prefix = this.type.notWaxed().noCopperPrefix;
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_conductive_anvil"))
			.blockTexture("front",  BigTechMod.modID(prefix + "conductive_anvil_front"))
			.blockTexture("side",   BigTechMod.modID(prefix + "conductive_anvil_side"))
			.blockTexture("top",    BigTechMod.modID(prefix + "conductive_anvil_top"))
			.blockTexture("bottom", BigTechMod.modID(prefix + "conductive_anvil_bottom"))
			.toString()
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
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.CONDUCTIVE_ANVILS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.CONDUCTIVE_ANVILS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		//only one variant has a recipe, and that variant is
		//handled by CommonConductiveAnvilDataGenerator instead.
	}

	public static class CommonConductiveAnvilDataGenerator implements DataGenerator {

		@Override
		public void run(DataGenContext context) {
			this.writeBlockModels(context);
			this.writeTags(context);
			this.writeRecipe(context);
		}

		public void writeBlockModels(DataGenContext context) {
			context.writeToFile(
				context.blockModelPath(BigTechMod.modID("template_conductive_anvil")),
				//language=json
				"""
				{
					"parent": "block/block",
					"textures": {
						"particle": "#bottom",
						"front":  "bigtech:block/conductive_anvil_front",
						"side":   "bigtech:block/conductive_anvil_side",
						"top":    "bigtech:block/conductive_anvil_top",
						"bottom": "bigtech:block/conductive_anvil_bottom"
					},
					"elements": [
						{
							"from": [  2, 0,  2 ],
							"to":   [ 14, 4, 14 ],
							"faces": {
								"up":    { "uv": [ 2,  2, 14, 14 ], "texture": "#bottom" },
								"down":  { "uv": [ 2,  2, 14, 14 ], "texture": "#bottom", "cullface": "down" },
								"north": { "uv": [ 2, 12, 14, 16 ], "texture": "#side"   },
								"east":  { "uv": [ 2, 12, 14, 16 ], "texture": "#front"  },
								"south": { "uv": [ 2, 12, 14, 16 ], "texture": "#side"   },
								"west":  { "uv": [ 2, 12, 14, 16 ], "texture": "#front"  }
							}
						},
						{
							"from": [  4, 4,  3 ],
							"to":   [ 12, 5, 13 ],
							"faces": {
								"up":    { "uv": [  4,  3, 12, 13 ], "texture": "#bottom" },
								"north": { "uv": [  4,  3, 12,  4 ], "texture": "#bottom", "rotation": 180 },
								"east":  { "uv": [ 11,  3, 12, 13 ], "texture": "#bottom", "rotation":  90 },
								"south": { "uv": [  4, 12, 12, 13 ], "texture": "#bottom" },
								"west":  { "uv": [  4,  3,  5, 13 ], "texture": "#bottom", "rotation": 270 }
							}
						},
						{
							"from": [  6,  5,  4 ],
							"to":   [ 10, 10, 12 ],
							"faces": {
								"north": { "uv": [ 6, 6, 10, 11 ], "texture": "#side"  },
								"east":  { "uv": [ 4, 6, 12, 11 ], "texture": "#front" },
								"south": { "uv": [ 6, 6, 10, 11 ], "texture": "#side"  },
								"west":  { "uv": [ 4, 6, 12, 11 ], "texture": "#front" }
							}
						},
						{
							"from": [  3, 10,  0 ],
							"to":   [ 13, 16, 16 ],
							"faces": {
								"up":    { "uv": [ 3, 0, 13, 16 ], "texture": "#top", "cullface": "up" },
								"down":  { "uv": [ 3, 0, 13, 16 ], "texture": "#top"   },
								"north": { "uv": [ 3, 0, 13,  6 ], "texture": "#side"  },
								"east":  { "uv": [ 0, 0, 16,  6 ], "texture": "#front" },
								"south": { "uv": [ 3, 0, 13,  6 ], "texture": "#side"  },
								"west":  { "uv": [ 0, 0, 16,  6 ], "texture": "#front" }
							}
						}
					],
					"display": {
						"fixed": {
							"rotation": [ 0, 90, 0 ],
							"scale": [ 0.5, 0.5, 0.5 ]
						}
					}
				}"""
			);
		}

		public void writeTags(DataGenContext context) {
			context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.CONDUCTIVE_ANVILS);
			context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.CONDUCTIVE_ANVILS);
		}

		public void writeRecipe(DataGenContext context) {
			context.writeToFile(
				context.recipePath(BigTechMod.modID("conductive_anvil")),
				new ShapedRecipeBuilder()
				.pattern("bbb", " i ", "iii")
				.where('b', ConventionalItemTags.STORAGE_BLOCKS_COPPER)
				.where('i', ConventionalItemTags.COPPER_INGOTS)
				.result(FunctionalItems.CONDUCTIVE_ANVILS.copper)
				.toString()
			);
		}
	}
}