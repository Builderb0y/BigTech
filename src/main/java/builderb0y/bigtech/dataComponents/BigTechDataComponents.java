package builderb0y.bigtech.dataComponents;

import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import builderb0y.bigtech.BigTechMod;

public class BigTechDataComponents {

	public static final ComponentType<Integer> LIGHTNING_ENERGY = register(
		"lightning_energy",
		ComponentType.<Integer>builder().codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT).build()
	);
	public static final ComponentType<Integer> LIGHTNING_CAPACITY = register(
		"lightning_capacity",
		ComponentType.<Integer>builder().codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT).build()
	);

	public static void init() {}

	public static <T> ComponentType<T> register(String name, ComponentType<T> type) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, BigTechMod.modID(name), type);
	}
}