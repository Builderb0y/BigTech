package builderb0y.bigtech.beams.base;

import java.util.UUID;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;

public class BeamType {

	public static final RegistryKey<Registry<BeamType>> REGISTRY_KEY = RegistryKey.ofRegistry(BigTechMod.modID("beam_type"));
	public static final Registry<BeamType> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public final Factory factory;

	public BeamType(Factory factory) {
		this.factory = factory;
	}

	@Override
	public String toString() {
		return "BeamType: { ${java.util.Objects.requireNonNullElse(REGISTRY.getId(this), \"unregistered\")} }";
	}

	@FunctionalInterface
	public static interface Factory {

		public abstract Beam create(World world, UUID uuid);
	}
}