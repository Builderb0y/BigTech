package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.api.LightningPulseInteractor;

public class BigTechBlockTags {

	public static final TagKey<Block>
		BELTS                    = of("belts"),
		BELT_SUPPORT             = of("belt_support"),
		ASCENDERS                = of("ascenders"),
		WOODEN_FRAMES            = of("wooden_frames"),
		COPPER_FRAMES            = of("copper_frames"),
		METAL_FRAMES             = of("metal_frames"),
		FRAMES                   = of("frames"),
		STICKS_TO_IRON_FRAME     = of("sticks_to_iron_frame"),
		STICKS_TO_COPPER_FRAME   = of("sticks_to_copper_frame"),
		STICKS_TO_GOLD_FRAME     = of("sticks_to_gold_frame"),
		STICKS_TO_OAK_FRAME      = of("sticks_to_oak_frame"),
		STICKS_TO_SPRUCE_FRAME   = of("sticks_to_spruce_frame"),
		STICKS_TO_BIRCH_FRAME    = of("sticks_to_birch_frame"),
		STICKS_TO_JUNGLE_FRAME   = of("sticks_to_jungle_frame"),
		STICKS_TO_ACACIA_FRAME   = of("sticks_to_acacia_frame"),
		STICKS_TO_DARK_OAK_FRAME = of("sticks_to_dark_oak_frame"),
		STICKS_TO_CHERRY_FRAME   = of("sticks_to_cherry_frame"),
		STICKS_TO_MANGROVE_FRAME = of("sticks_to_mangrove_frame"),
		STICKS_TO_CRIMSON_FRAME  = of("sticks_to_crimson_frame"),
		STICKS_TO_WARPED_FRAME   = of("sticks_to_warped_frame"),
		COPPER_LADDERS           = of("copper_ladders"),
		METAL_LADDERS            = of("metal_ladders"),
		LADDERS                  = common("ladders"),
		COPPER_SLABS             = of("copper_slabs"),
		/**
		contains vanilla blocks which can conduct a lightning pulse.
		notably, this includes iron blocks, copper blocks, and gold blocks.
		for mod devs: you do NOT need to tag your custom blocks with this tag
		if you have registered them with {@link LightningPulseInteractor#LOOKUP}.
		in fact, doing so with a fallback provider will probably break things,
		because my own fallback provider will get called first, and it checks for this tag.
		this tag is intended for normal metal blocks with no special behavior ONLY.
		*/
		CONDUCTS_LIGHTNING       = of("conducts_lightning"),
		/**
		an extension of {@link #CONDUCTS_LIGHTNING} which indicates that the tagged
		blocks should also shock nearby entities when receiving a lightning pulse,
		in addition to conducting that pulse. like with {@link #CONDUCTS_LIGHTNING},
		modded blocks which have special behavior registered to
		{@link LightningPulseInteractor#LOOKUP} do NOT need to be tagged with this tag.
		this tag is intended for normal metal blocks with no special behavior ONLY.
		*/
		SHOCKS_ENTITIES          = of("shocks_entities"),
		/**
		contains big tech's lightning cables, for usage with data packs
		(for example, recipes), but is not used by any game logic.
		modded blocks do not need to be tagged with this tag to interact with lightning.
		*/
		LIGHTNING_CABLES         = of("lightning_cables"),
		LIGHTNING_JARS           = of("lightning_jars"),
		WOODEN_CATWALK_PLATFORMS = of("wooden_catwalk_platforms"),
		COPPER_CATWALK_PLATFORMS = of("copper_catwalk_platforms"),
		METAL_CATWALK_PLATFORMS  = of("metal_catwalk_platforms"),
		CATWALK_PLATFORMS        = of("catwalk_platforms"),
		WOODEN_CATWALK_STAIRS    = of("wooden_catwalk_stairs"),
		COPPER_CATWALK_STAIRS    = of("copper_catwalk_stairs"),
		METAL_CATWALK_STAIRS     = of("metal_catwalk_stairs"),
		CATWALK_STAIRS           = of("catwalk_stairs"),
		COPPER_BARS              = of("copper_bars"),
		CRYSTAL_CLUSTERS         = of("crystal_clusters"),
		PHASE_MANIPULATORS       = of("phase_manipulators"),
		MINER_BREAKABLE          = of("miner_breakable"),
		IRON_BLOCKS              = common("iron_blocks"),
		MAGNETITE_BLOCKS         = common("magnetite_blocks"),
		UNWAXED_COPPER_BLOCKS    = common("unwaxed_copper_blocks"),
		WAXED_COPPER_BLOCKS      = common("waxed_copper_blocks"),
		UNWAXED_CUT_COPPER_BLOCKS = common("unwaxed_cut_copper_blocks"),
		WAXED_CUT_COPPER_BLOCKS   = common("waxed_cut_copper_blocks"),
		UNWAXED_CUT_COPPER_STAIRS = common("unwaxed_cut_copper_stairs"),
		WAXED_CUT_COPPER_STAIRS   = common("waxed_cut_copper_stairs"),
		UNWAXED_CUT_COPPER_SLABS  = common("unwaxed_cut_copper_slabs"),
		WAXED_CUT_COPPER_SLABS    = common("waxed_cut_copper_slabs"),
		UNWAXED_COPPER_BARS       = of("unwaxed_copper_bars"),
		WAXED_COPPER_BARS         = of("waxed_copper_bars"),
		UNWAXED_COPPER_SLABS      = of("unwaxed_copper_slabs"),
		WAXED_COPPER_SLABS        = of("waxed_copper_slabs");

	public static TagKey<Block> common(String name) {
		return TagKey.of(RegistryKeys.BLOCK, new Identifier("c", name));
	}

	public static TagKey<Block> of(String name) {
		return TagKey.of(RegistryKeys.BLOCK, BigTechMod.modID(name));
	}
}