package builderb0y.bigtech.blocks;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.blocks.belts.*;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;
import builderb0y.bigtech.registrableCollections.CrystalClusterRegistrableCollection.CrystalClusterColor;

public class FunctionalBlocks {

	//////////////////////////////// belts ////////////////////////////////

	@UseDataGen(void.class)
	public static final DirectionalBeltBlock BELT = BigTechBlocks.register(
		"belt",
		new DirectionalBeltBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.STONE_GRAY)
			.strength(0.2F)
			.pistonBehavior(PistonBehavior.DESTROY)
		)
	);
	@UseDataGen(void.class)
	public static final LauncherBeltBlock LAUNCHER_BELT = BigTechBlocks.register(
		"launcher_belt",
		new LauncherBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final InjectorBeltBlock INJECTOR_BELT = BigTechBlocks.register(
		"injector_belt",
		new InjectorBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final EjectorBeltBlock EJECTOR_BELT = BigTechBlocks.register(
		"ejector_belt",
		new EjectorBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final SorterBeltBlock SORTER_BELT = BigTechBlocks.register(
		"sorter_belt",
		new SorterBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final DetectorBeltBlock DETECTOR_BELT = BigTechBlocks.register(
		"detector_belt",
		new DetectorBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final DirectorBeltBlock DIRECTOR_BELT = BigTechBlocks.register(
		"director_belt",
		new DirectorBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final TrapdoorBeltBlock TRAPDOOR_BELT = BigTechBlocks.register(
		"trapdoor_belt",
		new TrapdoorBeltBlock(
			AbstractBlock.Settings.copy(BELT).nonOpaque()
		)
	);
	@UseDataGen(void.class)
	public static final BrakeBeltBlock BRAKE_BELT = BigTechBlocks.register(
		"brake_belt",
		new BrakeBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final SpeedyBeltBlock SPEEDY_BELT = BigTechBlocks.register(
		"speedy_belt",
		new SpeedyBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);

	//////////////////////////////// ascenders ////////////////////////////////

	@UseDataGen(void.class)
	public static final AscenderBlock ASCENDER = BigTechBlocks.register(
		"ascender",
		new AscenderBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.STONE_GRAY)
			.nonOpaque()
			.strength(0.2F),
			Direction.UP
		)
	);
	@UseDataGen(void.class)
	public static final AscenderBlock DESCENDER = BigTechBlocks.register(
		"descender",
		new AscenderBlock(
			AbstractBlock.Settings.copy(ASCENDER),
			Direction.DOWN
		)
	);

	//////////////////////////////// automation ////////////////////////////////

	@UseDataGen(void.class)
	public static final WoodenHopperBlock WOODEN_HOPPER = BigTechBlocks.register(
		"wooden_hopper",
		new WoodenHopperBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.OAK_TAN)
			.strength(2.5F)
			.sounds(BlockSoundGroup.WOOD)
			.burnable()
		)
	);

	@UseDataGen(void.class)
	public static final IgnitorBlock IGNITOR = BigTechBlocks.register(
		"ignitor",
		new IgnitorBlock(
			AbstractBlock.Settings.copy(Blocks.FURNACE)
		)
	);

	@UseDataGen(void.class)
	public static final SilverIodideCannonBlock SILVER_IODIDE_CANNON = BigTechBlocks.register(
		"silver_iodide_cannon",
		new SilverIodideCannonBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.BLACK)
			.requiresTool()
			.strength(5.0F, 50.0F)
		)
	);

	@UseDataGen(void.class)
	public static final SpawnerInterceptorBlock SPAWNER_INTERCEPTOR = BigTechBlocks.register(
		"spawner_interceptor",
		new SpawnerInterceptorBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.BLACK)
			.requiresTool()
			.strength(10.0F, 250.0F)
		)
	);

	@UseDataGen(void.class)
	public static final PulsarBlock PULSAR = BigTechBlocks.register(
		"pulsar",
		new PulsarBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor((BlockState state) -> state.get(Properties.POWERED) ? MapColor.RED : MapColor.DARK_RED)
			.strength(1.5F)
		)
	);

	//////////////////////////////// encased blocks ////////////////////////////////

	@UseDataGen(void.class)
	public static final EncasedRedstoneBlock ENCASED_REDSTONE_BLOCK = BigTechBlocks.register(
		"encased_redstone_block",
		new EncasedRedstoneBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor((BlockState state) -> state.get(Properties.FACING) == Direction.UP ? MapColor.BRIGHT_RED : MapColor.STONE_GRAY)
			.strength(3.0F, 6.0F)
			.requiresTool()
		)
	);
	@UseDataGen(void.class)
	public static final EncasedSlimeBlock ENCASED_SLIME_BLOCK = BigTechBlocks.register(
		"encased_slime_block",
		new EncasedSlimeBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor((BlockState state) -> state.get(Properties.FACING) == Direction.UP ? MapColor.PALE_GREEN : MapColor.STONE_GRAY)
			.strength(3.0F, 6.0F)
			.requiresTool(),
			false
		)
	);
	@UseDataGen(void.class)
	public static final EncasedSlimeBlock ENCASED_HONEY_BLOCK = BigTechBlocks.register(
		"encased_honey_block",
		new EncasedSlimeBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor((BlockState state) -> state.get(Properties.FACING) == Direction.UP ? MapColor.ORANGE : MapColor.STONE_GRAY)
			.strength(3.0F, 6.0F)
			.requiresTool(),
			true
		)
	);

	//////////////////////////////// lightning stuff ////////////////////////////////

	@UseDataGen(void.class)
	public static final LightningCableBlock IRON_LIGHTNING_CABLE = BigTechBlocks.register(
		"iron_lightning_cable",
		new LightningCableBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.GRAY)
			.strength(0.8F)
			.sounds(BlockSoundGroup.WOOL)
		)
	);
	@UseDataGen(void.class)
	public static final LightningCableBlock COPPER_LIGHTNING_CABLE = BigTechBlocks.register(
		"copper_lightning_cable",
		new LightningCableBlock(
			AbstractBlock.Settings.copy(IRON_LIGHTNING_CABLE)
		)
	);
	@UseDataGen(void.class)
	public static final LightningCableBlock GOLD_LIGHTNING_CABLE = BigTechBlocks.register(
		"gold_lightning_cable",
		new LightningCableBlock(
			AbstractBlock.Settings.copy(IRON_LIGHTNING_CABLE)
		)
	);
	@UseDataGen(void.class)
	public static final SmallLightningJarBlock SMALL_LIGHTNING_JAR = BigTechBlocks.register(
		"small_lightning_jar",
		new SmallLightningJarBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.ORANGE)
			.strength(2.0F, 1.0F)
			.nonOpaque()
			.requiresTool()
			.sounds(BlockSoundGroup.COPPER)
		)
	);
	@UseDataGen(void.class)
	public static final LargeLightningJarBlock LARGE_LIGHTNING_JAR = BigTechBlocks.register(
		"large_lightning_jar",
		new LargeLightningJarBlock(
			AbstractBlock.Settings.copy(SMALL_LIGHTNING_JAR)
		)
	);
	@UseDataGen(void.class)
	public static final TransmuterBlock TRANSMUTER = BigTechBlocks.register(
		"transmuter",
		new TransmuterBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.BLACK)
			.requiresTool()
			.nonOpaque()
			.strength(20.0F, 600.0F)
		)
	);
	@UseDataGen(void.class)
	public static final CopperCoilBlock COPPER_COIL = BigTechBlocks.register(
		"copper_coil",
		new CopperCoilBlock(
			AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection CONDUCTIVE_ANVILS = new CopperBlockCollection(
		(CopperRegistrableCollection.Type type) -> type.noCopperPrefix + "conductive_anvil",
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = (
				AbstractBlock
				.Settings
				.copy(Blocks.ANVIL)
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

	//////////////////////////////// crystals ////////////////////////////////

	@UseDataGen(void.class)
	public static final CrystalClusterBlockCollection CRYSTAl_ClUSTERS = new CrystalClusterBlockCollection(
		true,
		(CrystalClusterColor color) -> {
			return new CrystalClusterBlock(
				AbstractBlock
				.Settings
				.create()
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
	public static final CrystallineSandBlock CRYSTALLINE_SAND = BigTechBlocks.register(
		"crystalline_sand",
		new CrystallineSandBlock(
			AbstractBlock.Settings.copy(Blocks.SAND)
		)
	);

	//////////////////////////////// lasers ////////////////////////////////

	@UseDataGen(void.class)
	public static final RedstoneTransmitterBlock REDSTONE_TRANSMITTER = BigTechBlocks.register(
		"redstone_transmitter",
		new RedstoneTransmitterBlock(
			AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final RedstoneReceiverBlock REDSTONE_RECEIVER = BigTechBlocks.register(
		"redstone_receiver",
		new RedstoneReceiverBlock(
			AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final BeamInterceptorBlock BEAM_INTERCEPTOR = BigTechBlocks.register(
		"beam_interceptor",
		new BeamInterceptorBlock(
			AbstractBlock
			.Settings
			.create()
			.pistonBehavior(PistonBehavior.DESTROY)
			.nonOpaque()
			.strength(0.2F)
		)
	);
	@UseDataGen(void.class)
	public static final TripwireBeamBlock TRIPWIRE = BigTechBlocks.register(
		"tripwire",
		new TripwireBeamBlock(
			AbstractBlock
			.Settings
			.copy(Blocks.QUARTZ_BLOCK)
			.mapColor(MapColor.OAK_TAN)
		)
	);
	@UseDataGen(void.class)
	public static final SpotlightBlock SPOTLIGHT = BigTechBlocks.register(
		"spotlight",
		new SpotlightBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor((BlockState state) -> {
				return state.get(Properties.FACING) == Direction.UP ? MapColor.PALE_YELLOW : MapColor.BLACK;
			})
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);
	@UseDataGen(void.class)
	public static final ShortRangeDestroyerBlock SHORT_RANGE_DESTROYER = BigTechBlocks.register(
		"short_range_destroyer",
		new ShortRangeDestroyerBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);
	@UseDataGen(void.class)
	public static final LongRangeDestroyerBlock LONG_RANGE_DESTROYER = BigTechBlocks.register(
		"long_range_destroyer",
		new LongRangeDestroyerBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(2.0F, 6.0F)
		)
	);
	@UseDataGen(void.class)
	public static final IgnitorBeamBlock IGNITOR_BEAM = BigTechBlocks.register(
		"ignitor_beam",
		new IgnitorBeamBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(3.0F)
		)
	);
	@UseDataGen(void.class)
	public static final MirrorBlock MIRROR = BigTechBlocks.register(
		"mirror",
		new MirrorBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.IRON_GRAY)
			.nonOpaque()
			.strength(0.6F)
			.requiresTool()
		)
	);
	@UseDataGen(void.class)
	public static final PrismBlock PRISM = BigTechBlocks.register(
		"prism",
		new PrismBlock(
			AbstractBlock.Settings.copy(Blocks.GLASS)
		)
	);
	@UseDataGen(void.class)
	public static final PhaseManipulatorBlock PHASE_SCRAMBLER = BigTechBlocks.register(
		"phase_scrambler",
		new PhaseManipulatorBlock(
			AbstractBlock
			.Settings
			.copy(Blocks.BLACK_STAINED_GLASS)
			.allowsSpawning(Blocks::never)
			.solidBlock(Blocks::never)
			.suffocates(Blocks::never)
			.blockVision(Blocks::never)
			.sounds(BlockSoundGroup.STONE),
			false
		)
	);
	@UseDataGen(void.class)
	public static final PhaseManipulatorBlock PHASE_ALIGNER = BigTechBlocks.register(
		"phase_aligner",
		new PhaseManipulatorBlock(
			AbstractBlock
			.Settings
			.copy(Blocks.WHITE_STAINED_GLASS)
			.allowsSpawning(Blocks::never)
			.solidBlock(Blocks::never)
			.suffocates(Blocks::never)
			.blockVision(Blocks::never)
			.sounds(BlockSoundGroup.STONE),
			true
		)
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(void.class)
	public static final MagnetiteBlock MAGNETITE_BLOCK = BigTechBlocks.register(
		"magnetite_block",
		new MagnetiteBlock(
			AbstractBlock
			.Settings
			.copy(Blocks.IRON_BLOCK)
			.mapColor(MapColor.BRIGHT_RED)
		)
	);
	@UseDataGen(void.class)
	public static final WeightedPressurePlateBlock MEDIUM_WEIGHTED_PRESSURE_PLATE = BigTechBlocks.register(
		"medium_weighted_pressure_plate",
		new WeightedPressurePlateBlock(
			60,
			BigTechBlocks.COPPER_BLOCK_SET_TYPE,
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.ORANGE)
			.solid()
			.requiresTool()
			.noCollision()
			.strength(0.5F)
			.pistonBehavior(PistonBehavior.DESTROY)
		)
	);
	@UseDataGen(void.class)
	public static final StoneCraftingTableBlock STONE_CRAFTING_TABLE = BigTechBlocks.register(
		"stone_crafting_table",
		new StoneCraftingTableBlock(
			AbstractBlock.Settings.copy(Blocks.COBBLESTONE)
		)
	);

	public static void init() {}
}