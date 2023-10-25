package builderb0y.bigtech.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Oxidizable.OxidationLevel;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.belts.*;
import builderb0y.bigtech.datagen.base.UseDataGen;

public class BigTechBlocks {

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
	public static final OxidizableFrameBlock
		COPPER_FRAME = register(
			"copper_frame",
			new OxidizableFrameBlock(
				AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),
				BigTechBlockTags.STICKS_TO_COPPER_FRAME,
				OxidationLevel.UNAFFECTED
			)
		),
		EXPOSED_COPPER_FRAME = register(
			"exposed_copper_frame",
			new OxidizableFrameBlock(
				AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),
				BigTechBlockTags.STICKS_TO_COPPER_FRAME,
				OxidationLevel.EXPOSED
			)
		),
		WEATHERED_COPPER_FRAME = register(
			"weathered_copper_frame",
			new OxidizableFrameBlock(
				AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),
				BigTechBlockTags.STICKS_TO_COPPER_FRAME,
				OxidationLevel.WEATHERED
			)
		),
		OXIDIZED_COPPER_FRAME = register(
			"oxidized_copper_frame",
			new OxidizableFrameBlock(
				AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),
				BigTechBlockTags.STICKS_TO_COPPER_FRAME,
				OxidationLevel.OXIDIZED
			)
		);
	@UseDataGen(void.class)
	public static final FrameBlock
		WAXED_COPPER_FRAME = register(
			"waxed_copper_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),
				BigTechBlockTags.STICKS_TO_COPPER_FRAME
			)
		),
		WAXED_EXPOSED_COPPER_FRAME = register(
			"waxed_exposed_copper_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),
				BigTechBlockTags.STICKS_TO_COPPER_FRAME
			)
		),
		WAXED_WEATHERED_COPPER_FRAME = register(
			"waxed_weathered_copper_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),
				BigTechBlockTags.STICKS_TO_COPPER_FRAME
			)
		),
		WAXED_OXIDIZED_COPPER_FRAME = register(
			"waxed_oxidized_copper_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),
				BigTechBlockTags.STICKS_TO_COPPER_FRAME
			)
		);
	@UseDataGen(void.class)
	public static final FrameBlock
		OAK_FRAME = register(
			"oak_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.OAK_PLANKS),
				BigTechBlockTags.STICKS_TO_OAK_FRAME
			)
		),
		SPRUCE_FRAME = register(
			"spruce_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.SPRUCE_PLANKS),
				BigTechBlockTags.STICKS_TO_SPRUCE_FRAME
			)
		),
		BIRCH_FRAME = register(
			"birch_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.BIRCH_PLANKS),
				BigTechBlockTags.STICKS_TO_BIRCH_FRAME
			)
		),
		JUNGLE_FRAME = register(
			"jungle_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.JUNGLE_PLANKS),
				BigTechBlockTags.STICKS_TO_JUNGLE_FRAME
			)
		),
		ACACIA_FRAME = register(
			"acacia_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS),
				BigTechBlockTags.STICKS_TO_ACACIA_FRAME
			)
		),
		DARK_OAK_FRAME = register(
			"dark_oak_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.DARK_OAK_PLANKS),
				BigTechBlockTags.STICKS_TO_DARK_OAK_FRAME
			)
		),
		CHERRY_FRAME = register(
			"cherry_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.CHERRY_PLANKS),
				BigTechBlockTags.STICKS_TO_CHERRY_FRAME
			)
		),
		MANGROVE_FRAME = register(
			"mangrove_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.MANGROVE_PLANKS),
				BigTechBlockTags.STICKS_TO_MANGROVE_FRAME
			)
		),
		CRIMSON_FRAME = register(
			"crimson_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.CRIMSON_PLANKS),
				BigTechBlockTags.STICKS_TO_CRIMSON_FRAME
			)
		),
		WARPED_FRAME = register(
			"warped_frame",
			new FrameBlock(
				AbstractBlock.Settings.copy(Blocks.WARPED_PLANKS),
				BigTechBlockTags.STICKS_TO_WARPED_FRAME
			)
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

		OxidizableBlocksRegistry.registerOxidizableBlockPair(          COPPER_FRAME,   EXPOSED_COPPER_FRAME);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(  EXPOSED_COPPER_FRAME, WEATHERED_COPPER_FRAME);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_COPPER_FRAME,  OXIDIZED_COPPER_FRAME);

		OxidizableBlocksRegistry.registerWaxableBlockPair(          COPPER_FRAME,           WAXED_COPPER_FRAME);
		OxidizableBlocksRegistry.registerWaxableBlockPair(  EXPOSED_COPPER_FRAME,   WAXED_EXPOSED_COPPER_FRAME);
		OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_COPPER_FRAME, WAXED_WEATHERED_COPPER_FRAME);
		OxidizableBlocksRegistry.registerWaxableBlockPair( OXIDIZED_COPPER_FRAME,  WAXED_OXIDIZED_COPPER_FRAME);

		FlammableBlockRegistry.getDefaultInstance().add(     OAK_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(  SPRUCE_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(   BIRCH_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(  JUNGLE_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(  ACACIA_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(DARK_OAK_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(  CHERRY_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(MANGROVE_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add( CRIMSON_FRAME, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(  WARPED_FRAME, 5, 20);
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(
			RenderLayer.cutout,
			TRAPDOOR_BELT,
			ASCENDER,
			DESCENDER,
			IRON_FRAME,
			COPPER_FRAME,
			GOLD_FRAME,
			EXPOSED_COPPER_FRAME,
			WEATHERED_COPPER_FRAME,
			OXIDIZED_COPPER_FRAME,
			WAXED_COPPER_FRAME,
			WAXED_EXPOSED_COPPER_FRAME,
			WAXED_WEATHERED_COPPER_FRAME,
			WAXED_OXIDIZED_COPPER_FRAME,
			OAK_FRAME,
			SPRUCE_FRAME,
			BIRCH_FRAME,
			JUNGLE_FRAME,
			ACACIA_FRAME,
			DARK_OAK_FRAME,
			CHERRY_FRAME,
			MANGROVE_FRAME,
			CRIMSON_FRAME,
			WARPED_FRAME,
			TRANSMUTER
		);
	}

	public static <B extends Block> B register(String name, B block) {
		return Registry.register(Registries.BLOCK, BigTechMod.modID(name), block);
	}
}