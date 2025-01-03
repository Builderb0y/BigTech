package builderb0y.bigtech.items;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

import net.minecraft.block.Block;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.datagen.impl.TripwireDataGenerator;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.*;
import builderb0y.bigtech.datagen.impl.ascenders.AscenderDataGenerator;
import builderb0y.bigtech.datagen.impl.ascenders.DescenderDataGenerator;
import builderb0y.bigtech.datagen.impl.belts.*;
import builderb0y.bigtech.datagen.impl.destroyers.LongRangeDeployerDataGenerator;
import builderb0y.bigtech.datagen.impl.destroyers.LongRangeDestroyerDataGenerator;
import builderb0y.bigtech.datagen.impl.destroyers.ShortRangeDeployerDataGenerator;
import builderb0y.bigtech.datagen.impl.destroyers.ShortRangeDestroyerDataGenerator;
import builderb0y.bigtech.datagen.impl.lightningJars.LargeLightningJarDataGenerator;
import builderb0y.bigtech.datagen.impl.lightningJars.SmallLightningJarDataGenerator;
import builderb0y.bigtech.datagen.impl.technoCrafters.PortableTechnoCrafterDataGenerator;
import builderb0y.bigtech.datagen.impl.technoCrafters.TechnoCrafterDataGenerator;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;

import static builderb0y.bigtech.items.BigTechItems.*;

@SuppressWarnings("unused")
public class FunctionalItems {

	//////////////////////////////// belts ////////////////////////////////

	@UseDataGen(NormalBeltDataGenerator.class)
	public static final BeltBlockItem BELT = registerBelt(
		FunctionalBlocks.BELT
	);
	@UseDataGen(SpeedyBeltDataGenerator.class)
	public static final BeltBlockItem SPEEDY_BELT = registerBelt(
		FunctionalBlocks.SPEEDY_BELT
	);
	@UseDataGen(BrakeBeltDataGenerator.class)
	public static final BeltBlockItem BRAKE_BELT = registerBelt(
		FunctionalBlocks.BRAKE_BELT
	);
	@UseDataGen(TrapdoorBeltDataGenerator.class)
	public static final BeltBlockItem TRAPDOOR_BELT = registerBelt(
		FunctionalBlocks.TRAPDOOR_BELT
	);
	@UseDataGen(DirectorBeltDataGenerator.class)
	public static final BlockItem DIRECTOR_BELT = registerPlacer(
		FunctionalBlocks.DIRECTOR_BELT
	);
	@UseDataGen(DetectorBeltDataGenerator.class)
	public static final BeltBlockItem DETECTOR_BELT = registerBelt(
		FunctionalBlocks.DETECTOR_BELT
	);
	@UseDataGen(SorterBeltDataGenerator.class)
	public static final BeltBlockItem SORTER_BELT = registerBelt(
		FunctionalBlocks.SORTER_BELT
	);
	@UseDataGen(EjectorBeltDataGenerator.class)
	public static final BeltBlockItem EJECTOR_BELT = registerBelt(
		FunctionalBlocks.EJECTOR_BELT
	);
	@UseDataGen(InjectorBeltDataGenerator.class)
	public static final BeltBlockItem INJECTOR_BELT = registerBelt(
		FunctionalBlocks.INJECTOR_BELT
	);
	@UseDataGen(LauncherBeltDataGenerator.class)
	public static final BlockItem LAUNCHER_BELT = registerBelt(
		FunctionalBlocks.LAUNCHER_BELT
	);

	//////////////////////////////// ascenders ////////////////////////////////

	@UseDataGen(AscenderDataGenerator.class)
	public static final AscenderBlockItem ASCENDER = register(
		new AscenderBlockItem(
			FunctionalBlocks.ASCENDER,
			settings("ascender", true)
		)
	);
	@UseDataGen(DescenderDataGenerator.class)
	public static final AscenderBlockItem DESCENDER = register(
		new AscenderBlockItem(
			FunctionalBlocks.DESCENDER,
			settings("descender", true)
		)
	);

	//////////////////////////////// automation ////////////////////////////////

	@UseDataGen(WoodenHopperDataGenerator.class)
	public static final BlockItem WOODEN_HOPPER = registerPlacer(
		FunctionalBlocks.WOODEN_HOPPER
	);

	@UseDataGen(IgnitorDataGenerator.class)
	public static final BlockItem IGNITOR = registerPlacer(
		FunctionalBlocks.IGNITOR
	);

