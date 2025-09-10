package builderb0y.bigtech.entities;

import java.util.Optional;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Util;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.EntityLocalizationDataGenerator;

public class BigTechEntityTypes {

	@UseDataGen(EntityLocalizationDataGenerator.class)
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
			1.0F,
			10,
			3,
			Util.createTranslationKey("entity", BigTechMod.modID("miner")),
			Optional.empty(),
			FeatureSet.empty()
		)
	);
	@UseDataGen(EntityLocalizationDataGenerator.class)
	public static final EntityType<MagneticArrowEntity> MAGNETIC_ARROW = register(
		"magnetic_arrow",
		new EntityType<>(
			MagneticArrowEntity::new,
			SpawnGroup.MISC,
			true,
			true,
			false,
			true,
			ImmutableSet.of(),
			EntityDimensions.fixed(0.5F, 0.5F),
			1.0F,
			4,
			20,
			Util.createTranslationKey("entity", BigTechMod.modID("magnetic_arrow")),
			Optional.empty(),
			FeatureSet.empty()
		)
	);
	@UseDataGen(EntityLocalizationDataGenerator.class)
	public static final EntityType<StormCloudEntity> STORM_CLOUD = register(
		"storm_cloud",
		new EntityType<>(
			StormCloudEntity::new,
			SpawnGroup.MISC,
			true,
			true,
			true,
			true,
			ImmutableSet.of(),
			EntityDimensions.fixed(1.0F, 1.0F),
			1.0F,
			16,
			Integer.MAX_VALUE,
			Util.createTranslationKey("entity", BigTechMod.modID("storm_cloud")),
			Optional.empty(),
			FeatureSet.empty()
		)
	);
	@UseDataGen(EntityLocalizationDataGenerator.class)
	public static final EntityType<StormLightningEntity> STORM_LIGHTNING = register(
		"storm_lightning",
		new EntityType<>(
			StormLightningEntity::new,
			SpawnGroup.MISC,
			false,
			true,
			true,
			true,
			ImmutableSet.of(),
			EntityDimensions.fixed(0.0F, 0.0F),
			1.0F,
			16,
			Integer.MAX_VALUE,
			Util.createTranslationKey("entity", BigTechMod.modID("storm_lightning")),
			Optional.empty(),
			FeatureSet.empty()
		)
	);

	public static RegistryKey<EntityType<?>> key(String name) {
		return RegistryKey.of(RegistryKeys.ENTITY_TYPE, BigTechMod.modID(name));
	}

	public static <E extends Entity> EntityType<E> register(String name, EntityType<E> entityType) {
		return Registry.register(Registries.ENTITY_TYPE, BigTechMod.modID(name), entityType);
	}

	public static void init() {}
}