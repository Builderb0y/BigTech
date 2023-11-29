package builderb0y.bigtech.blocks;

import java.util.stream.Stream;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.blockEntities.BeamInterceptorBlockEntity;
import builderb0y.bigtech.blocks.belts.*;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.registrableCollections.RegistrableCollection;
import builderb0y.bigtech.util.WorldHelper;

public class BigTechBlocks {

	public static final BlockSetType COPPER_BLOCK_SET_TYPE = (
		BlockSetTypeBuilder
		.copyOf(BlockSetType.IRON)
		.soundGroup(BlockSoundGroup.COPPER)
		.build(BigTechMod.modID("copper"))
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection VANILLA_COPPER_BLOCKS = new CopperBlockCollection(
		null,
		Blocks.COPPER_BLOCK,
		Blocks.EXPOSED_COPPER,
		Blocks.WEATHERED_COPPER,
		Blocks.OXIDIZED_COPPER,
		Blocks.WAXED_COPPER_BLOCK,
		Blocks.WAXED_EXPOSED_COPPER,
		Blocks.WAXED_WEATHERED_COPPER,
		Blocks.WAXED_OXIDIZED_COPPER
	);
	@UseDataGen(void.class)
	public static final WoodBlockCollection VANILLA_PLANKS = new WoodBlockCollection(
		null,
		Blocks.OAK_PLANKS,
		Blocks.SPRUCE_PLANKS,
		Blocks.BIRCH_PLANKS,
		Blocks.JUNGLE_PLANKS,
		Blocks.ACACIA_PLANKS,
		Blocks.DARK_OAK_PLANKS,
		Blocks.MANGROVE_PLANKS,
		Blocks.CHERRY_PLANKS,
		Blocks.CRIMSON_PLANKS,
		Blocks.WARPED_PLANKS
	);

	@UseDataGen(void.class)
	public static final DirectionalBeltBlock BELT = register(
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
	public static final SpeedyBeltBlock SPEEDY_BELT = register(
		"speedy_belt",
		new SpeedyBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final BrakeBeltBlock BRAKE_BELT = register(
		"brake_belt",
		new BrakeBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final TrapdoorBeltBlock TRAPDOOR_BELT = register(
		"trapdoor_belt",
		new TrapdoorBeltBlock(
			AbstractBlock.Settings.copy(BELT).nonOpaque()
		)
	);
	@UseDataGen(void.class)
	public static final DirectorBeltBlock DIRECTOR_BELT = register(
		"director_belt",
		new DirectorBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final DetectorBeltBlock DETECTOR_BELT = register(
		"detector_belt",
		new DetectorBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final SorterBeltBlock SORTER_BELT = register(
		"sorter_belt",
		new SorterBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final EjectorBeltBlock EJECTOR_BELT = register(
		"ejector_belt",
		new EjectorBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final InjectorBeltBlock INJECTOR_BELT = register(
		"injector_belt",
		new InjectorBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final LauncherBeltBlock LAUNCHER_BELT = register(
		"launcher_belt",
		new LauncherBeltBlock(
			AbstractBlock.Settings.copy(BELT)
		)
	);
	@UseDataGen(void.class)
	public static final AscenderBlock ASCENDER = register(
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
	public static final AscenderBlock DESCENDER = register(
		"descender",
		new AscenderBlock(
			AbstractBlock.Settings.copy(ASCENDER),
			Direction.DOWN
		)
	);
	@UseDataGen(void.class)
	public static final FrameBlock IRON_FRAME = register(
		"iron_frame",
		new FrameBlock(
			AbstractBlock.Settings.copy(Blocks.IRON_BLOCK),
			BigTechBlockTags.STICKS_TO_IRON_FRAME
		)
	);
	@UseDataGen(void.class)
	public static final FrameBlock GOLD_FRAME = register(
		"gold_frame",
		new FrameBlock(
			AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK),
			BigTechBlockTags.STICKS_TO_GOLD_FRAME
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_FRAMES = new CopperBlockCollection(
		"frame",
		type -> {
			AbstractBlock.Settings settings = AbstractBlock.Settings.copy(VANILLA_COPPER_BLOCKS.get(type));
			return (
				type.waxed
				? new FrameBlock(settings, BigTechBlockTags.STICKS_TO_COPPER_FRAME)
				: new OxidizableFrameBlock(settings, BigTechBlockTags.STICKS_TO_COPPER_FRAME, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final WoodBlockCollection WOOD_FRAMES = new WoodBlockCollection(
		"frame",
		type -> new FrameBlock(
			AbstractBlock.Settings.copy(VANILLA_PLANKS.get(type)),
			switch (type) {
				case OAK      -> BigTechBlockTags.STICKS_TO_OAK_FRAME;
				case SPRUCE   -> BigTechBlockTags.STICKS_TO_SPRUCE_FRAME;
				case BIRCH    -> BigTechBlockTags.STICKS_TO_BIRCH_FRAME;
				case JUNGLE   -> BigTechBlockTags.STICKS_TO_JUNGLE_FRAME;
				case ACACIA   -> BigTechBlockTags.STICKS_TO_ACACIA_FRAME;
				case DARK_OAK -> BigTechBlockTags.STICKS_TO_DARK_OAK_FRAME;
				case MANGROVE -> BigTechBlockTags.STICKS_TO_MANGROVE_FRAME;
				case CHERRY   -> BigTechBlockTags.STICKS_TO_CHERRY_FRAME;
				case CRIMSON  -> BigTechBlockTags.STICKS_TO_CRIMSON_FRAME;
				case WARPED   -> BigTechBlockTags.STICKS_TO_WARPED_FRAME;
			}
		)
	);
	@UseDataGen(void.class)
	public static final LadderBlock IRON_LADDER = register(
		"iron_ladder",
		new LadderBlock(
			AbstractBlock
			.Settings
			.copy(Blocks.IRON_BLOCK)
			.strength(1.0F)
			.notSolid()
			.nonOpaque()
			.pistonBehavior(PistonBehavior.DESTROY)
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_LADDERS = new CopperBlockCollection(
		"ladder",
		type -> {
			AbstractBlock.Settings settings = (
				AbstractBlock
				.Settings
				.copy(VANILLA_COPPER_BLOCKS.get(type))
				.strength(0.6F, 1.2F)
				.notSolid()
				.nonOpaque()
				.pistonBehavior(PistonBehavior.DESTROY)
			);
			return (
				type.waxed
				? new LadderBlock(settings)
				: new OxidizableLaderBlock(settings, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_SLABS = new CopperBlockCollection(
		"slab",
		type -> {
			AbstractBlock.Settings settings = AbstractBlock.Settings.copy(VANILLA_COPPER_BLOCKS.get(type));
			return (
				type.waxed
				? new SlabBlock(settings)
				: new OxidizableSlabBlock(type.level, settings)
			);
		}
	);
	@UseDataGen(void.class)
	public static final EncasedRedstoneBlock ENCASED_REDSTONE_BLOCK = register(
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
	public static final EncasedSlimeBlock
		ENCASED_SLIME_BLOCK = register(
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
		),
		ENCASED_HONEY_BLOCK = register(
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
	@UseDataGen(void.class)
	public static final LightningCableBlock
		IRON_LIGHTNING_CABLE = register(
			"iron_lightning_cable",
			new LightningCableBlock(
				AbstractBlock
				.Settings
				.create()
				.mapColor(MapColor.GRAY)
				.strength(0.8F)
				.sounds(BlockSoundGroup.WOOL)
			)
		),
		GOLD_LIGHTNING_CABLE = register(
			"gold_lightning_cable",
			new LightningCableBlock(
				AbstractBlock.Settings.copy(IRON_LIGHTNING_CABLE)
			)
		),
		COPPER_LIGHTNING_CABLE = register(
			"copper_lightning_cable",
			new LightningCableBlock(
				AbstractBlock.Settings.copy(IRON_LIGHTNING_CABLE)
			)
		);
	@UseDataGen(void.class)
	public static final SmallLightningJarBlock SMALL_LIGHTNING_JAR = register(
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
	public static final LargeLightningJarBlock LARGE_LIGHTNING_JAR = register(
		"large_lightning_jar",
		new LargeLightningJarBlock(
			AbstractBlock.Settings.copy(SMALL_LIGHTNING_JAR)
		)
	);
	@UseDataGen(void.class)
	public static final TransmuterBlock TRANSMUTER = register(
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
	public static final MagnetiteBlock MAGNETITE_BLOCK = register(
		"magnetite_block",
		new MagnetiteBlock(
			AbstractBlock
			.Settings
			.copy(Blocks.IRON_BLOCK)
			.mapColor(MapColor.BRIGHT_RED)
		)
	);
	@UseDataGen(void.class)
	public static final CatwalkPlatformBlock IRON_CATWALK_PLATFORM = register(
		"iron_catwalk_platform",
		new CatwalkPlatformBlock(
			AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_CATWALK_PLATFORMS = new CopperBlockCollection(
		"catwalk_platform",
		type -> {
			AbstractBlock.Settings settings = AbstractBlock.Settings.copy(VANILLA_COPPER_BLOCKS.get(type));
			return (
				type.waxed
				? new CatwalkPlatformBlock(settings)
				: new OxidizableCatwalkPlatformBlock(settings, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final WoodBlockCollection WOOD_CATWALK_PLATFORMS = new WoodBlockCollection(
		"catwalk_platform",
		type -> new CatwalkPlatformBlock(
			AbstractBlock.Settings.copy(VANILLA_PLANKS.get(type))
		)
	);
	@UseDataGen(void.class)
	public static final CatwalkStairsBlock IRON_CATWALK_STAIRS = register(
		"iron_catwalk_stairs",
		new CatwalkStairsBlock(
			AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_CATWALK_STAIRS = new CopperBlockCollection(
		"catwalk_stairs",
		type -> {
			AbstractBlock.Settings settings = AbstractBlock.Settings.copy(VANILLA_COPPER_BLOCKS.get(type));
			return (
				type.waxed
				? new CopperCatwalkStairsBlock(settings)
				: new OxidizableCatwalkStairsBlock(settings, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final WoodBlockCollection WOOD_CATWALK_STAIRS = new WoodBlockCollection(
		"catwalk_stairs",
		type -> new CatwalkStairsBlock(
			AbstractBlock.Settings.copy(VANILLA_PLANKS.get(type))
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_BARS = new CopperBlockCollection(
		"bars",
		type -> {
			AbstractBlock.Settings settings = (
				AbstractBlock
				.Settings
				.create()
				.mapColor(switch (type.level) {
					case UNAFFECTED -> MapColor.ORANGE;
					case EXPOSED -> MapColor.TERRACOTTA_LIGHT_GRAY;
					case WEATHERED -> MapColor.DARK_AQUA;
					case OXIDIZED -> MapColor.TEAL;
				})
				.requiresTool()
				.strength(4.0F, 6.0F)
				.sounds(BlockSoundGroup.COPPER)
				.nonOpaque()
			);
			return (
				type.waxed
				? new PaneBlock(settings)
				: new OxidizablePaneBlock(settings, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final WeightedPressurePlateBlock MEDIUM_WEIGHTED_PRESSURE_PLATE = register(
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
			COPPER_BLOCK_SET_TYPE
		)
	);
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
	public static final CrystallineSandBlock CRYSTALLINE_SAND = register(
		"crystalline_sand",
		new CrystallineSandBlock(
			AbstractBlock.Settings.copy(Blocks.SAND)
		)
	);
	@UseDataGen(void.class)
	public static final RedstoneTransmitterBlock REDSTONE_TRANSMITTER = register(
		"redstone_transmitter",
		new RedstoneTransmitterBlock(
			AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final RedstoneReceiverBlock REDSTONE_RECEIVER = register(
		"redstone_receiver",
		new RedstoneReceiverBlock(
			AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final BeamInterceptorBlock BEAM_INTERCEPTOR = register(
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
	public static final MirrorBlock MIRROR = register(
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
	public static final PrismBlock PRISM = register(
		"prism",
		new PrismBlock(
			AbstractBlock.Settings.copy(Blocks.GLASS)
		)
	);
	@UseDataGen(void.class)
	public static final PhaseManipulatorBlock
		PHASE_SCRAMBLER = register(
			"phase_scrambler",
			new PhaseManipulatorBlock(
				AbstractBlock
				.Settings
				.copy(Blocks.BLACK_STAINED_GLASS)
				.sounds(BlockSoundGroup.STONE),
				false
			)
		),
		PHASE_ALIGNER = register(
			"phase_aligner",
			new PhaseManipulatorBlock(
				AbstractBlock
				.Settings
				.copy(Blocks.WHITE_STAINED_GLASS)
				.sounds(BlockSoundGroup.STONE),
				true
			)
		);
	@UseDataGen(void.class)
	public static final CopperCoilBlock COPPER_COIL = register(
		"copper_coil",
		new CopperCoilBlock(
			AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)
		)
	);

	public static void init() {
		LandPathNodeTypesRegistry.register(         BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(  SPEEDY_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(   BRAKE_BELT, (state, neighbor) -> !state.get(Properties.POWERED) && !neighbor ? PathNodeType.RAIL : null);
		LandPathNodeTypesRegistry.register(TRAPDOOR_BELT, (state, neighbor) -> state.get(Properties.POWERED) != state.get(Properties.INVERTED) && !neighbor ? PathNodeType.RAIL : null);
		LandPathNodeTypesRegistry.register(DIRECTOR_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(DETECTOR_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(  SORTER_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register( EJECTOR_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(INJECTOR_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(LAUNCHER_BELT, PathNodeType.RAIL, null);
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(
			RenderLayer.cutout,
			TRAPDOOR_BELT,
			ASCENDER,
			DESCENDER,
			IRON_FRAME,
			GOLD_FRAME,
			IRON_LADDER,
			TRANSMUTER,
			SMALL_LIGHTNING_JAR,
			LARGE_LIGHTNING_JAR,
			IRON_CATWALK_PLATFORM,
			IRON_CATWALK_STAIRS,
			BEAM_INTERCEPTOR
		);
		BlockRenderLayerMap.INSTANCE.putBlocks(
			RenderLayer.cutout,
			Stream.of(
				COPPER_FRAMES,
				WOOD_FRAMES,
				COPPER_LADDERS,
				COPPER_CATWALK_PLATFORMS,
				COPPER_CATWALK_STAIRS,
				COPPER_BARS,
				WOOD_CATWALK_PLATFORMS,
				WOOD_CATWALK_STAIRS
			)
			.flatMap(RegistrableCollection::stream)
			.toArray(Block[]::new)
		);
		BlockRenderLayerMap.INSTANCE.putBlocks(
			RenderLayer.getTranslucent(),
			PHASE_SCRAMBLER,
			PHASE_ALIGNER,
			PRISM
		);
		BlockRenderLayerMap.INSTANCE.putBlocks(
			RenderLayer.getTranslucent(),
			CRYSTAl_ClUSTERS
			.stream()
			.toArray(Block[]::new)
		);
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				BeamInterceptorBlockEntity blockEntity;
				if (tintIndex == 1 && world != null && pos != null && (blockEntity = WorldHelper.getBlockEntity(world, pos, BeamInterceptorBlockEntity.class)) != null && blockEntity.color != null) {
					return BeamSegment.packRgb(blockEntity.color) | 0xFF000000;
				}
				return -1;
			},
			BEAM_INTERCEPTOR
		);
	}

	public static <B extends Block> B register(String name, B block) {
		return Registry.register(Registries.BLOCK, BigTechMod.modID(name), block);
	}
}