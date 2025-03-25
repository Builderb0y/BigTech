package builderb0y.bigtech.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.OrePlacedFeatures;

import builderb0y.bigtech.BigTechMod;

public class BigTechWorldgen {

	public static void init() {
		BiomeModifications.addFeature(
			BiomeSelectors.tag(BigTechBiomeTags.CRYSTAL_CLUSTER_SPAWNABLE),
			GenerationStep.Feature.UNDERGROUND_DECORATION,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, BigTechMod.modID("crystal_cluster"))
		);
		BiomeModifications.addFeature(
			(BiomeSelectionContext biome) -> biome.hasPlacedFeature(OrePlacedFeatures.ORE_GOLD),
			GenerationStep.Feature.UNDERGROUND_ORES,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, BigTechMod.modID("ore_silver"))
		);
		BiomeModifications.addFeature(
			(BiomeSelectionContext biome) -> biome.hasPlacedFeature(OrePlacedFeatures.ORE_GOLD_LOWER),
			GenerationStep.Feature.UNDERGROUND_ORES,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, BigTechMod.modID("ore_silver_lower"))
		);
	}
}