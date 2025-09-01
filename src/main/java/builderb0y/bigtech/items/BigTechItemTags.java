package builderb0y.bigtech.items;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

public class BigTechItemTags {

	public static final TagKey<Item>
		ASCENDERS                 = of("ascenders"),
		BELTS                     = of("belts"),
		CATWALK_PLATFORMS         = of("catwalk_platforms"),
		CATWALK_STAIRS            = of("catwalk_stairs"),
		COLORLESS_GLASS_MATERIALS = of("colorless_glass_materials"),
		CONDUCTIVE_ANVILS         = of("conductive_anvils"),
		COPPER_BARS               = of("copper_bars"),
		COPPER_CATWALK_PLATFORMS  = of("copper_catwalk_platforms"),
		COPPER_CATWALK_STAIRS     = of("copper_catwalk_stairs"),
		COPPER_DOUBLEABLES        = of("copper_doubleables"),
		COPPER_FRAMES             = of("copper_frames"),
		COPPER_LADDERS            = of("copper_ladders"),
		COPPER_SLABS              = of("copper_slabs"),
		CRYSTAL_CLUSTERS          = of("crystal_clusters"),
		CRYSTAL_LAMPS             = of("crystal_lamps"),
		ELECTROMAGNETIC_BLOCKS    = of("electromagnetic_blocks"),
		FERROMAGNETIC_BLOCKS      = of("ferromagnetic_blocks"),
		FRAMES                    = of("frames"),
		GOLD_DOUBLEABLES          = of("gold_doubleables"),
		IRON_DOUBLEABLES          = of("iron_doubleables"),
		LEDS                      = of("leds"),
		LIGHTNING_CABLES          = of("lightning_cables"),
		LIGHTNING_JARS            = of("lightning_jars"),
		MAGNETIC_ARMOR            = of("magnetic_armor"),
		MAGNETIC_BLOCKS           = of("magnetic_blocks"),
		METAL_CATWALK_PLATFORMS   = of("metal_catwalk_platforms"),
		METAL_CATWALK_STAIRS      = of("metal_catwalk_stairs"),
		METAL_FRAMES              = of("metal_frames"),
		METAL_LADDERS             = of("metal_ladders"),
		PHASE_MANIPULATORS        = of("phase_manipulators"),
		REPAIRS_MAGNETITE_ARMOR   = of("repairs_magnetite_armor"),
		SHOCK_PROTECTIVE_ARMOR    = of("shock_protective_armor"),
		SILVER_DOUBLEABLES        = of("silver_doubleables"),
		UNWAXED_COPPER_BARS       = of("unwaxed_copper_bars"),
		UNWAXED_COPPER_SLABS      = of("unwaxed_copper_slabs"),
		WAXED_COPPER_BARS         = of("waxed_copper_bars"),
		WAXED_COPPER_SLABS        = of("waxed_copper_slabs"),
		WOODEN_CATWALK_PLATFORMS  = of("wooden_catwalk_platforms"),
		WOODEN_CATWALK_STAIRS     = of("wooden_catwalk_stairs"),
		WOODEN_FRAMES             = of("wooden_frames"),
		ELECTRUM_INGOTS           = common("ingots/electrum"),
		GLOWSTONE_ALLOY_INGOTS    = common("ingots/glowstone_alloy"),
		LAPIS_ALLOY_INGOTS        = common("ingots/lapis_alloy"),
		MAGNETITE_INGOTS          = common("ingots/magnetite"),
		REDSTONE_ALLOY_INGOTS     = common("ingots/redstone_alloy"),
		SILVER_INGOTS             = common("ingots/silver"),
		STEEL_INGOTS              = common("ingots/steel"),
		LADDERS                   = common("ladders"),
		COPPER_NUGGETS            = common("nuggets/copper"),
		ELECTRUM_NUGGETS          = common("nuggets/electrum"),
		GLOWSTONE_ALLOY_NUGGETS   = common("nuggets/glowstone_alloy"),
		GOLD_NUGGETS              = common("nuggets/gold"),
		IRON_NUGGETS              = common("nuggets/iron"),
		LAPIS_ALLOY_NUGGETS       = common("nuggets/lapis_alloy"),
		MAGNETITE_NUGGETS         = common("nuggets/magnetite"),
		REDSTONE_ALLOY_NUGGETS    = common("nuggets/redstone_alloy"),
		SILVER_NUGGETS            = common("nuggets/silver"),
		STEEL_NUGGETS             = common("nuggets/steel"),
		OBSIDIAN                  = common("obsidian"),
		SILVER_ORES               = common("ores/silver"),
		PRESSURE_PLATES           = common("pressure_plates"),
		RAW_SILVER                = common("raw_materials/silver"),
		SILICON                   = common("silicon"),
		ELECTRUM_BLOCKS           = common("storage_blocks/electrum"),
		GLOWSTONE_ALLOY_BLOCKS    = common("storage_blocks/glowstone_alloy"),
		LAPIS_ALLOY_BLOCKS        = common("storage_blocks/lapis_alloy"),
		RAW_SILVER_BLOCKS         = common("storage_blocks/raw_silver"),
		REDSTONE_ALLOY_BLOCKS     = common("storage_blocks/redstone_alloy"),
		SILICON_BLOCKS            = common("storage_blocks/silicon"),
		SILVER_BLOCKS             = common("storage_blocks/silver"),
		STEEL_BLOCKS              = common("storage_blocks/steel"),
		UNWAXED_COPPER_BLOCKS     = common("unwaxed_copper_blocks"),
		UNWAXED_CUT_COPPER_BLOCKS = common("unwaxed_cut_copper_blocks"),
		UNWAXED_CUT_COPPER_SLABS  = common("unwaxed_cut_copper_slabs"),
		UNWAXED_CUT_COPPER_STAIRS = common("unwaxed_cut_copper_stairs"),
		WAXED_COPPER_BLOCKS       = common("waxed_copper_blocks"),
		WAXED_CUT_COPPER_BLOCKS   = common("waxed_cut_copper_blocks"),
		WAXED_CUT_COPPER_SLABS    = common("waxed_cut_copper_slabs"),
		WAXED_CUT_COPPER_STAIRS   = common("waxed_cut_copper_stairs");

	public static TagKey<Item> of(String name) {
		return TagKey.of(RegistryKeys.ITEM, BigTechMod.modID(name));
	}

	public static TagKey<Item> common(String name) {
		return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", name));
	}
}