package builderb0y.bigtech.items;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

public class BigTechItemTags {

	public static final TagKey<Item>
		BELTS           = of    ("belts"),
		ASCENDERS       = of    ("ascenders"),
		PRESSURE_PLATES = common("pressure_plates");

	public static TagKey<Item> of(String name) {
		return TagKey.of(RegistryKeys.ITEM, BigTechMod.modID(name));
	}

	public static TagKey<Item> common(String name) {
		return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
	}
}