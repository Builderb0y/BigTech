package builderb0y.bigtech.items;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

public class BigTechItemTags {

	public static final TagKey<Item>
		BELTS                    = of("belts"),
		ASCENDERS                = of("ascenders"),
		WOODEN_FRAMES            = of("wooden_frames"),
		COPPER_FRAMES            = of("copper_frames"),
		METAL_FRAMES             = of("metal_frames"),
		FRAMES                   = of("frames"),
		COPPER_LADDERS           = of("copper_ladders"),
		METAL_LADDERS            = of("metal_ladders"),
		LADDERS                  = common("ladders"),
		LIGHTNING_CABLES         = of("lightning_cables"),
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
		PRESSURE_PLATES          = common("pressure_plates"),
		IRON_NUGGETS             = common("iron_nuggets"),
		COPPER_NUGGETS           = common("copper_nuggets"),
		GOLD_NUGGETS             = common("gold_nuggets"),
		STICKS                   = common("wood_sticks"),
		REDSTONE_BLOCKS          = common("redstone_blocks"),
		OBSIDIAN                 = common("obsidian"),
		IRON_BLOCKS              = common("iron_blocks"),
		MAGNETITE_NUGGETS        = common("magnetite_nuggets"),
		MAGNETITE_INGOTS         = common("magnetite_ingots"),
		MAGNETITE_BLOCKS         = common("magnetite_blocks");

	public static TagKey<Item> of(String name) {
		return TagKey.of(RegistryKeys.ITEM, BigTechMod.modID(name));
	}

	public static TagKey<Item> common(String name) {
		return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
	}
}