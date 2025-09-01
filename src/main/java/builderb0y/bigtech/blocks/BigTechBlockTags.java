package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.api.LightningPulseInteractor;

public class BigTechBlockTags {

	public static final TagKey<Block>
		ASCENDERS                 = of("ascenders"),
		BELT_SUPPORT              = of("belt_support"),
		BELTS                     = of("belts"),
		CATWALK_PLATFORMS         = of("catwalk_platforms"),
		CATWALK_STAIRS            = of("catwalk_stairs"),
		CONDUCTIVE_ANVILS         = of("conductive_anvils"),
		/**
		contains vanilla blocks which can conduct a lightning pulse.
		notably, this includes iron blocks, copper blocks, and gold blocks.
		for mod devs: you do NOT need to tag your custom blocks with this tag
		if you have registered them with {@link LightningPulseInteractor#LOOKUP}.
		in fact, doing so with a fallback provider will probably break things,
		because my own fallback provider will get called first, and it checks for this tag.
		this tag is intended for normal metal blocks with no special behavior ONLY.
		*/
		CONDUCTS_LIGHTNING        = of("conducts_lightning"),
		COPPER_BARS               = of("copper_bars"),
		COPPER_CATWALK_PLATFORMS  = of("copper_catwalk_platforms"),
		COPPER_CATWALK_STAIRS     = of("copper_catwalk_stairs"),
		COPPER_FRAMES             = of("copper_frames"),
		COPPER_LADDERS            = of("copper_ladders"),
		COPPER_SLABS              = of("copper_slabs"),
		CRYSTAL_CLUSTERS          = of("crystal_clusters"),
		CRYSTAL_LAMPS             = of("crystal_lamps"),
		ELECTROMAGNETIC_BLOCKS    = of("electromagnetic_blocks"),
		FERROMAGNETIC_BLOCKS      = of("ferromagnetic_blocks"),
		FRAMES                    = of("frames"),
		INVALID_TRANSPORT_DESPAWN = of("invalid_transport_despawn"),
		LEDS                      = of("leds"),
		/**
		contains big tech's lightning cables, for usage with data packs
		(for example, recipes), but is not used by any game logic.
		modded blocks do not need to be tagged with this tag to interact with lightning.
		*/
		LIGHTNING_CABLES          = of("lightning_cables"),
		LIGHTNING_JARS            = of("lightning_jars"),
		MAGNETIC_BLOCKS           = of("magnetic_blocks"),
		METAL_CATWALK_PLATFORMS   = of("metal_catwalk_platforms"),
		METAL_CATWALK_STAIRS      = of("metal_catwalk_stairs"),
		METAL_FRAMES              = of("metal_frames"),
		METAL_LADDERS             = of("metal_ladders"),
		MINER_BREAKABLE           = of("miner_breakable"),
		PHASE_MANIPULATORS        = of("phase_manipulators"),
		/**
		an extension of {@link #CONDUCTS_LIGHTNING} which indicates that the tagged
		blocks should also shock nearby entities when receiving a lightning pulse,
		in addition to conducting that pulse. like with {@link #CONDUCTS_LIGHTNING},
		modded blocks which have special behavior registered to
		{@link LightningPulseInteractor#LOOKUP} do NOT need to be tagged with this tag.
		this tag is intended for normal metal blocks with no special behavior ONLY.
		*/
		SHOCKS_ENTITIES           = of("shocks_entities"),
		STICKS_TO_ACACIA_FRAME    = of("sticks_to_acacia_frame"),
		STICKS_TO_BIRCH_FRAME     = of("sticks_to_birch_frame"),
		STICKS_TO_CHERRY_FRAME    = of("sticks_to_cherry_frame"),
		STICKS_TO_COPPER_FRAME    = of("sticks_to_copper_frame"),
		STICKS_TO_CRIMSON_FRAME   = of("sticks_to_crimson_frame"),
		STICKS_TO_DARK_OAK_FRAME  = of("sticks_to_dark_oak_frame"),
		STICKS_TO_ELECTRUM_FRAME  = of("sticks_to_electrum_frame"),
		STICKS_TO_GOLD_FRAME      = of("sticks_to_gold_frame"),
		STICKS_TO_IRON_FRAME      = of("sticks_to_iron_frame"),
		STICKS_TO_JUNGLE_FRAME    = of("sticks_to_jungle_frame"),
		STICKS_TO_MANGROVE_FRAME  = of("sticks_to_mangrove_frame"),
		STICKS_TO_OAK_FRAME       = of("sticks_to_oak_frame"),
		STICKS_TO_PALE_OAK_FRAME  = of("sticks_to_pale_oak_frame"),
		STICKS_TO_SILVER_FRAME    = of("sticks_to_silver_frame"),
		STICKS_TO_SPRUCE_FRAME    = of("sticks_to_spruce_frame"),
		STICKS_TO_STEEL_FRAME     = of("sticks_to_steel_frame"),
		STICKS_TO_WARPED_FRAME    = of("sticks_to_warped_frame"),
		UNWAXED_COPPER_BARS       = of("unwaxed_copper_bars"),
		UNWAXED_COPPER_SLABS      = of("unwaxed_copper_slabs"),
		WAXED_COPPER_BARS         = of("waxed_copper_bars"),
		WAXED_COPPER_SLABS        = of("waxed_copper_slabs"),
		WOODEN_CATWALK_PLATFORMS  = of("wooden_catwalk_platforms"),
		WOODEN_CATWALK_STAIRS     = of("wooden_catwalk_stairs"),
		WOODEN_FRAMES             = of("wooden_frames"),
		LADDERS                   = common("ladders"),
		SHEARS_MINEABLE           = common("mineable/shears"),
		SILVER_ORES               = common("ores/silver"),
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


	public static TagKey<Block> common(String name) {
		return TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", name));
	}

	public static TagKey<Block> of(String name) {
		return TagKey.of(RegistryKeys.BLOCK, BigTechMod.modID(name));
	}
}