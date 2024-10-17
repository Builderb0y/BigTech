package builderb0y.bigtech.worldgen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

import builderb0y.bigtech.BigTechMod;

public class BigTechFeatures {

	public static final CrystalClusterFeature CRYSTAL_CLUSTER = register("crystal_cluster", new CrystalClusterFeature());

	public static void init() {}

	public static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
		return Registry.register(Registries.FEATURE, BigTechMod.modID(name), feature);
	}
}