	@UseDataGen(SilverIodideCannonDataGenerator.class)
	public static final BlockItem SILVER_IODIDE_CANNON = registerPlacer(
		FunctionalBlocks.SILVER_IODIDE_CANNON
	);

	@UseDataGen(SpawnerInterceptorDataGenerator.class)
	public static final BlockItem SPAWNER_INTERCEPTOR = registerPlacer(
		FunctionalBlocks.SPAWNER_INTERCEPTOR
	);

	@UseDataGen(PulsarDataGenerator.class)
	public static final BlockItem PULSAR = registerPlacer(
		FunctionalBlocks.PULSAR
	);

	@UseDataGen(RadioDataGenerator.class)
	public static final BlockItem RADIO = registerPlacer(
		FunctionalBlocks.RADIO
	);

	//////////////////////////////// encased blocks ////////////////////////////////

	@UseDataGen(EncasedRedstoneBlockDataGenerator.class)
	public static final BlockItem ENCASED_REDSTONE_BLOCK = registerPlacer(
		FunctionalBlocks.ENCASED_REDSTONE_BLOCK
	);
	@UseDataGen(EncasedSlimeBlockDataGenerator.class)
	public static final BlockItem ENCASED_SLIME_BLOCK = registerPlacer(
		FunctionalBlocks.ENCASED_SLIME_BLOCK
	);
	@UseDataGen(EncasedSlimeBlockDataGenerator.class)
	public static final BlockItem ENCASED_HONEY_BLOCK = registerPlacer(
		FunctionalBlocks.ENCASED_HONEY_BLOCK
	);

	//////////////////////////////// lightning stuff ////////////////////////////////

	@UseDataGen(LightningCableDataGenerator.class)
	public static final BlockItem IRON_LIGHTNING_CAbLE = registerPlacer(
		FunctionalBlocks.IRON_LIGHTNING_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.class)
	public static final BlockItem GOLD_LIGHTNING_CABLE = registerPlacer(
		FunctionalBlocks.GOLD_LIGHTNING_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.class)
	public static final BlockItem COPPER_LIGHTNING_CABLE = registerPlacer(
		FunctionalBlocks.COPPER_LIGHTNING_CABLE
	);
	@UseDataGen(LightningDiodeDataGenerator.class)
	public static final BlockItem LIGHTNING_DIODE = registerPlacer(
		FunctionalBlocks.LIGHTNING_DIODE
	);
	@UseDataGen(LightningElectrodeDataGenerator.class)
	public static final Item LIGHTNING_ELECTRODE = register(
		new Item(settings("lightning_electrode", false))
	);
	@UseDataGen(QuadLightningElectrodeDataGenerator.class)
	public static final Item QUAD_LIGHTNING_ELECTRODE = register(
		new Item(settings("quad_lightning_electrode", false))
	);
	@UseDataGen(LightningBatteryDataGenerator.class)
	public static final LightningBatteryItem LIGHTNING_BATTERY = register(
		new LightningBatteryItem(
			settings("lightning_battery", false)
			.component(BigTechDataComponents.LIGHTNING_CAPACITY, 1000)
			.component(BigTechDataComponents.LIGHTNING_ENERGY, 0)
		)
	);
	@UseDataGen(SmallLightningJarDataGenerator.class)
	public static final BlockItem SMALL_LIGHTNING_JAR = registerPlacer(
		FunctionalBlocks.SMALL_LIGHTNING_JAR
	);
	@UseDataGen(LargeLightningJarDataGenerator.class)
	public static final BlockItem LARGE_LIGHTNING_JAR = registerPlacer(
		FunctionalBlocks.LARGE_LIGHTNING_JAR
	);
	@UseDataGen(TransmuterDataGenerator.class)
	public static final BlockItem TRANSMUTER = registerPlacer(
		FunctionalBlocks.TRANSMUTER
	);
	@UseDataGen(CopperCoilDataGenerator.class)
	public static final BlockItem COPPER_COIL = registerPlacer(
		FunctionalBlocks.COPPER_COIL
	);
	@UseDataGen(ConductiveAnvilDataGenerator.class)
	public static final CopperItemCollection CONDUCTIVE_ANVILS = new CopperItemCollection(
		true,
		(CopperRegistrableCollection.Type type) -> new BlockItem(
			FunctionalBlocks.CONDUCTIVE_ANVILS.get(type),
			settings(type.noCopperPrefix + "conductive_anvil", true)
		)
	);
	@UseDataGen(ArcFurnaceElectrodeDataGenerator.class)
	public static final BlockItem ARC_FURNACE_ELECTRODE = registerPlacer(
		FunctionalBlocks.ARC_FURNACE_ELECTRODE
	);

