package builderb0y.bigtech.items;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.datagen.impl.functional.lasers.*;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.*;
import builderb0y.bigtech.datagen.impl.functional.*;
import builderb0y.bigtech.datagen.impl.functional.arcFurnace.ArcFurnaceElectrodeDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.ascenders.AscenderDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.ascenders.DescenderDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.SteelDoorDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.SteelPressurePlateDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.destroyers.LongRangeDeployerDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.destroyers.LongRangeDestroyerDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.destroyers.ShortRangeDeployerDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.destroyers.ShortRangeDestroyerDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.belts.*;
import builderb0y.bigtech.datagen.impl.functional.lasers.phase.PhaseAlignerDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.lasers.phase.PhaseScramblerDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.lightning.*;
import builderb0y.bigtech.datagen.impl.functional.ConductiveAnvilDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.lightning.jars.LargeLightningJarDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.lightning.jars.SmallLightningJarDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.magnets.*;
import builderb0y.bigtech.datagen.impl.functional.technoCrafters.PortableTechnoCrafterDataGenerator;
import builderb0y.bigtech.datagen.impl.functional.technoCrafters.TechnoCrafterDataGenerator;
import builderb0y.bigtech.datagen.impl.material.circuits.MicroProcessorDataGenerator;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

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

	@UseDataGen(IgniterDataGenerator.class)
	public static final BlockItem IGNITER = registerPlacer(
		FunctionalBlocks.IGNITER
	);
	static {
		Registries.ITEM.addAlias(
			BigTechMod.modID("ignitor"),
			BigTechMod.modID("igniter")
		);
	}

	@UseDataGen(SilverIodideCannonDataGenerator.class)
	public static final BlockItem SILVER_IODIDE_CANNON = registerPlacer(
		FunctionalBlocks.SILVER_IODIDE_CANNON
	);

	@UseDataGen(SpawnerInterceptorDataGenerator.class)
	public static final BlockItem SPAWNER_INTERCEPTOR = registerPlacer(
		FunctionalBlocks.SPAWNER_INTERCEPTOR
	);

	@UseDataGen(RadioDataGenerator.class)
	public static final BlockItem RADIO = registerPlacer(
		FunctionalBlocks.RADIO,
		RadioBlockItem::new
	);

	//////////////////////////////// redstone ////////////////////////////////

	@UseDataGen(PulsarDataGenerator.class)
	public static final BlockItem PULSAR = registerPlacer(
		FunctionalBlocks.PULSAR
	);

	@UseDataGen(AssemblerDataGenerator.class)
	public static final BlockItem ASSEMBLER = registerPlacer(
		FunctionalBlocks.ASSEMBLER
	);

	@UseDataGen(MicroProcessorDataGenerator.class)
	public static final MicroProcessorBlockItem MICRO_PROCESSOR = registerPlacer(
		FunctionalBlocks.MICRO_PROCESSOR,
		MicroProcessorBlockItem::new
	);

	@UseDataGen(MagnifyingGlassDataGenerator.class)
	public static final Item MAGNIFYING_GLASS = register(
		new Item(settings("magnifying_glass", false).maxCount(1))
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
	@UseDataGen(SteelPistonDataGenerator.class)
	public static final BlockItem STEEL_PISTON = registerPlacer(
		FunctionalBlocks.STEEL_PISTON
	);
	@UseDataGen(SteelPistonDataGenerator.class)
	public static final BlockItem STICKY_STEEL_PISTON = registerPlacer(
		FunctionalBlocks.STICKY_STEEL_PISTON
	);

	//////////////////////////////// lightning stuff ////////////////////////////////

	@UseDataGen(LightningCableDataGenerator.Iron.class)
	public static final BlockItem IRON_LIGHTNING_CAbLE = registerPlacer(
		FunctionalBlocks.IRON_LIGHTNING_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.Steel.class)
	public static final BlockItem STEEL_LIGHTNING_CABLE = registerPlacer(
		FunctionalBlocks.STEEL_LIGHTNINT_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.Copper.class)
	public static final BlockItem COPPER_LIGHTNING_CABLE = registerPlacer(
		FunctionalBlocks.COPPER_LIGHTNING_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.Gold.class)
	public static final BlockItem GOLD_LIGHTNING_CABLE = registerPlacer(
		FunctionalBlocks.GOLD_LIGHTNING_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.Silver.class)
	public static final BlockItem SILVER_LIGHTNING_CABLE = registerPlacer(
		FunctionalBlocks.SILVER_LIGHTNING_CABLE
	);
	@UseDataGen(LightningCableDataGenerator.Electrum.class)
	public static final BlockItem ELECTRUM_LIGHTNING_CABLE = registerPlacer(
		FunctionalBlocks.ELECTRUM_LIGHTNING_CABLE
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
		FunctionalBlocks.SMALL_LIGHTNING_JAR,
		LightningJarBlockItem::new
	);
	@UseDataGen(LargeLightningJarDataGenerator.class)
	public static final BlockItem LARGE_LIGHTNING_JAR = registerPlacer(
		FunctionalBlocks.LARGE_LIGHTNING_JAR,
		LightningJarBlockItem::new
	);
	@UseDataGen(TransmuterDataGenerator.class)
	public static final BlockItem TRANSMUTER = registerPlacer(
		FunctionalBlocks.TRANSMUTER
	);
	@UseDataGen(CopperCoilDataGenerator.class)
	public static final BlockItem COPPER_COIL = registerPlacer(
		FunctionalBlocks.COPPER_COIL
	);
	@UseDataGen(ElectrumCoilDataGenerator.class)
	public static final BlockItem ELECTRUM_COIL = registerPlacer(
		FunctionalBlocks.ELECTRUM_COIL
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
	public static final BlockItem BEAM_INTERCEPTOR = registerPlacer(
		FunctionalBlocks.BEAM_INTERCEPTOR,
		ClientNbtCopyingBlockItem::new
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
	@UseDataGen(IgniterBeamDataGenerator.class)
	public static final BlockItem IGNITER_BEAM = registerPlacer(
		FunctionalBlocks.IGNITER_BEAM
	);
	static {
		Registries.ITEM.addAlias(
			BigTechMod.modID("ignitor_beam"),
			BigTechMod.modID("igniter_beam")
		);
	}
	@UseDataGen(MirrorDataGenerator.class)
	public static final BlockItem MIRROR = registerPlacer(
		FunctionalBlocks.MIRROR
	);
	@UseDataGen(PrismDataGenerator.class)
	public static final BlockItem PRISM = registerPlacer(
		FunctionalBlocks.PRISM,
		ClientNbtCopyingBlockItem::new
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

	//////////////////////////////// magnets ////////////////////////////////

	@UseDataGen(FerromagneticAttractorDataGenerator.class)
	public static final BlockItem FERROMAGNETIC_ATTRACTOR = registerPlacer(
		FunctionalBlocks.FERROMAGNETIC_ATTRACTOR
	);
	static {
		Registries.ITEM.addAlias(BigTechMod.modID("magnetite_block"), BigTechMod.modID("ferromagnetic_attractor"));
	}
	@UseDataGen(FerromagneticRepulsorDataGenerator.class)
	public static final BlockItem FERROMAGNETIC_REPULSOR = registerPlacer(
		FunctionalBlocks.FERROMAGNETIC_REPULSOR
	);
	@UseDataGen(FerromagneticTranslatorDataGenerator.class)
	public static final BlockItem FERROMAGNETIC_TRANSLATOR = registerPlacer(
		FunctionalBlocks.FERROMAGNETIC_TRANSLATOR
	);
	@UseDataGen(ElectromagneticAttractorDataGenerator.class)
	public static final BlockItem ELECTROMAGNETIC_ATTRACTOR = registerPlacer(
		FunctionalBlocks.ELECTROMAGNETIC_ATTRACTOR
	);
	@UseDataGen(ElectromagneticRepulsorDataGenerator.class)
	public static final BlockItem ELECTROMAGNETIC_REPULSOR = registerPlacer(
		FunctionalBlocks.ELECTROMAGNETIC_REPULSOR
	);
	@UseDataGen(ElectromagneticTranslatorDataGenerator.class)
	public static final BlockItem ELECTROMAGNETIC_TRANSLATOR = registerPlacer(
		FunctionalBlocks.ELECTROMAGNETIC_TRANSLATOR
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(MagnetiteArmorDataGenerator.Helmet.class)
	public static final Item MAGNETITE_HELMET = register(
		new Item(
			settings("magnetite_helmet", false).armor(
				MagnetiteArmorMaterial.INSTANCE,
				EquipmentType.HELMET
			)
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Chestplate.class)
	public static final Item MAGNETITE_CHESTPLATE = register(
		new Item(
			settings("magnetite_chestplate", false).armor(
				MagnetiteArmorMaterial.INSTANCE,
				EquipmentType.CHESTPLATE
			)
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Leggings.class)
	public static final Item MAGNETITE_LEGGINGS = register(
		new Item(
			settings("magnetite_leggings", false).armor(
				MagnetiteArmorMaterial.INSTANCE,
				EquipmentType.LEGGINGS
			)
		)
	);
	@UseDataGen(MagnetiteArmorDataGenerator.Boots.class)
	public static final Item MAGNETITE_BOOTS = register(
		new Item(
			settings("magnetite_boots", false).armor(
				MagnetiteArmorMaterial.INSTANCE,
				EquipmentType.BOOTS
			)
		)
	);
	@UseDataGen(MediumWeightedPressurePlateDataGenerator.class)
	public static final BlockItem MEDIUM_WEIGHTED_PRESSURE_PLATE = registerPlacer(
		FunctionalBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE
	);
	@UseDataGen(SteelPressurePlateDataGenerator.class)
	public static final BlockItem STEEL_PRESSURE_PLATE = registerPlacer(
		FunctionalBlocks.STEEL_PRESSURE_PLATE
	);
	@UseDataGen(SteelDoorDataGenerator.class)
	public static final TallBlockItem STEEL_DOOR = registerPlacer(
		FunctionalBlocks.STEEL_DOOR,
		TallBlockItem::new
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
		});
	}

	public static BeltBlockItem registerBelt(Block block) {
		Identifier id = Registries.BLOCK.getId(block);
		return register(id, new BeltBlockItem(block, settings(id, true)));
	}
}