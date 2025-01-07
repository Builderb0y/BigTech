package builderb0y.bigtech.items;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

public class BigTechItemTags {

	public static final TagKey<Item>
		BELTS                     = of("belts"),
		ASCENDERS                 = of("ascenders"),
		WOODEN_FRAMES             = of("wooden_frames"),
		COPPER_FRAMES             = of("copper_frames"),
		METAL_FRAMES              = of("metal_frames"),
		FRAMES                    = of("frames"),
		COPPER_LADDERS            = of("copper_ladders"),
		METAL_LADDERS             = of("metal_ladders"),
		LADDERS                   = common("ladders"),
		COPPER_SLABS              = of("copper_slabs"),
		LIGHTNING_CABLES          = of("lightning_cables"),
		LIGHTNING_JARS            = of("lightning_jars"),
		WOODEN_CATWALK_PLATFORMS  = of("wooden_catwalk_platforms"),
		COPPER_CATWALK_PLATFORMS  = of("copper_catwalk_platforms"),
		METAL_CATWALK_PLATFORMS   = of("metal_catwalk_platforms"),
		CATWALK_PLATFORMS         = of("catwalk_platforms"),
		WOODEN_CATWALK_STAIRS     = of("wooden_catwalk_stairs"),
		COPPER_CATWALK_STAIRS     = of("copper_catwalk_stairs"),
		METAL_CATWALK_STAIRS      = of("metal_catwalk_stairs"),
		CATWALK_STAIRS            = of("catwalk_stairs"),
		COPPER_BARS               = of("copper_bars"),
		CRYSTAL_CLUSTERS          = of("crystal_clusters"),
		PHASE_MANIPULATORS        = of("phase_manipulators"),
		FERROMAGNETIC_BLOCKS      = of("ferromagnetic_blocks"),
		ELECTROMAGNETIC_BLOCKS    = of("electromagnetic_blocks"),
		MAGNETIC_BLOCKS           = of("magnetic_blocks"),
		PRESSURE_PLATES           = common("pressure_plates"),
		IRON_NUGGETS              = common("nuggets/iron"),
		COPPER_NUGGETS            = common("nuggets/copper"),
		GOLD_NUGGETS              = common("nuggets/gold"),
		OBSIDIAN                  = common("obsidian"),
		MAGNETITE_NUGGETS         = common("nuggets/magnetite"),
		MAGNETITE_INGOTS          = common("ingots/magnetite"),
		UNWAXED_COPPER_BLOCKS     = common("unwaxed_copper_blocks"),
		WAXED_COPPER_BLOCKS       = common("waxed_copper_blocks"),
		UNWAXED_CUT_COPPER_BLOCKS = common("unwaxed_cut_copper_blocks"),
		WAXED_CUT_COPPER_BLOCKS   = common("waxed_cut_copper_blocks"),
		UNWAXED_CUT_COPPER_STAIRS = common("unwaxed_cut_copper_stairs"),
		WAXED_CUT_COPPER_STAIRS   = common("waxed_cut_copper_stairs"),
		UNWAXED_CUT_COPPER_SLABS  = common("unwaxed_cut_copper_slabs"),
		WAXED_CUT_COPPER_SLABS    = common("waxed_cut_copper_slabs"),
		UNWAXED_COPPER_BARS       = of("unwaxed_copper_bars"),
		WAXED_COPPER_BARS         = of("waxed_copper_bars"),
		UNWAXED_COPPER_SLABS      = of("unwaxed_copper_slabs"),
		WAXED_COPPER_SLABS        = of("waxed_copper_slabs"),
		LEDS                      = of("leds"),
		CONDUCTIVE_ANVILS         = of("conductive_anvils"),
		CRYSTAL_LAMPS             = of("crystal_lamps"),
		REPAIRS_MAGNETITE_ARMOR   = of("repairs_magnetite_armor"),
		MAGNETIC_ARMOR            = of("magnetic_armor"),
		SHOCK_PROTECTIVE_ARMOR    = of("shock_protective_armor"),
		COLORLESS_GLASS_MATERIALS = of("colorless_glass_materials"),
		IRON_DOUBLEABLES          = of("iron_doubleables"),
		COPPER_DOUBLEABLES        = of("copper_doubleables"),
		GOLD_DOUBLEABLES          = of("gold_doubleables");

	public static TagKey<Item> of(String name) {
		return TagKey.of(RegistryKeys.ITEM, BigTechMod.modID(name));
	}

	public static TagKey<Item> common(String name) {
		return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", name));
	}
}