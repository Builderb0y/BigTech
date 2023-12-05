package builderb0y.bigtech.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.blocks.belts.*;
import builderb0y.bigtech.datagen.base.UseDataGen;

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

	//////////////////////////////// encased blocks ////////////////////////////////

	@UseDataGen(void.class)
	public static final EncasedRedstoneBlock ENCASED_REDSTONE_BLOCK = BigTechBlocks.register(
		"encased_redstone_block",
		new EncasedRedstoneBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(state -> state.get(Properties.FACING) == Direction.UP ? MapColor.BRIGHT_RED : MapColor.STONE_GRAY)
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
			.mapColor(state -> state.get(Properties.FACING) == Direction.UP ? MapColor.PALE_GREEN : MapColor.STONE_GRAY)
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
			.mapColor(state -> state.get(Properties.FACING) == Direction.UP ? MapColor.ORANGE : MapColor.STONE_GRAY)
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

	//////////////////////////////// crystals ////////////////////////////////

	@UseDataGen(void.class)
	public static final CrystalClusterBlockCollection CRYSTAl_ClUSTERS = new CrystalClusterBlockCollection(
		true,
		color -> {
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
				.luminance(state -> 7),
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
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.ORANGE)
			.solid()
			.requiresTool()
			.noCollision()
			.strength(0.5F)
			.pistonBehavior(PistonBehavior.DESTROY),
			BigTechBlocks.COPPER_BLOCK_SET_TYPE
		)
	);

	public static void init() {}
}