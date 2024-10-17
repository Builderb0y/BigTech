package builderb0y.bigtech.worldgen;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

import builderb0y.bigtech.BigTechMod;

public class BigTechBiomeTags {

	public static final TagKey<Biome>
		CRYSTAL_CLUSTER_SPAWNABLE = of("crystal_cluster_spawnable");

	public static TagKey<Biome> of(String name) {
		return TagKey.of(RegistryKeys.BIOME, BigTechMod.modID(name));
	}
}