package builderb0y.bigtech.api;

import java.util.Arrays;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import builderb0y.bigtech.BigTechMod;

import static builderb0y.bigtech.api.ProficiencyCategory.*;

public record ProficiencyItem(
	RegistryEntryList<Item> items,
	Object2IntMap<RegistryEntry<ProficiencyCategory>> categories
) {

	public static final RegistryKey<Registry<ProficiencyItem>> REGISTRY_KEY = RegistryKey.ofRegistry(BigTechMod.modID("proficiency_item"));
	/**
	register items to this registry in the {@link BigTechInitializer}!
	registering via your own mod initializer is considered undefined behavior!
	*/
	public static final Registry<ProficiencyItem> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
	@Internal
	public static final Map<Item, Object2IntMap<ProficiencyCategory>> CACHE = new Reference2ObjectOpenHashMap<>();
	public static final RegistryEntry<ProficiencyItem>
		WOODEN_AXE           = new Builder(Items.WOODEN_AXE          ).add(AXES,        1).add(TOOLS, 1).add(WOOD,      3).register("wooden_axe"          ),
		WOODEN_HOE           = new Builder(Items.WOODEN_HOE          ).add(HOES,        1).add(TOOLS, 1).add(WOOD,      2).register("wooden_hoe"          ),
		WOODEN_PICKAXE       = new Builder(Items.WOODEN_PICKAXE      ).add(PICKAXES,    1).add(TOOLS, 1).add(WOOD,      3).register("wooden_pickaxe"      ),
		WOODEN_SHOVEL        = new Builder(Items.WOODEN_SHOVEL       ).add(SHOVELS,     1).add(TOOLS, 1).add(WOOD,      1).register("wooden_shovel"       ),
		WOODEN_SWORD         = new Builder(Items.WOODEN_SWORD        ).add(SWORDS,      1).add(TOOLS, 1).add(WOOD,      2).register("wooden_sword"        ),

		STONE_AXE            = new Builder(Items.STONE_AXE           ).add(AXES,        1).add(TOOLS, 1).add(STONE,     3).register("stone_axe"           ),
		STONE_HOE            = new Builder(Items.STONE_HOE           ).add(HOES,        1).add(TOOLS, 1).add(STONE,     2).register("stone_hoe"           ),
		STONE_PICKAXE        = new Builder(Items.STONE_PICKAXE       ).add(PICKAXES,    1).add(TOOLS, 1).add(STONE,     3).register("stone_pickaxe"       ),
		STONE_SHOVEL         = new Builder(Items.STONE_SHOVEL        ).add(SHOVELS,     1).add(TOOLS, 1).add(STONE,     1).register("stone_shovel"        ),
		STONE_SWORD          = new Builder(Items.STONE_SWORD         ).add(SWORDS,      1).add(TOOLS, 1).add(STONE,     2).register("stone_sword"         ),

		IRON_AXE             = new Builder(Items.IRON_AXE            ).add(AXES,        1).add(TOOLS, 1).add(IRON,      3).register("iron_axe"            ),
		IRON_HOE             = new Builder(Items.IRON_HOE            ).add(HOES,        1).add(TOOLS, 1).add(IRON,      2).register("iron_hoe"            ),
		IRON_PICKAXE         = new Builder(Items.IRON_PICKAXE        ).add(PICKAXES,    1).add(TOOLS, 1).add(IRON,      3).register("iron_pickaxe"        ),
		IRON_SHOVEL          = new Builder(Items.IRON_SHOVEL         ).add(SHOVELS,     1).add(TOOLS, 1).add(IRON,      1).register("iron_shovel"         ),
		IRON_SWORD           = new Builder(Items.IRON_SWORD          ).add(SWORDS,      1).add(TOOLS, 1).add(IRON,      2).register("iron_sword"          ),

		GOLDEN_AXE           = new Builder(Items.GOLDEN_AXE          ).add(AXES,        1).add(TOOLS, 1).add(GOLD,      3).register("golden_axe"          ),
		GOLDEN_HOE           = new Builder(Items.GOLDEN_HOE          ).add(HOES,        1).add(TOOLS, 1).add(GOLD,      2).register("golden_hoe"          ),
		GOLDEN_PICKAXE       = new Builder(Items.GOLDEN_PICKAXE      ).add(PICKAXES,    1).add(TOOLS, 1).add(GOLD,      3).register("golden_pickaxe"      ),
		GOLDEN_SHOVEL        = new Builder(Items.GOLDEN_SHOVEL       ).add(SHOVELS,     1).add(TOOLS, 1).add(GOLD,      1).register("golden_shovel"       ),
		GOLDEN_SWORD         = new Builder(Items.GOLDEN_SWORD        ).add(SWORDS,      1).add(TOOLS, 1).add(GOLD,      2).register("golden_sword"        ),

		DIAMOND_AXE          = new Builder(Items.DIAMOND_AXE         ).add(AXES,        1).add(TOOLS, 1).add(DIAMOND,   3).register("diamond_axe"         ),
		DIAMOND_HOE          = new Builder(Items.DIAMOND_HOE         ).add(HOES,        1).add(TOOLS, 1).add(DIAMOND,   2).register("diamond_hoe"         ),
		DIAMOND_PICKAXE      = new Builder(Items.DIAMOND_PICKAXE     ).add(PICKAXES,    1).add(TOOLS, 1).add(DIAMOND,   3).register("diamond_pickaxe"     ),
		DIAMOND_SHOVEL       = new Builder(Items.DIAMOND_SHOVEL      ).add(SHOVELS,     1).add(TOOLS, 1).add(DIAMOND,   1).register("diamond_shovel"      ),
		DIAMOND_SWORD        = new Builder(Items.DIAMOND_SWORD       ).add(SWORDS,      1).add(TOOLS, 1).add(DIAMOND,   2).register("diamond_sword"       ),

		NETHERITE_AXE        = new Builder(Items.DIAMOND_AXE         ).add(AXES,        1).add(TOOLS, 1).add(NETHERITE, 3).register("netherite_axe"       ),
		NETHERITE_HOE        = new Builder(Items.DIAMOND_HOE         ).add(HOES,        1).add(TOOLS, 1).add(NETHERITE, 2).register("netherite_hoe"       ),
		NETHERITE_PICKAXE    = new Builder(Items.DIAMOND_PICKAXE     ).add(PICKAXES,    1).add(TOOLS, 1).add(NETHERITE, 3).register("netherite_pickaxe"   ),
		NETHERITE_SHOVEL     = new Builder(Items.DIAMOND_SHOVEL      ).add(SHOVELS,     1).add(TOOLS, 1).add(NETHERITE, 1).register("netherite_shovel"    ),
		NETHERITE_SWORD      = new Builder(Items.DIAMOND_SWORD       ).add(SWORDS,      1).add(TOOLS, 1).add(NETHERITE, 2).register("netherite_sword"     ),

		LEATHER_HELMET       = new Builder(Items.LEATHER_HELMET      ).add(HELMETS,     1).add(ARMOR, 1).add(LEATHER,   5).register("leather_helmet"      ),
		LEATHER_CHESTPLATE   = new Builder(Items.LEATHER_CHESTPLATE  ).add(CHESTPLATES, 1).add(ARMOR, 1).add(LEATHER,   8).register("leather_chestplate"  ),
		LEATHER_LEGGINGS     = new Builder(Items.LEATHER_LEGGINGS    ).add(LEGGINGS,    1).add(ARMOR, 1).add(LEATHER,   7).register("leather_leggings"    ),
		LEATHER_BOOTS        = new Builder(Items.LEATHER_BOOTS       ).add(BOOTS,       1).add(ARMOR, 1).add(LEATHER,   4).register("leather_boots"       ),

		CHAINMAIL_HELMET     = new Builder(Items.CHAINMAIL_HELMET    ).add(HELMETS,     1).add(ARMOR, 1).add(CHAINMAIL, 5).register("chainmail_helmet"    ),
		CHAINMAIL_CHESTPLATE = new Builder(Items.CHAINMAIL_CHESTPLATE).add(CHESTPLATES, 1).add(ARMOR, 1).add(CHAINMAIL, 8).register("chainmail_chestplate"),
		CHAINMAIL_LEGGINGS   = new Builder(Items.CHAINMAIL_LEGGINGS  ).add(LEGGINGS,    1).add(ARMOR, 1).add(CHAINMAIL, 7).register("chainmail_leggings"  ),
		CHAINMAIL_BOOTS      = new Builder(Items.CHAINMAIL_BOOTS     ).add(BOOTS,       1).add(ARMOR, 1).add(CHAINMAIL, 4).register("chainmail_boots"     ),

		IRON_HELMET          = new Builder(Items.IRON_HELMET         ).add(HELMETS,     1).add(ARMOR, 1).add(IRON,      5).register("iron_helmet"         ),
		IRON_CHESTPLATE      = new Builder(Items.IRON_CHESTPLATE     ).add(CHESTPLATES, 1).add(ARMOR, 1).add(IRON,      8).register("iron_chestplate"     ),
		IRON_LEGGINGS        = new Builder(Items.IRON_LEGGINGS       ).add(LEGGINGS,    1).add(ARMOR, 1).add(IRON,      7).register("iron_leggings"       ),
		IRON_BOOTS           = new Builder(Items.IRON_BOOTS          ).add(BOOTS,       1).add(ARMOR, 1).add(IRON,      4).register("iron_boots"          ),

		GOLDEN_HELMET        = new Builder(Items.GOLDEN_HELMET       ).add(HELMETS,     1).add(ARMOR, 1).add(GOLD,      5).register("golden_helmet"       ),
		GOLDEN_CHESTPLATE    = new Builder(Items.GOLDEN_CHESTPLATE   ).add(CHESTPLATES, 1).add(ARMOR, 1).add(GOLD,      8).register("golden_chestplate"   ),
		GOLDEN_LEGGINGS      = new Builder(Items.GOLDEN_LEGGINGS     ).add(LEGGINGS,    1).add(ARMOR, 1).add(GOLD,      7).register("golden_leggings"     ),
		GOLDEN_BOOTS         = new Builder(Items.GOLDEN_BOOTS        ).add(BOOTS,       1).add(ARMOR, 1).add(GOLD,      4).register("golden_boots"        ),

		DIAMOND_HELMET       = new Builder(Items.DIAMOND_HELMET      ).add(HELMETS,     1).add(ARMOR, 1).add(DIAMOND,   5).register("diamond_helmet"      ),
		DIAMOND_CHESTPLATE   = new Builder(Items.DIAMOND_CHESTPLATE  ).add(CHESTPLATES, 1).add(ARMOR, 1).add(DIAMOND,   8).register("diamond_chestplate"  ),
		DIAMOND_LEGGINGS     = new Builder(Items.DIAMOND_LEGGINGS    ).add(LEGGINGS,    1).add(ARMOR, 1).add(DIAMOND,   7).register("diamond_leggings"    ),
		DIAMOND_BOOTS        = new Builder(Items.DIAMOND_BOOTS       ).add(BOOTS,       1).add(ARMOR, 1).add(DIAMOND,   4).register("diamond_boots"       ),

		NETHERITE_HELMET     = new Builder(Items.NETHERITE_HELMET    ).add(HELMETS,     1).add(ARMOR, 1).add(NETHERITE, 5).register("netherite_helmet"    ),
		NETHERITE_CHESTPLATE = new Builder(Items.NETHERITE_CHESTPLATE).add(CHESTPLATES, 1).add(ARMOR, 1).add(NETHERITE, 8).register("netherite_chestplate"),
		NETHERITE_LEGGINGS   = new Builder(Items.NETHERITE_LEGGINGS  ).add(LEGGINGS,    1).add(ARMOR, 1).add(NETHERITE, 7).register("netherite_leggings"  ),
		NETHERITE_BOOTS      = new Builder(Items.NETHERITE_BOOTS     ).add(BOOTS,       1).add(ARMOR, 1).add(NETHERITE, 4).register("netherite_boots"     );

	@Internal
	public static void init() {
		for (ProficiencyItem proficiencyItem : REGISTRY) {
			proficiencyItem.items.stream().map(RegistryEntry<Item>::value).forEach((Item item) -> {
				Object2IntMap<ProficiencyCategory> map = CACHE.computeIfAbsent(item, $ -> new Object2IntOpenHashMap<>());
				for (ObjectIterator<Object2IntMap.Entry<RegistryEntry<ProficiencyCategory>>> iterator = Object2IntMaps.fastIterator(proficiencyItem.categories); iterator.hasNext();) {
					Object2IntMap.Entry<RegistryEntry<ProficiencyCategory>> entry = iterator.next();
					map.mergeInt(entry.getKey().value(), entry.getIntValue(), Integer::sum);
				}
			});
		}
	}

	@Internal
	public static class Builder extends Object2IntOpenHashMap<RegistryEntry<ProficiencyCategory>> {

		public RegistryEntryList<Item> items;

		public Builder(RegistryEntryList<Item> items) {
			this.items = items;
		}

		@SuppressWarnings({ "deprecation", "unchecked" })
		public Builder(Item... items) {
			this.items = RegistryEntryList.of(Arrays.stream(items).map(Item::getRegistryEntry).toArray(RegistryEntry[]::new));
		}

		public Builder add(RegistryEntry<ProficiencyCategory> category, int increment) {
			this.addTo(category, increment);
			return this;
		}

		public RegistryEntry<ProficiencyItem> register(String id) {
			return Registry.registerReference(
				REGISTRY,
				BigTechMod.modID(id),
				new ProficiencyItem(this.items, new Object2IntOpenHashMap<>(this))
			);
		}
	}
}