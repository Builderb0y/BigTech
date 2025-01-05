package builderb0y.bigtech.blocks;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.blocks.belts.*;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.CrucibleDataGenerator;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;

import static builderb0y.bigtech.blocks.BigTechBlocks.register;
import static builderb0y.bigtech.blocks.BigTechBlocks.settings;
import static builderb0y.bigtech.blocks.BigTechBlocks.copySettings;

public class FunctionalBlocks {

	//////////////////////////////// belts ////////////////////////////////

	@UseDataGen(void.class)
	public static final DirectionalBeltBlock BELT = register(
		new DirectionalBeltBlock(
			settings("belt")
			.mapColor(MapColor.STONE_GRAY)
			.strength(0.2F)
			.pistonBehavior(PistonBehavior.DESTROY)
		)
	);
	@UseDataGen(void.class)
	public static final LauncherBeltBlock LAUNCHER_BELT = register(
		new LauncherBeltBlock(
			copySettings(BELT, "launcher_belt")
		)
	);
	@UseDataGen(void.class)
	public static final InjectorBeltBlock INJECTOR_BELT = register(
		new InjectorBeltBlock(
			copySettings(BELT, "injector_belt")
		)
	);
	@UseDataGen(void.class)
	public static final EjectorBeltBlock EJECTOR_BELT = register(
		new EjectorBeltBlock(
			copySettings(BELT, "ejector_belt")
		)
	);
	@UseDataGen(void.class)
	public static final SorterBeltBlock SORTER_BELT = register(
		new SorterBeltBlock(
			copySettings(BELT, "sorter_belt")
		)
	);
	@UseDataGen(void.class)
	public static final DetectorBeltBlock DETECTOR_BELT = register(
		new DetectorBeltBlock(
			copySettings(BELT, "detector_belt")
		)
	);
	@UseDataGen(void.class)
	public static final DirectorBeltBlock DIRECTOR_BELT = register(
		new DirectorBeltBlock(
			copySettings(BELT, "director_belt")
		)
	);
	@UseDataGen(void.class)
	public static final TrapdoorBeltBlock TRAPDOOR_BELT = register(
		new TrapdoorBeltBlock(
			copySettings(BELT, "trapdoor_belt").nonOpaque()
		)
	);
	@UseDataGen(void.class)
	public static final BrakeBeltBlock BRAKE_BELT = register(
		new BrakeBeltBlock(
			copySettings(BELT, "brake_belt")
		)
	);
	@UseDataGen(void.class)
	public static final SpeedyBeltBlock SPEEDY_BELT = register(
		new SpeedyBeltBlock(
			copySettings(BELT, "speedy_belt")
		)
	);

	//////////////////////////////// ascenders ////////////////////////////////

	@UseDataGen(void.class)
	public static final AscenderBlock ASCENDER = register(
		new AscenderBlock(
			settings("ascender")
			.mapColor(MapColor.STONE_GRAY)
			.nonOpaque()
			.strength(0.2F)
			.allowsSpawning(Blocks::never)
			.solidBlock(Blocks::never)
			.suffocates(Blocks::never)
			.blockVision(Blocks::never),
			Direction.UP
		)
	);
	@UseDataGen(void.class)
	public static final AscenderBlock DESCENDER = register(
		new AscenderBlock(
			copySettings(ASCENDER, "descender"),
			Direction.DOWN
		)
	);

	//////////////////////////////// automation ////////////////////////////////

	@UseDataGen(void.class)
	public static final WoodenHopperBlock WOODEN_HOPPER = register(
		new WoodenHopperBlock(
			settings("wooden_hopper")
			.mapColor(MapColor.OAK_TAN)
			.strength(2.5F)
			.sounds(BlockSoundGroup.WOOD)
			.burnable()
		)
	);

	@UseDataGen(void.class)
	public static final IgnitorBlock IGNITOR = register(
		new IgnitorBlock(
			copySettings(Blocks.FURNACE, "ignitor")
		)
	);

	@UseDataGen(void.class)
	public static final SilverIodideCannonBlock SILVER_IODIDE_CANNON = register(
		new SilverIodideCannonBlock(
			settings("silver_iodide_cannon")
			.mapColor(MapColor.BLACK)
			.requiresTool()
			.strength(5.0F, 50.0F)
		)
	);

