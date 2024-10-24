package builderb0y.bigtech.items;

import net.fabricmc.fabric.api.registry.FuelRegistry;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
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

public class FunctionalItems {

	//////////////////////////////// belts ////////////////////////////////

	@UseDataGen(NormalBeltDataGenerator.class)
	public static final BeltBlockItem BELT = BigTechItems.registerBelt(
		FunctionalBlocks.BELT
	);
	@UseDataGen(SpeedyBeltDataGenerator.class)
	public static final BeltBlockItem SPEEDY_BELT = BigTechItems.registerBelt(
		FunctionalBlocks.SPEEDY_BELT
	);
	@UseDataGen(BrakeBeltDataGenerator.class)
	public static final BeltBlockItem BRAKE_BELT = BigTechItems.registerBelt(
		FunctionalBlocks.BRAKE_BELT
	);
	@UseDataGen(TrapdoorBeltDataGenerator.class)
	public static final BeltBlockItem TRAPDOOR_BELT = BigTechItems.registerBelt(
		FunctionalBlocks.TRAPDOOR_BELT
	);
	@UseDataGen(DirectorBeltDataGenerator.class)
	public static final BlockItem DIRECTOR_BELT = BigTechItems.registerPlacer(
		FunctionalBlocks.DIRECTOR_BELT
	);
	@UseDataGen(DetectorBeltDataGenerator.class)
	public static final BeltBlockItem DETECTOR_BELT = BigTechItems.registerBelt(
		FunctionalBlocks.DETECTOR_BELT
	);
	@UseDataGen(SorterBeltDataGenerator.class)
	public static final BeltBlockItem SORTER_BELT = BigTechItems.registerBelt(
		FunctionalBlocks.SORTER_BELT
	);
	@UseDataGen(EjectorBeltDataGenerator.class)
	public static final BeltBlockItem EJECTOR_BELT = BigTechItems.registerBelt(
		FunctionalBlocks.EJECTOR_BELT
	);
	@UseDataGen(InjectorBeltDataGenerator.class)
	public static final BeltBlockItem INJECTOR_BELT = BigTechItems.registerBelt(
		FunctionalBlocks.INJECTOR_BELT
	);
	@UseDataGen(LauncherBeltDataGenerator.class)
	public static final BlockItem LAUNCHER_BELT = BigTechItems.registerBelt(
		FunctionalBlocks.LAUNCHER_BELT
	);

	//////////////////////////////// ascenders ////////////////////////////////

	@UseDataGen(AscenderDataGenerator.class)
	public static final AscenderBlockItem ASCENDER = BigTechItems.register(
		"ascender",
		new AscenderBlockItem(
			FunctionalBlocks.ASCENDER,
			new Item.Settings()
		)
	);
	@UseDataGen(DescenderDataGenerator.class)
	public static final AscenderBlockItem DESCENDER = BigTechItems.register(
		"descender",
		new AscenderBlockItem(
			FunctionalBlocks.DESCENDER,
			new Item.Settings()
		)
	);

	//////////////////////////////// automation ////////////////////////////////

	@UseDataGen(WoodenHopperDataGenerator.class)
	public static final BlockItem WOODEN_HOPPER = BigTechItems.registerPlacer(
		FunctionalBlocks.WOODEN_HOPPER
	);

	@UseDataGen(IgnitorDataGenerator.class)
	public static final BlockItem IGNITOR = BigTechItems.registerPlacer(
		FunctionalBlocks.IGNITOR
	);

	@UseDataGen(SilverIodideCannonDataGenerator.class)
	public static final BlockItem SILVER_IODIDE_CANNON = BigTechItems.registerPlacer(
		FunctionalBlocks.SILVER_IODIDE_CANNON
	);

	@UseDataGen(SpawnerInterceptorDataGenerator.class)
	public static final BlockItem SPAWNER_INTERCEPTOR = BigTechItems.registerPlacer(
		FunctionalBlocks.SPAWNER_INTERCEPTOR
	);

	@UseDataGen(PulsarDataGenerator.class)
	public static final BlockItem PULSAR = BigTechItems.registerPlacer(
		FunctionalBlocks.PULSAR
	);

	@UseDataGen(RadioDataGenerator.class)
	public static final BlockItem RADIO = BigTechItems.registerPlacer(
		FunctionalBlocks.RADIO
	);

