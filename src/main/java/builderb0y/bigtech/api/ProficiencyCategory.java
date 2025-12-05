package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import builderb0y.bigtech.BigTechMod;

public record ProficiencyCategory(
	double multiplier
) {

	public static final RegistryKey<Registry<ProficiencyCategory>> REGISTRY_KEY = RegistryKey.ofRegistry(BigTechMod.modID("proficiency_category"));
	/**
	register categories to this registry in the {@link BigTechInitializer} only!
	registering via your own mod initializer is considered undefined behavior!
	*/
	public static final Registry<ProficiencyCategory> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
	public static final RegistryEntry<ProficiencyCategory>
		ALL_ITEMS   = register("all_items",           1.0D /  32.0D),

		TOOLS       = register("tools",               1.0D / 128.0D),

		SWORDS      = register("tools/swords",        1.0D /  64.0D),
		AXES        = register("tools/axes",          1.0D /  64.0D),
		HOES        = register("tools/hoes",          1.0D /  64.0D),
		PICKAXES    = register("tools/pickaxes",      1.0D /  64.0D),
		SHOVELS     = register("tools/shovels",       1.0D /  64.0D),

		ARMOR       = register("armor",               1.0D / 128.0D),

		HELMETS     = register("armor/helmets",       1.0D /  64.0D),
		CHESTPLATES = register("armor/chestplates",   1.0D /  64.0D),
		LEGGINGS    = register("armor/leggings",      1.0D /  64.0D),
		BOOTS       = register("armor/boots",         1.0D /  64.0D),

		WOOD        = register("materials/wood",      1.0D / 512.0D),
		LEATHER     = register("materials/leather",   1.0D / 512.0D),
		STONE       = register("materials/stone",     1.0D / 256.0D),
		//todo: copper in 1.21.11.
		//todo: bronze, when I add that.
		IRON        = register("materials/iron",      1.0D / 128.0D),
		CHAINMAIL   = register("materials/chainmail", 1.0D / 128.0D),
		GOLD        = register("materials/gold",      1.0D /  64.0D),
		//todo: steel, when I add that.
		DIAMOND     = register("materials/diamond",   1.0D /  32.0D),
		NETHERITE   = register("materials/netherite", 1.0D /  16.0D);

	@Internal
	public static RegistryEntry<ProficiencyCategory> register(String name, double multiplier) {
		return Registry.registerReference(REGISTRY, BigTechMod.modID(name), new ProficiencyCategory(multiplier));
	}

	@Internal
	public static void init() {}
}