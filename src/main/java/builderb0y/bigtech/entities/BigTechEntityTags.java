package builderb0y.bigtech.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import builderb0y.bigtech.BigTechMod;

public class BigTechEntityTags {

	public static final TagKey<EntityType<?>>
		INVALID_TRANSPORT_DESPAWN = of("invalid_transport_despawn");

	public static TagKey<EntityType<?>> of(String name) {
		return TagKey.of(RegistryKeys.ENTITY_TYPE, BigTechMod.modID(name));
	}
}