	@UseDataGen(void.class)
	public static final SpawnerInterceptorBlock SPAWNER_INTERCEPTOR = register(
		new SpawnerInterceptorBlock(
			settings("spawner_interceptor")
			.mapColor(MapColor.BLACK)
			.requiresTool()
			.strength(10.0F, 250.0F)
		)
	);

	@UseDataGen(void.class)
	public static final PulsarBlock PULSAR = register(
		new PulsarBlock(
			settings("pulsar")
			.mapColor((BlockState state) -> state.get(Properties.POWERED) ? MapColor.RED : MapColor.DARK_RED)
			.solidBlock(Blocks::never) //ensures pulsars can't conduct a redstone signal through them.
			.strength(1.5F)
			.sounds(BlockSoundGroup.WOOD)
		)
	);

	@UseDataGen(void.class)
	public static final RadioBlock RADIO = register(
		new RadioBlock(
			settings("radio")
			.mapColor(MapColor.BLACK)
			.breakInstantly()
		)
	);

	//////////////////////////////// encased blocks ////////////////////////////////

	@UseDataGen(void.class)
	public static final EncasedRedstoneBlock ENCASED_REDSTONE_BLOCK = register(
		new EncasedRedstoneBlock(
			settings("encased_redstone_block")
			.mapColor((BlockState state) -> state.get(Properties.FACING) == Direction.UP ? MapColor.BRIGHT_RED : MapColor.STONE_GRAY)
			.strength(3.0F, 6.0F)
			.requiresTool()
		)
	);
	@UseDataGen(void.class)
	public static final EncasedSlimeBlock ENCASED_SLIME_BLOCK = register(
		new EncasedSlimeBlock(
			settings("encased_slime_block")
			.mapColor((BlockState state) -> state.get(Properties.FACING) == Direction.UP ? MapColor.PALE_GREEN : MapColor.STONE_GRAY)
			.strength(3.0F, 6.0F)
			.requiresTool(),
			false
		)
	);
	@UseDataGen(void.class)
	public static final EncasedSlimeBlock ENCASED_HONEY_BLOCK = register(
		new EncasedSlimeBlock(
			settings("encased_honey_block")
			.mapColor((BlockState state) -> state.get(Properties.FACING) == Direction.UP ? MapColor.ORANGE : MapColor.STONE_GRAY)
			.strength(3.0F, 6.0F)
			.requiresTool(),
			true
		)
	);

	//////////////////////////////// lightning stuff ////////////////////////////////

	@UseDataGen(void.class)
	public static final LightningCableBlock IRON_LIGHTNING_CABLE = register(
		new LightningCableBlock(
			settings("iron_lightning_cable")
			.mapColor(MapColor.GRAY)
			.strength(0.8F)
			.sounds(BlockSoundGroup.WOOL)
		)
	);
	@UseDataGen(void.class)
	public static final LightningCableBlock COPPER_LIGHTNING_CABLE = register(
		new LightningCableBlock(
			copySettings(IRON_LIGHTNING_CABLE, "copper_lightning_cable")
		)
	);
	@UseDataGen(void.class)
	public static final LightningCableBlock GOLD_LIGHTNING_CABLE = register(
		new LightningCableBlock(
			copySettings(IRON_LIGHTNING_CABLE, "gold_lightning_cable")
		)
	);
	@UseDataGen(void.class)
	public static final LightningDiodeBlock LIGHTNING_DIODE = register(
		new LightningDiodeBlock(
			copySettings(IRON_LIGHTNING_CABLE, "lightning_diode")
		)
	);
	@UseDataGen(void.class)
	public static final SmallLightningJarBlock SMALL_LIGHTNING_JAR = register(
		new SmallLightningJarBlock(
			settings("small_lightning_jar")
			.mapColor(MapColor.ORANGE)
			.strength(2.0F, 1.0F)
			.nonOpaque()
			.requiresTool()
			.sounds(BlockSoundGroup.COPPER)
		)
	);
	@UseDataGen(void.class)
	public static final LargeLightningJarBlock LARGE_LIGHTNING_JAR = register(
		new LargeLightningJarBlock(
			copySettings(SMALL_LIGHTNING_JAR, "large_lightning_jar")
		)
	);
	@UseDataGen(void.class)
	public static final TransmuterBlock TRANSMUTER = register(
		new TransmuterBlock(
			settings("transmuter")
			.mapColor(MapColor.BLACK)
			.requiresTool()
			.nonOpaque()
			.strength(20.0F, 600.0F)
		)
	);
	@UseDataGen(void.class)
	public static final CopperCoilBlock COPPER_COIL = register(
		new CopperCoilBlock(
			copySettings(Blocks.COPPER_BLOCK, "copper_coil")
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection CONDUCTIVE_ANVILS = new CopperBlockCollection(
		true,
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = (
				copySettings(Blocks.ANVIL, type.noCopperPrefix + "conductive_anvil")
				.mapColor(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type).getDefaultMapColor())
				.sounds(BigTechBlocks.CONDUCTIVE_ANVIL_SOUND_GROUP)
			);
			return (
				type.waxed
				? new ConductiveAnvilBlock(settings)
				: new OxidizableConductiveAnvilBlock(settings, type.level)
			);
		}
	);
	@UseDataGen(CrucibleDataGenerator.class)
	public static final CrucibleBlock CRUCIBLE = register(
		new CrucibleBlock(
			copySettings(Blocks.CAULDRON, "crucible")
		)
	);
	@UseDataGen(void.class)
	public static final ArcFurnaceElectrodeBlock ARC_FURNACE_ELECTRODE = register(
		new ArcFurnaceElectrodeBlock(
			copySettings(Blocks.CAULDRON, "arc_furnace_electrode")
		)
	);

