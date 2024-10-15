package builderb0y.bigtech.beams.impl;

import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamType;

public class BeamTypes {

	public static final BeamType
		REDSTONE  = register("redstone",   RedstoneBeam::new),
		DEPLOYER  = register("deployer",   DeployerBeam::new),
		DESTROYER = register("destroyer", DestroyerBeam::new),
		TRIPWIRE  = register("tripwire",   TripwireBeam::new),
		SPOTLIGHT = register("spotlight", SpotlightBeam::new),
		IGNITOR   = register("ignitor",     IgnitorBeam::new),
		LIGHTNING = register("lightning", LightningBeam::new);

	public static void init() {}

	public static BeamType register(String name, BeamType.Factory factory) {
		return Registry.register(BeamType.REGISTRY, BigTechMod.modID(name), new BeamType(factory));
	}
}