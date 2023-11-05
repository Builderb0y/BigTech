package builderb0y.bigtech.beams.impl;

import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamType;

public class BeamTypes {

	public static final BeamType
		REDSTONE = register("redstone", RedstoneBeam::new);

	public static void init() {}

	public static BeamType register(String name, BeamType.Factory factory) {
		return Registry.register(BeamType.REGISTRY, BigTechMod.modID(name), new BeamType(factory));
	}
}