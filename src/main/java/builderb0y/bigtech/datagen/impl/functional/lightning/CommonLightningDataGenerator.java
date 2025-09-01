package builderb0y.bigtech.datagen.impl.functional.lightning;

import java.util.List;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;

public class CommonLightningDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechBlockTags.UNWAXED_COPPER_BLOCKS    ).addAll(    Blocks.COPPER_BLOCK,                  Blocks.EXPOSED_COPPER,                        Blocks.WEATHERED_COPPER,                        Blocks.OXIDIZED_COPPER                   );
		context.getTags(BigTechBlockTags.WAXED_COPPER_BLOCKS      ).addAll(    Blocks.WAXED_COPPER_BLOCK,            Blocks.WAXED_EXPOSED_COPPER,                  Blocks.WAXED_WEATHERED_COPPER,                  Blocks.WAXED_OXIDIZED_COPPER             );
		context.getTags(BigTechBlockTags.UNWAXED_CUT_COPPER_BLOCKS).addAll(    Blocks.CUT_COPPER,                    Blocks.EXPOSED_CUT_COPPER,                    Blocks.WEATHERED_CUT_COPPER,                    Blocks.OXIDIZED_CUT_COPPER               );
		context.getTags(BigTechBlockTags.WAXED_CUT_COPPER_BLOCKS  ).addAll(    Blocks.WAXED_CUT_COPPER,              Blocks.WAXED_EXPOSED_CUT_COPPER,              Blocks.WAXED_WEATHERED_CUT_COPPER,              Blocks.WAXED_OXIDIZED_CUT_COPPER         );
		context.getTags(BigTechBlockTags.UNWAXED_CUT_COPPER_STAIRS).addAll(    Blocks.CUT_COPPER_STAIRS,             Blocks.EXPOSED_CUT_COPPER_STAIRS,             Blocks.WEATHERED_CUT_COPPER_STAIRS,             Blocks.OXIDIZED_CUT_COPPER_STAIRS        );
		context.getTags(BigTechBlockTags.WAXED_CUT_COPPER_STAIRS  ).addAll(    Blocks.WAXED_CUT_COPPER_STAIRS,       Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS,       Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS,       Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS  );
		context.getTags(BigTechBlockTags.UNWAXED_CUT_COPPER_SLABS ).addAll(    Blocks.CUT_COPPER_SLAB,               Blocks.EXPOSED_CUT_COPPER_SLAB,               Blocks.WEATHERED_CUT_COPPER_SLAB,               Blocks.OXIDIZED_CUT_COPPER_SLAB          );
		context.getTags(BigTechBlockTags.WAXED_CUT_COPPER_SLABS   ).addAll(    Blocks.WAXED_CUT_COPPER_SLAB,         Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,         Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,         Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB    );
		context.getTags(BigTechBlockTags.UNWAXED_COPPER_BARS      ).addAll(DecoBlocks.COPPER_BARS .copper,       DecoBlocks.COPPER_BARS .exposed_copper,       DecoBlocks.COPPER_BARS .weathered_copper,       DecoBlocks.COPPER_BARS .oxidized_copper      );
		context.getTags(BigTechBlockTags.WAXED_COPPER_BARS        ).addAll(DecoBlocks.COPPER_BARS .waxed_copper, DecoBlocks.COPPER_BARS .waxed_exposed_copper, DecoBlocks.COPPER_BARS .waxed_weathered_copper, DecoBlocks.COPPER_BARS .waxed_oxidized_copper);
		context.getTags(BigTechBlockTags.UNWAXED_COPPER_SLABS     ).addAll(DecoBlocks.COPPER_SLABS.copper,       DecoBlocks.COPPER_SLABS.exposed_copper,       DecoBlocks.COPPER_SLABS.weathered_copper,       DecoBlocks.COPPER_SLABS.oxidized_copper      );
		context.getTags(BigTechBlockTags.WAXED_COPPER_SLABS       ).addAll(DecoBlocks.COPPER_SLABS.waxed_copper, DecoBlocks.COPPER_SLABS.waxed_exposed_copper, DecoBlocks.COPPER_SLABS.waxed_weathered_copper, DecoBlocks.COPPER_SLABS.waxed_oxidized_copper);

		context.getTags(BigTechItemTags .UNWAXED_COPPER_BLOCKS    ).addAll(    Items.COPPER_BLOCK,                    Items.EXPOSED_COPPER,                         Items.WEATHERED_COPPER,                         Items.OXIDIZED_COPPER                   );
		context.getTags(BigTechItemTags .WAXED_COPPER_BLOCKS      ).addAll(    Items.WAXED_COPPER_BLOCK,              Items.WAXED_EXPOSED_COPPER,                   Items.WAXED_WEATHERED_COPPER,                   Items.WAXED_OXIDIZED_COPPER             );
		context.getTags(BigTechItemTags .UNWAXED_CUT_COPPER_BLOCKS).addAll(    Items.CUT_COPPER,                      Items.EXPOSED_CUT_COPPER,                     Items.WEATHERED_CUT_COPPER,                     Items.OXIDIZED_CUT_COPPER               );
		context.getTags(BigTechItemTags .WAXED_CUT_COPPER_BLOCKS  ).addAll(    Items.WAXED_CUT_COPPER,                Items.WAXED_EXPOSED_CUT_COPPER,               Items.WAXED_WEATHERED_CUT_COPPER,               Items.WAXED_OXIDIZED_CUT_COPPER         );
		context.getTags(BigTechItemTags .UNWAXED_CUT_COPPER_STAIRS).addAll(    Items.CUT_COPPER_STAIRS,               Items.EXPOSED_CUT_COPPER_STAIRS,              Items.WEATHERED_CUT_COPPER_STAIRS,              Items.OXIDIZED_CUT_COPPER_STAIRS        );
		context.getTags(BigTechItemTags .WAXED_CUT_COPPER_STAIRS  ).addAll(    Items.WAXED_CUT_COPPER_STAIRS,         Items.WAXED_EXPOSED_CUT_COPPER_STAIRS,        Items.WAXED_WEATHERED_CUT_COPPER_STAIRS,        Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS  );
		context.getTags(BigTechItemTags .UNWAXED_CUT_COPPER_SLABS ).addAll(    Items.CUT_COPPER_SLAB,                 Items.EXPOSED_CUT_COPPER_SLAB,                Items.WEATHERED_CUT_COPPER_SLAB,                Items.OXIDIZED_CUT_COPPER_SLAB          );
		context.getTags(BigTechItemTags .WAXED_CUT_COPPER_SLABS   ).addAll(    Items.WAXED_CUT_COPPER_SLAB,           Items.WAXED_EXPOSED_CUT_COPPER_SLAB,          Items.WAXED_WEATHERED_CUT_COPPER_SLAB,          Items.WAXED_OXIDIZED_CUT_COPPER_SLAB    );
		context.getTags(BigTechItemTags .UNWAXED_COPPER_BARS      ).addAll(DecoItems.COPPER_BARS .copper,         DecoItems.COPPER_BARS .exposed_copper,        DecoItems.COPPER_BARS .weathered_copper,        DecoItems.COPPER_BARS .oxidized_copper      );
		context.getTags(BigTechItemTags .WAXED_COPPER_BARS        ).addAll(DecoItems.COPPER_BARS .waxed_copper,   DecoItems.COPPER_BARS .waxed_exposed_copper,  DecoItems.COPPER_BARS .waxed_weathered_copper,  DecoItems.COPPER_BARS .waxed_oxidized_copper);
		context.getTags(BigTechItemTags .UNWAXED_COPPER_SLABS     ).addAll(DecoItems.COPPER_SLABS.copper,         DecoItems.COPPER_SLABS.exposed_copper,        DecoItems.COPPER_SLABS.weathered_copper,        DecoItems.COPPER_SLABS.oxidized_copper      );
		context.getTags(BigTechItemTags .WAXED_COPPER_SLABS       ).addAll(DecoItems.COPPER_SLABS.waxed_copper,   DecoItems.COPPER_SLABS.waxed_exposed_copper,  DecoItems.COPPER_SLABS.waxed_weathered_copper,  DecoItems.COPPER_SLABS.waxed_oxidized_copper);

		context.getTags(BigTechBlockTags.CONDUCTS_LIGHTNING).addAll(List.of(
			new TagOrItem(ConventionalBlockTags.STORAGE_BLOCKS_IRON),
			new TagOrItem(BigTechBlockTags.STEEL_BLOCKS),
			new TagOrItem(Blocks.IRON_BARS),
			new TagOrItem(Blocks.IRON_DOOR),
			new TagOrItem(Blocks.IRON_TRAPDOOR),
			new TagOrItem(Blocks.ANVIL),
			new TagOrItem(Blocks.CHIPPED_ANVIL),
			new TagOrItem(Blocks.DAMAGED_ANVIL),
			new TagOrItem(Blocks.CHAIN),
			new TagOrItem(ConventionalBlockTags.STORAGE_BLOCKS_GOLD),
			new TagOrItem(BigTechBlockTags.WAXED_COPPER_BLOCKS),
			new TagOrItem(BigTechBlockTags.WAXED_CUT_COPPER_BLOCKS),
			new TagOrItem(BigTechBlockTags.WAXED_CUT_COPPER_STAIRS),
			new TagOrItem(BigTechBlockTags.WAXED_CUT_COPPER_SLABS),
			new TagOrItem(BigTechBlockTags.WAXED_COPPER_BARS),
			new TagOrItem(BigTechBlockTags.WAXED_COPPER_SLABS),
			new TagOrItem(BigTechBlockTags.METAL_FRAMES),
			new TagOrItem(BigTechBlockTags.MAGNETIC_BLOCKS)
		));
		context.getTags(BigTechBlockTags.SHOCKS_ENTITIES).addAll(List.of(
			new TagOrItem(ConventionalBlockTags.STORAGE_BLOCKS_IRON),
			new TagOrItem(BigTechBlockTags.STEEL_BLOCKS),
			new TagOrItem(Blocks.IRON_BARS),
			new TagOrItem(Blocks.IRON_DOOR),
			new TagOrItem(Blocks.IRON_TRAPDOOR),
			new TagOrItem(Blocks.ANVIL),
			new TagOrItem(Blocks.CHIPPED_ANVIL),
			new TagOrItem(Blocks.DAMAGED_ANVIL),
			new TagOrItem(Blocks.CHAIN),
			new TagOrItem(ConventionalBlockTags.STORAGE_BLOCKS_GOLD),
			new TagOrItem(BigTechBlockTags.WAXED_COPPER_BLOCKS),
			new TagOrItem(BigTechBlockTags.WAXED_CUT_COPPER_BLOCKS),
			new TagOrItem(BigTechBlockTags.WAXED_CUT_COPPER_STAIRS),
			new TagOrItem(BigTechBlockTags.WAXED_CUT_COPPER_SLABS),
			new TagOrItem(BigTechBlockTags.WAXED_COPPER_BARS),
			new TagOrItem(BigTechBlockTags.WAXED_COPPER_SLABS),
			new TagOrItem(BigTechBlockTags.METAL_FRAMES),
			new TagOrItem(BigTechBlockTags.MAGNETIC_BLOCKS)
		));
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_lightning_cable_center")),
			//language=json
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"particle": "#cable"
				},
				"elements": [
					{
						"from": [  4,  4,  4 ],
						"to":   [ 12, 12, 12 ],
						"faces": {
							"up": { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_lightning_cable_connection")),
			//language=json
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"particle": "#cable"
				},
				"elements": [
					{
						"from": [  4, 12,  4 ],
						"to":   [ 12, 16, 12 ],
						"faces": {
							"up":    { "uv": [ 8, 0, 16,  8 ], "texture": "#cable", "cullface": "up" },
							"north": { "uv": [ 8, 8, 16, 12 ], "texture": "#cable"                   },
							"east":  { "uv": [ 8, 8, 16, 12 ], "texture": "#cable"                   },
							"south": { "uv": [ 8, 8, 16, 12 ], "texture": "#cable"                   },
							"west":  { "uv": [ 8, 8, 16, 12 ], "texture": "#cable"                   }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.itemModelPath(BigTechMod.modID("template_lightning_cable")),
			//language=json
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"particle": "#cable"
				},
				"elements": [
					{
						"from": [  4,  4,  4 ],
						"to":   [ 12, 12, 12 ],
						"faces": {
							"up":    { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"down":  { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"north": { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"east":  { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"south": { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" },
							"west":  { "uv": [ 0, 0, 8, 8 ], "texture": "#cable" }
						}
					}
				]
			}"""
		);
	}
}