	//////////////////////////////// lasers ////////////////////////////////

	@UseDataGen(LightningTransmitterDataGenerator.class)
	public static final BlockItem LIGHTNING_TRANSMITTER = registerPlacer(
		FunctionalBlocks.LIGHTNING_TRANSMITTER
	);
	@UseDataGen(RedstoneTransmitterDataGenerator.class)
	public static final BlockItem REDSTONE_TRANSMITTER = registerPlacer(
		FunctionalBlocks.REDSTONE_TRANSMITTER
	);
	@UseDataGen(RedstoneReceiverDataGenerator.class)
	public static final BlockItem REDSTONE_RECEIVER = registerPlacer(
		FunctionalBlocks.REDSTONE_RECEIVER
	);
	@UseDataGen(BeamInterceptorDataGenerator.class)
	public static final BlockItem BEAM_INTERCEPTOR = register(
		new ClientNbtCopyingBlockItem(
			FunctionalBlocks.BEAM_INTERCEPTOR,
			settings("beam_interceptor", true)
		)
	);
	@UseDataGen(TripwireDataGenerator.class)
	public static final BlockItem TRIPWIRE = registerPlacer(
		FunctionalBlocks.TRIPWIRE
	);
	@UseDataGen(SpotlightDataGenerator.class)
	public static final BlockItem SPOTLIGHT = registerPlacer(
		FunctionalBlocks.SPOTLIGHT
	);
	@UseDataGen(ShortRangeDeployerDataGenerator.class)
	public static final BlockItem SHORT_RANGE_DEPLOYER = registerPlacer(
		FunctionalBlocks.SHORT_RANGE_DEPLOYER
	);
	@UseDataGen(LongRangeDeployerDataGenerator.class)
	public static final BlockItem LONG_RANGE_DEPLOYER = registerPlacer(
		FunctionalBlocks.LONG_RANGE_DEPLOYER
	);
	@UseDataGen(ShortRangeDestroyerDataGenerator.class)
	public static final BlockItem SHORT_RANGE_DESTROYER = registerPlacer(
		FunctionalBlocks.SHORT_RANGE_DESTROYER
	);
	@UseDataGen(LongRangeDestroyerDataGenerator.class)
	public static final BlockItem LONG_RANGE_DESTROYER = registerPlacer(
		FunctionalBlocks.LONG_RANGE_DESTROYER
	);
	@UseDataGen(IgnitorBeamDataGenerator.class)
	public static final BlockItem IGNITOR_BEAM = registerPlacer(
		FunctionalBlocks.IGNITOR_BEAM
	);
	@UseDataGen(MirrorDataGenerator.class)
	public static final BlockItem MIRROR = registerPlacer(
		FunctionalBlocks.MIRROR
	);
	@UseDataGen(PrismDataGenerator.class)
	public static final BlockItem PRISM = register(
		new ClientNbtCopyingBlockItem(
			FunctionalBlocks.PRISM,
			settings("prism", true)
		)
	);
	@UseDataGen(LensDataGenerator.class)
	public static final Item LENS = register(
		new Item(settings("lens", false))
	);
	@UseDataGen(PhaseScramblerDataGenerator.class)
	public static final BlockItem PHASE_SCRAMBLER = registerPlacer(
		FunctionalBlocks.PHASE_SCRAMBLER
	);
	@UseDataGen(PhaseAlignerDataGenerator.class)
	public static final BlockItem PHASE_ALIGNER = registerPlacer(
		FunctionalBlocks.PHASE_ALIGNER
	);

	//////////////////////////////// crystals ////////////////////////////////

