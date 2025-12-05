package builderb0y.bigtech.datagen.impl;

import java.util.Map;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.entities.BigTechEntityTags;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.MaterialItems;
import builderb0y.bigtech.worldgen.BigTechBiomeTags;

public class VanillaTweaksDataGenerator implements DataGenerator {

	//language=json
	public static final String transforms = """
	{
		"parent": "%PARENT",
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
	}""";

	@Override
	public void run(DataGenContext context) {
		context.getTags(MiningToolTags.AXE).add(BlockTags.LEAVES);
		context.getTags(BigTechItemTags.SHOCK_PROTECTIVE_ARMOR).addAll(
			Items.IRON_HELMET,
			Items.IRON_CHESTPLATE,
			Items.IRON_LEGGINGS,
			Items.IRON_BOOTS,

			Items.GOLDEN_HELMET,
			Items.GOLDEN_CHESTPLATE,
			Items.GOLDEN_LEGGINGS,
			Items.GOLDEN_BOOTS,

			Items.CHAINMAIL_HELMET,
			Items.CHAINMAIL_CHESTPLATE,
			Items.CHAINMAIL_LEGGINGS,
			Items.CHAINMAIL_BOOTS
		);
		context.getTags(BigTechEntityTags.INVALID_TRANSPORT_DESPAWN).addAll(EntityType.ITEM.getRegistryEntry(), EntityType.EXPERIENCE_ORB.getRegistryEntry());
		context.getTags(BigTechBlockTags.INVALID_TRANSPORT_DESPAWN).addAll(BigTechBlockTags.BELTS, BigTechBlockTags.ASCENDERS);
		context.writeToFile(
			context.blockModelPath(Identifier.ofVanilla("ladder")),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_ladder"))
			.blockTexture("ladder", Identifier.ofVanilla("ladder"))
			.toString()
		);
		for (String suffix : new String[] { "_center", "_north", "_east", "_south", "_west" }) {
			context.writeToFile(
				context.blockModelPath(
					context.suffixPath(BigTechMod.modID("vanilla_iron_bars"), suffix)
				),
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
			context.replace(transforms, Map.of("PARENT", "minecraft:block/hopper"))
		);
		context.writeToFile(
			context.itemModelPath(Identifier.ofVanilla("cauldron")),
			context.replace(transforms, Map.of("PARENT", "minecraft:block/cauldron"))
		);
		context.getTags(BigTechBiomeTags.CRYSTAL_CLUSTER_SPAWNABLE).add(BiomeTags.IS_OVERWORLD);
		context.getTags(BlockTags.REPLACEABLE_BY_TREES).add(BlockTags.SAPLINGS);
		context.lang.put("key.bigtech.cycle_state", "Cycle State");
		context.lang.put("key.bigtech.place_delay", "Change Place Delay");
		context.lang.put("key.bigtech.break_delay", "Change Break Delay");
		context.lang.put("overlay.bigtech.place_delay", "Place delay: %s tick(s)");
		context.lang.put("overlay.bigtech.break_delay", "Break delay: %s tick(s)");
		context.writeToFile(
			context.recipePath(Identifier.ofVanilla("anvil")),
			new ShapedRecipeBuilder()
			.pattern("bbb", " i ", "iii")
			.where('b', BigTechItemTags.STEEL_BLOCKS)
			.where('i', BigTechItemTags.STEEL_INGOTS)
			.result(Items.ANVIL)
			.toString()
		);
		context.writeToFile(
			context.recipePath(Identifier.ofVanilla("daylight_detector")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("ppp", "www")
			.where('p', MaterialItems.PHOTOVOLTAIC_RECEIVER)
			.where('w', ItemTags.PLANKS)
			.result(Items.DAYLIGHT_DETECTOR)
			.toString()
		);
		context.lang.put("stat_type.bigtech.proficiency", "Proficiency Gained");
	}
}