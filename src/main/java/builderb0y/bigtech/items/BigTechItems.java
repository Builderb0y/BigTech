package builderb0y.bigtech.items;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;

public class BigTechItems {

	public static void init() {}

	public static <I extends Item> I register(String name, I item) {
		return Registry.register(Registries.ITEM, BigTechMod.modID(name), item);
	}
}