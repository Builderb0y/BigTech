package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import builderb0y.bigtech.BigTechMod;

public class BigTechBlockTags {

	public static final TagKey<Block>
		BELTS                    = of("belts"),
		BELT_SUPPORT             = of("belt_support"),
		ASCENDERS                = of("ascenders"),
		PREVENTS_ITEM_MERGING    = of("prevents_item_merging"),
		WOODEN_FRAMES            = of("wooden_frames"),
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
		STICKS_TO_WARPED_FRAME   = of("sticks_to_warped_frame");

	public static TagKey<Block> of(String name) {
		return TagKey.of(RegistryKeys.BLOCK, BigTechMod.modID(name));
	}
}