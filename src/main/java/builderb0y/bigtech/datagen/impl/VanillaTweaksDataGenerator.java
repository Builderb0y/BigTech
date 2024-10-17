package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.armorMaterials.ArmorMaterialTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.worldgen.BigTechBiomeTags;

public class VanillaTweaksDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(MiningToolTags.AXE).add(BlockTags.LEAVES);
		context.getTags(ArmorMaterialTags.SHOCK_PROTECTION).addAll(ArmorMaterials.IRON, ArmorMaterials.GOLD, ArmorMaterials.CHAIN);
		context.writeToFile(
			context.blockModelPath(Identifier.ofVanilla("ladder")),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_ladder"))
			.blockTexture("ladder", Identifier.ofVanilla("ladder"))
			.toString()
		);
		for (String suffix : new String[] { "_center", "_north", "_east", "_south", "_west" }) {				context.writeToFile(
			context.blockModelPath(
				context.suffixPath(BigTechMod.modID("vanilla_iron_bars"), suffix)),
				new RetexturedModelBuilder()
				.blockParent(BigTechMod.modID("template_bars${suffix}"))
				.blockTexture("bars", Identifier.ofVanilla("iron_bars"))
				.toString()
			);
		}
		context.writeToFile(
			context.blockstatePath(Identifier.ofVanilla("iron_bars")),
			//language=json
			"""
			{
				"multipart": [
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_center" } },
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_north"  }, "when": { "north": "true" } },
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_east"   }, "when": { "east":  "true" } },
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_south"  }, "when": { "south": "true" } },
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_west"   }, "when": { "west":  "true" } }
				]
			}"""
		);
		context.writeToFile(
			context.recipePath(Identifier.ofVanilla("hopper")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("s s", "scs", " s ")
			.where('s', Items.STONE)
			.where('c', Items.CHEST)
			.result(Items.HOPPER)
			.toString()
		);
		context.writeToFile(
			context.itemModelPath(Identifier.ofVanilla("hopper")),
			//language=json
			"""
			{
				"parent": "minecraft:block/hopper",
				"gui_light": "side",
				"display": {
					"gui": {
						"rotation":    [ 30,   225,     0     ],
						"translation": [  0,     0,     0     ],
						"scale":       [  0.625, 0.625, 0.625 ]
					},
					"ground": {
						"rotation":    [ 0,    0,    0    ],
						"translation": [ 0,    3,    0    ],
						"scale":       [ 0.25, 0.25, 0.25 ]
					},
					"fixed": {
						"rotation":    [ 0,   0,   0   ],
						"translation": [ 0,   0,   0   ],
						"scale":       [ 0.5, 0.5, 0.5 ]
					},
					"thirdperson_righthand": {
						"rotation":    [ 75,    45,     0     ],
						"translation": [  0,     2.5,   0     ],
						"scale":       [  0.375, 0.375, 0.375 ]
					},
					"firstperson_righthand": {
						"rotation":    [ 0,   45,    0    ],
						"translation": [ 0,    0,    0    ],
						"scale":       [ 0.40, 0.40, 0.40 ]
					},
					"firstperson_lefthand": {
						"rotation":    [ 0,  225,    0    ],
						"translation": [ 0,    0,    0    ],
						"scale":       [ 0.40, 0.40, 0.40 ]
					}
				}
			}"""
		);
		context.getTags(BigTechBiomeTags.CRYSTAL_CLUSTER_SPAWNABLE).add(BiomeTags.IS_OVERWORLD);
	}
}