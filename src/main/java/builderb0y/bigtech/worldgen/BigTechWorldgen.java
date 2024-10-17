package builderb0y.bigtech.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.GenerationStep;

import builderb0y.bigtech.BigTechMod;

public class BigTechWorldgen {

	public static void init() {
		BiomeModifications.addFeature(
			BiomeSelectors.tag(BigTechBiomeTags.CRYSTAL_CLUSTER_SPAWNABLE),
			GenerationStep.Feature.UNDERGROUND_DECORATION,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, BigTechMod.modID("crystal_cluster"))
		);
	}
}