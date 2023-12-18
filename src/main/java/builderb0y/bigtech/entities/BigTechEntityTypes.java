package builderb0y.bigtech.entities;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;

import builderb0y.bigtech.BigTechMod;

public class BigTechEntityTypes {

	public static final EntityType<MinerEntity> MINER = register(
		"miner",
		new EntityType<>(
			MinerEntity::new,
			SpawnGroup.MISC,
			true,
			true,
			false,
			true,
			ImmutableSet.of(),
			EntityDimensions.fixed(1.875F, 1.9375F),
			10,
			3,
			FeatureSet.empty()
		)
	);

	public static <E extends Entity> EntityType<E> register(String name, EntityType<E> entityType) {
		return Registry.register(Registries.ENTITY_TYPE, BigTechMod.modID(name), entityType);
	}

	public static void init() {}
}