	@UseDataGen(CrystalClusterDataGenerator.class)
	public static final CrystalItemCollection CRYSTAL_CLUSTERS = new CrystalItemCollection(
		true,
		(CrystalColor color) -> new BlockItem(
			FunctionalBlocks.CRYSTAl_ClUSTERS.get(color),
			settings(color.prefix + "crystal_cluster", true)
		)
	);
	@UseDataGen(CrystalDebrisDataGenerator.class)
	public static final Item CRYSTAL_DEBRIS = register(
		new Item(settings("crystal_debris", false))
	);
	@UseDataGen(CrystallineSandDataGenerator.class)
	public static final BlockItem CRYSTALLINE_SAND = registerPlacer(
		FunctionalBlocks.CRYSTALLINE_SAND
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(MagnetiteNuggetDataGenerator.class)
	public static final Item MAGNETITE_NUGGET = register(
		new Item(settings("magnetite_nugget", false))
	);
	@UseDataGen(MagnetiteIngotDataGenerator.class)
	public static final Item MAGNETITE_INGOT = register(
		new Item(settings("magnetite_ingot", false))
	);
	@UseDataGen(MagnetiteBlockDataGenerator.class)
	public static final BlockItem MAGNETITE_BLOCK = registerPlacer(
		FunctionalBlocks.MAGNETITE_BLOCK
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Helmet.class)
	public static final ArmorItem MAGNETITE_HELMET = register(
		new ArmorItem(
			MagnetiteArmorMaterial.INSTANCE,
			EquipmentType.HELMET,
			settings("magnetite_helmet", false)
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Chestplate.class)
	public static final ArmorItem MAGNETITE_CHESTPLATE = register(
		new ArmorItem(
			MagnetiteArmorMaterial.INSTANCE,
			EquipmentType.CHESTPLATE,
			settings("magnetite_chestplate", false)
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Leggings.class)
	public static final ArmorItem MAGNETITE_LEGGINGS = register(
		new ArmorItem(
			MagnetiteArmorMaterial.INSTANCE,
			EquipmentType.LEGGINGS,
			settings("magnetite_leggings", false)
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Boots.class)
	public static final ArmorItem MAGNETITE_BOOTS = register(
		new ArmorItem(
			MagnetiteArmorMaterial.INSTANCE,
			EquipmentType.BOOTS,
			settings("magnetite_boots", false)
		)
	);
	@UseDataGen(CopperNuggetDataGenerator.class)
	public static final Item COPPER_NUGGET = register(
		new Item(settings("copper_nugget", false))
	);
	@UseDataGen(MediumWeightedPressurePlateDataGenerator.class)
	public static final BlockItem MEDIUM_WEIGHTED_PRESSURE_PLATE = registerPlacer(
		FunctionalBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE
	);
	@UseDataGen(MinerToolDataGenerator.class)
	public static final Item MINER_TOOL = register(
		new MinerToolItem(settings("miner_tool", false))
	);
	@UseDataGen(StoneCraftingTableDataGenerator.class)
	public static final BlockItem STONE_CRAFTING_TABLE = registerPlacer(
		FunctionalBlocks.STONE_CRAFTING_TABLE
	);
	@UseDataGen(TechnoCrafterDataGenerator.class)
	public static final BlockItem TECHNO_CRAFTER = registerPlacer(
		FunctionalBlocks.TECHNO_CRAFTER
	);
	@UseDataGen(PortableTechnoCrafterDataGenerator.class)
	public static final PortableTechnoCrafterItem PORTABLE_TECHNO_CRAFTER = register(
		new PortableTechnoCrafterItem(settings("portable_techno_crafter", false).maxCount(1))
	);
	@UseDataGen(MagneticArrowDataGenerator.class)
	public static final MagneticArrowItem MAGNETIC_ARROW = register(
		new MagneticArrowItem(settings("magnetic_arrow", false))
	);
	@UseDataGen(DislocatorDataGenerator.class)
	public static final DislocatorItem DISLOCATOR = register(
		new DislocatorItem(
			settings("dislocator", false).maxDamage(64).enchantable(15)
		)
	);

	public static void init() {
		FuelRegistryEvents.BUILD.register((FuelRegistry.Builder builder, FuelRegistryEvents.Context context) -> {
			int total = context.baseSmeltTime();
			builder.add(BigTechItemTags.WOODEN_FRAMES, total * 3 / 2);
			builder.add(BigTechItemTags.WOODEN_CATWALK_PLATFORMS, total * 3 / 2);
			builder.add(BigTechItemTags.WOODEN_CATWALK_STAIRS, total * 3 / 2);
			builder.add(CRYSTAL_DEBRIS, total * 6);
		});
	}

	public static BeltBlockItem registerBelt(Block block) {
		Identifier id = Registries.BLOCK.getId(block);
		return register(id, new BeltBlockItem(block, settings(id, true)));
	}
}