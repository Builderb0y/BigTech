package builderb0y.bigtech.blocks;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;
import builderb0y.bigtech.registrableCollections.LedRegistrableCollection.LedColor;
import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

public class DecoBlocks {

	//////////////////////////////// frames ////////////////////////////////

	@UseDataGen(void.class)
	public static final FrameBlock IRON_FRAME = BigTechBlocks.register(
		"iron_frame",
		new FrameBlock(
			AbstractBlock.Settings.copy(Blocks.IRON_BLOCK),
			BigTechBlockTags.STICKS_TO_IRON_FRAME
		)
	);
	@UseDataGen(void.class)
	public static final FrameBlock GOLD_FRAME = BigTechBlocks.register(
		"gold_frame",
		new FrameBlock(
			AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK),
			BigTechBlockTags.STICKS_TO_GOLD_FRAME
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_FRAMES = new CopperBlockCollection(
		(CopperRegistrableCollection.Type type) -> type.copperPrefix + "frame",
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = AbstractBlock.Settings.copy(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type));
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
		(WoodRegistrableCollection.Type type) -> new FrameBlock(
			AbstractBlock.Settings.copy(BigTechBlocks.VANILLA_PLANKS.get(type)),
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

	//////////////////////////////// catwalk platforms ////////////////////////////////

	@UseDataGen(void.class)
	public static final CatwalkPlatformBlock IRON_CATWALK_PLATFORM = BigTechBlocks.register(
		"iron_catwalk_platform",
		new CatwalkPlatformBlock(
			AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_CATWALK_PLATFORMS = new CopperBlockCollection(
		(CopperRegistrableCollection.Type type) -> type.copperPrefix + "catwalk_platform",
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = AbstractBlock.Settings.copy(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type));
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
		(WoodRegistrableCollection.Type type) -> new CatwalkPlatformBlock(
			AbstractBlock.Settings.copy(BigTechBlocks.VANILLA_PLANKS.get(type))
		)
	);

	//////////////////////////////// catwalk stairs ////////////////////////////////

	@UseDataGen(void.class)
	public static final CatwalkStairsBlock IRON_CATWALK_STAIRS = BigTechBlocks.register(
		"iron_catwalk_stairs",
		new CatwalkStairsBlock(
			AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_CATWALK_STAIRS = new CopperBlockCollection(
		(CopperRegistrableCollection.Type type) -> type.copperPrefix + "catwalk_stairs",
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = AbstractBlock.Settings.copy(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type));
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
		(WoodRegistrableCollection.Type type) -> new CatwalkStairsBlock(
			AbstractBlock.Settings.copy(BigTechBlocks.VANILLA_PLANKS.get(type))
		)
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(void.class)
	public static final LadderBlock IRON_LADDER = BigTechBlocks.register(
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
		(CopperRegistrableCollection.Type type) -> type.copperPrefix + "ladder",
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = (
				AbstractBlock
				.Settings
				.copy(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type))
				.strength(0.6F, 1.2F)
				.notSolid()
				.nonOpaque()
				.pistonBehavior(PistonBehavior.DESTROY)
			);
			return (
				type.waxed
				? new LadderBlock(settings)
				: new OxidizableLadderBlock(settings, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_BARS = new CopperBlockCollection(
		(CopperRegistrableCollection.Type type) -> type.copperPrefix + "bars",
		(CopperRegistrableCollection.Type type) -> {
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
	public static final CopperBlockCollection COPPER_SLABS = new CopperBlockCollection(
		(CopperRegistrableCollection.Type type) -> type.copperPrefix + "slab",
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = AbstractBlock.Settings.copy(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type));
			return (
				type.waxed
				? new SlabBlock(settings)
				: new OxidizableSlabBlock(type.level, settings)
			);
		}
	);
	@UseDataGen(void.class)
	public static final Block SMOOTH_OBSIDIAN = BigTechBlocks.register(
		"smooth_obsidian",
		new Block(
			AbstractBlock
			.Settings
			.copy(Blocks.OBSIDIAN)
			.hardness(20.0F)
			//did you know that pistons hard-code the check for
			//obsidian instead of using the piston behavior on the block?
			//in fact, obsidian doesn't even specify a piston behavior at all,
			//so copying its properties won't automatically make it unpushable.
			.pistonBehavior(PistonBehavior.BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final LedBlockCollection LEDS = new LedBlockCollection(
		true,
		(LedColor color) -> new LedBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(color.dyeColor())
			.pistonBehavior(PistonBehavior.DESTROY)
			.strength(0.2F, 0.0F)
			.noCollision()
		)
	);
	@UseDataGen(void.class)
	public static final CrystalBlockCollection CRYSTAL_LAMPS = new CrystalBlockCollection(
		"crystal_lamp",
		(CrystalColor color) -> {
			return new CrystalLampBlock(
				AbstractBlock
				.Settings
				.create()
				.mapColor(color.closestDyeColor)
				.strength(0.5F)
				.sounds(BlockSoundGroup.GLASS)
				.luminance((BlockState state) -> state.get(Properties.LIT) ? 15 : 0)
			);
		}
	);

	public static void init() {}
}