	//////////////////////////////// encased blocks ////////////////////////////////

	@UseDataGen(EncasedRedstoneBlockDataGenerator.class)
	public static final BlockItem ENCASED_REDSTONE_BLOCK = BigTechItems.registerPlacer(
		FunctionalBlocks.ENCASED_REDSTONE_BLOCK
	);
	@UseDataGen(EncasedSlimeBlockDataGenerator.class)
	public static final BlockItem ENCASED_SLIME_BLOCK = BigTechItems.registerPlacer(
		FunctionalBlocks.ENCASED_SLIME_BLOCK
	);
	@UseDataGen(EncasedSlimeBlockDataGenerator.class)
	public static final BlockItem ENCASED_HONEY_BLOCK = BigTechItems.registerPlacer(
		FunctionalBlocks.ENCASED_HONEY_BLOCK
	);

	//////////////////////////////// lightning stuff ////////////////////////////////

	@UseDataGen(LightningCableDataGenerator.class)
	public static final BlockItem IRON_LIGHTNING_CAbLE = BigTechItems.registerPlacer(
		FunctionalBlocks.IRON_LIGHTNING_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.class)
	public static final BlockItem GOLD_LIGHTNING_CABLE = BigTechItems.registerPlacer(
		FunctionalBlocks.GOLD_LIGHTNING_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.class)
	public static final BlockItem COPPER_LIGHTNING_CABLE = BigTechItems.registerPlacer(
		FunctionalBlocks.COPPER_LIGHTNING_CABLE
	);
	@UseDataGen(LightningDiodeDataGenerator.class)
	public static final BlockItem LIGHTNING_DIODE = BigTechItems.registerPlacer(
		FunctionalBlocks.LIGHTNING_DIODE
	);
	@UseDataGen(LightningElectrodeDataGenerator.class)
	public static final Item LIGHTNING_ELECTRODE = BigTechItems.register(
		"lightning_electrode",
		new Item(new Item.Settings())
	);
	@UseDataGen(QuadLightningElectrodeDataGenerator.class)
	public static final Item QUAD_LIGHTNING_ELECTRODE = BigTechItems.register(
		"quad_lightning_electrode",
		new Item(new Item.Settings())
	);
	@UseDataGen(LightningBatteryDataGenerator.class)
	public static final LightningBatteryItem LIGHTNING_BATTERY = BigTechItems.register(
		"lightning_battery",
		new LightningBatteryItem(
			new Item.Settings()
			.component(BigTechDataComponents.LIGHTNING_CAPACITY, 1000)
			.component(BigTechDataComponents.LIGHTNING_ENERGY, 0)
		)
	);
	@UseDataGen(SmallLightningJarDataGenerator.class)
	public static final BlockItem SMALL_LIGHTNING_JAR = BigTechItems.registerPlacer(
		FunctionalBlocks.SMALL_LIGHTNING_JAR
	);
	@UseDataGen(LargeLightningJarDataGenerator.class)
	public static final BlockItem LARGE_LIGHTNING_JAR = BigTechItems.registerPlacer(
		FunctionalBlocks.LARGE_LIGHTNING_JAR
	);
	@UseDataGen(TransmuterDataGenerator.class)
	public static final BlockItem TRANSMUTER = BigTechItems.registerPlacer(
		FunctionalBlocks.TRANSMUTER
	);
	@UseDataGen(CopperCoilDataGenerator.class)
	public static final BlockItem COPPER_COIL = BigTechItems.registerPlacer(
		FunctionalBlocks.COPPER_COIL
	);
	@UseDataGen(ConductiveAnvilDataGenerator.class)
	public static final CopperItemCollection CONDUCTIVE_ANVILS = new CopperItemCollection(
		(CopperRegistrableCollection.Type type) -> type.noCopperPrefix + "conductive_anvil",
		(CopperRegistrableCollection.Type type) -> new BlockItem(FunctionalBlocks.CONDUCTIVE_ANVILS.get(type), new Item.Settings())
	);

	//////////////////////////////// lasers ////////////////////////////////

