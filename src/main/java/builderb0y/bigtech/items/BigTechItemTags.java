package builderb0y.bigtech.items;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

public class BigTechItemTags {

	public static final TagKey<Item>
		BELTS           = of("belts"),
		ASCENDERS       = of("ascenders"),
		WOODEN_FRAMES   = of("wooden_frames"),
		METAL_FRAMES    = of("metal_frames"),
		FRAMES          = of("frames"),
		PRESSURE_PLATES = common("pressure_plates"),
		IRON_NUGGETS    = common("iron_nuggets"),
		COPPER_NUGGETS  = common("copper_nuggets"),
		GOLD_NUGGETS    = common("gold_nuggets"),
		STICKS          = common("wood_sticks"),
		REDSTONE_BLOCKS = common("redstone_blocks");

	public static TagKey<Item> of(String name) {
		return TagKey.of(RegistryKeys.ITEM, BigTechMod.modID(name));
	}

	public static TagKey<Item> common(String name) {
		return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
	}
}