	//////////////////////////////// lasers ////////////////////////////////

	@UseDataGen(void.class)
	public static final LightningTransmitterBlock LIGHTNING_TRANSMITTER = register(
		new LightningTransmitterBlock(
			settings("lightning_transmitter")
			.mapColor(MapColor.ORANGE)
			.requiresTool()
			.strength(1.0F)
		)
	);
	@UseDataGen(void.class)
	public static final RedstoneTransmitterBlock REDSTONE_TRANSMITTER = register(
		new RedstoneTransmitterBlock(
			settings("redstone_transmitter")
			.mapColor(MapColor.OFF_WHITE)
			.requiresTool()
			.strength(1.0F)
		)
	);
	@UseDataGen(void.class)
	public static final RedstoneReceiverBlock REDSTONE_RECEIVER = register(
		new RedstoneReceiverBlock(
			settings("redstone_receiver")
			.mapColor(MapColor.OFF_WHITE)
			.requiresTool()
			.strength(1.0F)
		)
	);
	@UseDataGen(void.class)
	public static final BeamInterceptorBlock BEAM_INTERCEPTOR = register(
		new BeamInterceptorBlock(
			settings("beam_interceptor")
			.pistonBehavior(PistonBehavior.DESTROY)
			.nonOpaque()
			.strength(0.2F)
		)
	);
	@UseDataGen(void.class)
	public static final TripwireBeamBlock TRIPWIRE = register(
		new TripwireBeamBlock(
			settings("tripwire")
			.mapColor(MapColor.OAK_TAN)
			.requiresTool()
			.strength(1.0F)
		)
	);
	@UseDataGen(void.class)
	public static final SpotlightBlock SPOTLIGHT = register(
		new SpotlightBlock(
			settings("spotlight")
			.mapColor((BlockState state) -> {
				return state.get(Properties.FACING) == Direction.UP ? MapColor.PALE_YELLOW : MapColor.BLACK;
			})
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);
	@UseDataGen(void.class)
	public static final ShortRangeDeployerBlock SHORT_RANGE_DEPLOYER = register(
		new ShortRangeDeployerBlock(
			settings("short_range_deployer")
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);
	@UseDataGen(void.class)
	public static final LongRangeDeployerBlock LONG_RANGE_DEPLOYER = register(
		new LongRangeDeployerBlock(
			settings("long_range_deployer")
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);
	@UseDataGen(void.class)
	public static final ShortRangeDestroyerBlock SHORT_RANGE_DESTROYER = register(
		new ShortRangeDestroyerBlock(
			settings("short_range_destroyer")
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);
	@UseDataGen(void.class)
	public static final LongRangeDestroyerBlock LONG_RANGE_DESTROYER = register(
		new LongRangeDestroyerBlock(
			settings("long_range_destroyer")
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);
	@UseDataGen(void.class)
	public static final IgnitorBeamBlock IGNITOR_BEAM = register(
		new IgnitorBeamBlock(
			settings("ignitor_beam")
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(3.0F)
		)
	);
	@UseDataGen(void.class)
	public static final MirrorBlock MIRROR = register(
		new MirrorBlock(
			settings("mirror")
			.mapColor(MapColor.IRON_GRAY)
			.nonOpaque()
			.strength(0.6F)
			.requiresTool()
		)
	);
	@UseDataGen(void.class)
	public static final PrismBlock PRISM = register(
		new PrismBlock(
			copySettings(Blocks.GLASS, "prism")
		)
	);
	@UseDataGen(void.class)
	public static final PhaseManipulatorBlock PHASE_SCRAMBLER = register(
		new PhaseManipulatorBlock(
			copySettings(Blocks.BLACK_STAINED_GLASS, "phase_scrambler")
			.allowsSpawning(Blocks::never)
			.solidBlock(Blocks::never)
			.suffocates(Blocks::never)
			.blockVision(Blocks::never)
			.sounds(BlockSoundGroup.STONE),
			false
		)
	);
	@UseDataGen(void.class)
	public static final PhaseManipulatorBlock PHASE_ALIGNER = register(
		new PhaseManipulatorBlock(
			copySettings(Blocks.WHITE_STAINED_GLASS, "phase_aligner")
			.allowsSpawning(Blocks::never)
			.solidBlock(Blocks::never)
			.suffocates(Blocks::never)
			.blockVision(Blocks::never)
			.sounds(BlockSoundGroup.STONE),
			true
		)
	);

	//////////////////////////////// crystals ////////////////////////////////

	@UseDataGen(void.class)
	public static final CrystalBlockCollection CRYSTAl_ClUSTERS = new CrystalBlockCollection(
		true,
		(CrystalColor color) -> {
			return new CrystalClusterBlock(
				settings(color.prefix + "crystal_cluster")
				.mapColor(color.closestDyeColor)
				.strength(0.3F)
				.sounds(BlockSoundGroup.AMETHYST_CLUSTER)
				.nonOpaque()
				.allowsSpawning(Blocks::never)
				.solidBlock(Blocks::never)
				.suffocates(Blocks::never)
				.blockVision(Blocks::never)
				.luminance((BlockState state) -> 7),
				color
			);
		}
	);
	@UseDataGen(void.class)
	public static final CrystallineSandBlock CRYSTALLINE_SAND = register(
		new CrystallineSandBlock(
			copySettings(Blocks.SAND, "crystalline_sand")
		)
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(void.class)
	public static final Block CAST_IRON_BLOCK = register(
		new Block(
			copySettings(Blocks.IRON_BLOCK, "cast_iron_block")
		)
	);
	@UseDataGen(void.class)
	public static final MagnetiteBlock MAGNETITE_BLOCK = register(
		new MagnetiteBlock(
			copySettings(Blocks.IRON_BLOCK, "magnetite_block")
			.mapColor(MapColor.BRIGHT_RED)
		)
	);
	@UseDataGen(void.class)
	public static final WeightedPressurePlateBlock MEDIUM_WEIGHTED_PRESSURE_PLATE = register(
		new WeightedPressurePlateBlock(
			60,
			BigTechBlocks.COPPER_BLOCK_SET_TYPE,
			settings("medium_weighted_pressure_plate")
			.mapColor(MapColor.ORANGE)
			.requiresTool()
			.noCollision()
			.strength(0.5F)
			.pistonBehavior(PistonBehavior.DESTROY)
		)
	);
	@UseDataGen(void.class)
	public static final StoneCraftingTableBlock STONE_CRAFTING_TABLE = register(
		new StoneCraftingTableBlock(
			copySettings(Blocks.COBBLESTONE, "stone_crafting_table")
		)
	);
	@UseDataGen(void.class)
	public static final TechnoCrafterBlock TECHNO_CRAFTER = register(
		new TechnoCrafterBlock(
			settings("techno_crafter")
			.mapColor(MapColor.WHITE_GRAY)
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);

	public static void init() {}
}