	@UseDataGen(LightningTransmitterDataGenerator.class)
	public static final BlockItem LIGHTNING_TRANSMITTER = BigTechItems.registerPlacer(
		FunctionalBlocks.LIGHTNING_TRANSMITTER
	);
	@UseDataGen(RedstoneTransmitterDataGenerator.class)
	public static final BlockItem REDSTONE_TRANSMITTER = BigTechItems.registerPlacer(
		FunctionalBlocks.REDSTONE_TRANSMITTER
	);
	@UseDataGen(RedstoneReceiverDataGenerator.class)
	public static final BlockItem REDSTONE_RECEIVER = BigTechItems.registerPlacer(
		FunctionalBlocks.REDSTONE_RECEIVER
	);
	@UseDataGen(BeamInterceptorDataGenerator.class)
	public static final BlockItem BEAM_INTERCEPTOR = BigTechItems.register(
		"beam_interceptor",
		new ClientNbtCopyingBlockItem(
			FunctionalBlocks.BEAM_INTERCEPTOR,
			new Item.Settings()
		)
	);
	@UseDataGen(TripwireDataGenerator.class)
	public static final BlockItem TRIPWIRE = BigTechItems.registerPlacer(
		FunctionalBlocks.TRIPWIRE
	);
	@UseDataGen(SpotlightDataGenerator.class)
	public static final BlockItem SPOTLIGHT = BigTechItems.registerPlacer(
		FunctionalBlocks.SPOTLIGHT
	);
	@UseDataGen(ShortRangeDeployerDataGenerator.class)
	public static final BlockItem SHORT_RANGE_DEPLOYER = BigTechItems.registerPlacer(
		FunctionalBlocks.SHORT_RANGE_DEPLOYER
	);
	@UseDataGen(LongRangeDeployerDataGenerator.class)
	public static final BlockItem LONG_RANGE_DEPLOYER = BigTechItems.registerPlacer(
		FunctionalBlocks.LONG_RANGE_DEPLOYER
	);
	@UseDataGen(ShortRangeDestroyerDataGenerator.class)
	public static final BlockItem SHORT_RANGE_DESTROYER = BigTechItems.registerPlacer(
		FunctionalBlocks.SHORT_RANGE_DESTROYER
	);
	@UseDataGen(LongRangeDestroyerDataGenerator.class)
	public static final BlockItem LONG_RANGE_DESTROYER = BigTechItems.registerPlacer(
		FunctionalBlocks.LONG_RANGE_DESTROYER
	);
	@UseDataGen(IgnitorBeamDataGenerator.class)
	public static final BlockItem IGNITOR_BEAM = BigTechItems.registerPlacer(
		FunctionalBlocks.IGNITOR_BEAM
	);
	@UseDataGen(MirrorDataGenerator.class)
	public static final BlockItem MIRROR = BigTechItems.registerPlacer(
		FunctionalBlocks.MIRROR
	);
	@UseDataGen(PrismDataGenerator.class)
	public static final BlockItem PRISM = BigTechItems.register(
		"prism",
		new ClientNbtCopyingBlockItem(
			FunctionalBlocks.PRISM,
			new Item.Settings()
		)
	);
	@UseDataGen(LensDataGenerator.class)
	public static final Item LENS = BigTechItems.register(
		"lens",
		new Item(new Item.Settings())
	);
	@UseDataGen(PhaseScramblerDataGenerator.class)
	public static final BlockItem PHASE_SCRAMBLER = BigTechItems.registerPlacer(
		FunctionalBlocks.PHASE_SCRAMBLER
	);
	@UseDataGen(PhaseAlignerDataGenerator.class)
	public static final BlockItem PHASE_ALIGNER = BigTechItems.registerPlacer(
		FunctionalBlocks.PHASE_ALIGNER
	);

	//////////////////////////////// crystals ////////////////////////////////

	@UseDataGen(CrystalClusterDataGenerator.class)
	public static final CrystalItemCollection CRYSTAL_CLUSTERS = new CrystalItemCollection(
		"crystal_cluster",
		(CrystalColor color) -> new BlockItem(
			FunctionalBlocks.CRYSTAl_ClUSTERS.get(color),
			new Item.Settings()
		)
	);
	@UseDataGen(CrystalDebrisDataGenerator.class)
	public static final Item CRYSTAL_DEBRIS = BigTechItems.register(
		"crystal_debris",
		new Item(new Item.Settings())
	);
	@UseDataGen(CrystallineSandDataGenerator.class)
	public static final BlockItem CRYSTALLINE_SAND = BigTechItems.registerPlacer(
		FunctionalBlocks.CRYSTALLINE_SAND
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(MagnetiteNuggetDataGenerator.class)
	public static final Item MAGNETITE_NUGGET = BigTechItems.register(
		"magnetite_nugget",
		new Item(new Item.Settings())
	);
	@UseDataGen(MagnetiteIngotDataGenerator.class)
	public static final Item MAGNETITE_INGOT = BigTechItems.register(
		"magnetite_ingot",
		new Item(new Item.Settings())
	);
	@UseDataGen(MagnetiteBlockDataGenerator.class)
	public static final BlockItem MAGNETITE_BLOCK = BigTechItems.registerPlacer(
		FunctionalBlocks.MAGNETITE_BLOCK
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Helmet.class)
	public static final ArmorItem MAGNETITE_HELMET = BigTechItems.register(
		"magnetite_helmet",
		new ArmorItem(
			MagnetiteArmorMaterial.INSTANCE,
			ArmorItem.Type.HELMET,
			new Item.Settings()
			.maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Chestplate.class)
	public static final ArmorItem MAGNETITE_CHESTPLATE = BigTechItems.register(
		"magnetite_chestplate",
		new ArmorItem(
			MagnetiteArmorMaterial.INSTANCE,
			ArmorItem.Type.CHESTPLATE,
			new Item.Settings()
			.maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Leggings.class)
	public static final ArmorItem MAGNETITE_LEGGINGS = BigTechItems.register(
		"magnetite_leggings",
		new ArmorItem(
			MagnetiteArmorMaterial.INSTANCE,
			ArmorItem.Type.LEGGINGS,
			new Item.Settings()
			.maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Boots.class)
	public static final ArmorItem MAGNETITE_BOOTS = BigTechItems.register(
		"magnetite_boots",
		new ArmorItem(
			MagnetiteArmorMaterial.INSTANCE,
			ArmorItem.Type.BOOTS,
			new Item.Settings()
			.maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))
		)
	);
	@UseDataGen(CopperNuggetDataGenerator.class)
	public static final Item COPPER_NUGGET = BigTechItems.register(
		BigTechMod.modID("copper_nugget"),
		new Item(new Item.Settings())
	);
	@UseDataGen(MediumWeightedPressurePlateDataGenerator.class)
	public static final BlockItem MEDIUM_WEIGHTED_PRESSURE_PLATE = BigTechItems.registerPlacer(
		FunctionalBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE
	);
	@UseDataGen(MinerToolDataGenerator.class)
	public static final Item MINER_TOOL = BigTechItems.register(
		"miner_tool",
		new MinerToolItem(new Item.Settings())
	);
	@UseDataGen(StoneCraftingTableDataGenerator.class)
	public static final BlockItem STONE_CRAFTING_TABLE = BigTechItems.registerPlacer(
		FunctionalBlocks.STONE_CRAFTING_TABLE
	);
	@UseDataGen(TechnoCrafterDataGenerator.class)
	public static final BlockItem TECHNO_CRAFTER = BigTechItems.registerPlacer(
		FunctionalBlocks.TECHNO_CRAFTER
	);
	@UseDataGen(PortableTechnoCrafterDataGenerator.class)
	public static final PortableTechnoCrafterItem PORTABLE_TECHNO_CRAFTER = BigTechItems.register(
		"portable_techno_crafter",
		new PortableTechnoCrafterItem(new Item.Settings().maxCount(1))
	);
	@UseDataGen(MagneticArrowDataGenerator.class)
	public static final MagneticArrowItem MAGNETIC_ARROW = BigTechItems.register(
		"magnetic_arrow",
		new MagneticArrowItem(new Item.Settings())
	);
	@UseDataGen(DislocatorDataGenerator.class)
	public static final DislocatorItem DISLOCATOR = BigTechItems.register(
		"dislocator",
		new DislocatorItem(
			new Item.Settings().maxDamage(64)
		)
	);

	public static void init() {
		FuelRegistry.INSTANCE.add(BigTechItemTags.WOODEN_FRAMES, 300);
		FuelRegistry.INSTANCE.add(BigTechItemTags.WOODEN_CATWALK_PLATFORMS, 300);
		FuelRegistry.INSTANCE.add(BigTechItemTags.WOODEN_CATWALK_STAIRS, 300);
		FuelRegistry.INSTANCE.add(CRYSTAL_DEBRIS, 1200);
	}
}