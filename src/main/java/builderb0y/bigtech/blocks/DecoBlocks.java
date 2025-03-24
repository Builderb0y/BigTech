package builderb0y.bigtech.blocks;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;

import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;
import builderb0y.bigtech.registrableCollections.LedRegistrableCollection.LedColor;
import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

import static builderb0y.bigtech.blocks.BigTechBlocks.register;
import static builderb0y.bigtech.blocks.BigTechBlocks.settings;
import static builderb0y.bigtech.blocks.BigTechBlocks.copySettings;

public class DecoBlocks {

	//////////////////////////////// frames ////////////////////////////////

	@UseDataGen(void.class)
	public static final FrameBlock IRON_FRAME = register(
		new FrameBlock(
			copySettings(Blocks.IRON_BLOCK, "iron_frame"),
			BigTechBlockTags.STICKS_TO_IRON_FRAME
		)
	);
	@UseDataGen(void.class)
	public static final FrameBlock STEEL_FRAME = register(
		new FrameBlock(
			copySettings(Blocks.IRON_BLOCK, "steel_frame")
			.mapColor(DyeColor.GRAY),
			BigTechBlockTags.STICKS_TO_STEEL_FRAME
		)
	);
	@UseDataGen(void.class)
	public static final FrameBlock GOLD_FRAME = register(
		new FrameBlock(
			copySettings(Blocks.GOLD_BLOCK, "gold_frame"),
			BigTechBlockTags.STICKS_TO_GOLD_FRAME
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_FRAMES = new CopperBlockCollection(
		true,
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = copySettings(
				BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type),
				type.copperPrefix + "frame"
			);
			return (
				type.waxed
				? new FrameBlock(settings, BigTechBlockTags.STICKS_TO_COPPER_FRAME)
				: new OxidizableFrameBlock(settings, BigTechBlockTags.STICKS_TO_COPPER_FRAME, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final WoodBlockCollection WOOD_FRAMES = new WoodBlockCollection(
		true,
		(WoodRegistrableCollection.Type type) -> new FrameBlock(
			copySettings(
				BigTechBlocks.VANILLA_PLANKS.get(type),
				type.prefix + "frame"
			),
			switch (type) {
				case OAK      -> BigTechBlockTags.STICKS_TO_OAK_FRAME;
				case SPRUCE   -> BigTechBlockTags.STICKS_TO_SPRUCE_FRAME;
				case BIRCH    -> BigTechBlockTags.STICKS_TO_BIRCH_FRAME;
				case JUNGLE   -> BigTechBlockTags.STICKS_TO_JUNGLE_FRAME;
				case ACACIA   -> BigTechBlockTags.STICKS_TO_ACACIA_FRAME;
				case DARK_OAK -> BigTechBlockTags.STICKS_TO_DARK_OAK_FRAME;
				case MANGROVE -> BigTechBlockTags.STICKS_TO_MANGROVE_FRAME;
				case CHERRY   -> BigTechBlockTags.STICKS_TO_CHERRY_FRAME;
				case PALE_OAK -> BigTechBlockTags.STICKS_TO_PALE_OAK_FRAME;
				case CRIMSON  -> BigTechBlockTags.STICKS_TO_CRIMSON_FRAME;
				case WARPED   -> BigTechBlockTags.STICKS_TO_WARPED_FRAME;
			}
		)
	);

	//////////////////////////////// catwalk platforms ////////////////////////////////

	@UseDataGen(void.class)
	public static final CatwalkPlatformBlock IRON_CATWALK_PLATFORM = register(
		new CatwalkPlatformBlock(
			copySettings(Blocks.IRON_BLOCK, "iron_catwalk_platform")
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_CATWALK_PLATFORMS = new CopperBlockCollection(
		true,
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = copySettings(
				BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type),
				type.copperPrefix + "catwalk_platform"
			);
			return (
				type.waxed
				? new CatwalkPlatformBlock(settings)
				: new OxidizableCatwalkPlatformBlock(settings, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final CatwalkPlatformBlock STEEL_CATWALK_PLATFORM = register(
		new CatwalkPlatformBlock(
			copySettings(Blocks.IRON_BLOCK, "steel_catwalk_platform")
			.mapColor(DyeColor.GRAY)
		)
	);
	@UseDataGen(void.class)
	public static final WoodBlockCollection WOOD_CATWALK_PLATFORMS = new WoodBlockCollection(
		true,
		(WoodRegistrableCollection.Type type) -> new CatwalkPlatformBlock(
			copySettings(
				BigTechBlocks.VANILLA_PLANKS.get(type),
				type.prefix + "catwalk_platform"
			)
		)
	);

	//////////////////////////////// catwalk stairs ////////////////////////////////

	@UseDataGen(void.class)
	public static final CatwalkStairsBlock IRON_CATWALK_STAIRS = register(
		new CatwalkStairsBlock(
			copySettings(Blocks.IRON_BLOCK, "iron_catwalk_stairs")
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_CATWALK_STAIRS = new CopperBlockCollection(
		true,
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = copySettings(
				BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type),
				type.copperPrefix + "catwalk_stairs"
			);
			return (
				type.waxed
				? new CopperCatwalkStairsBlock(settings)
				: new OxidizableCatwalkStairsBlock(settings, type.level)
			);
		}
	);
	@UseDataGen(void.class)
	public static final CatwalkStairsBlock STEEL_CATWALK_STAIRS = register(
		new CatwalkStairsBlock(
			copySettings(Blocks.IRON_BLOCK, "steel_catwalk_stairs")
			.mapColor(DyeColor.GRAY)
		)
	);
	@UseDataGen(void.class)
	public static final WoodBlockCollection WOOD_CATWALK_STAIRS = new WoodBlockCollection(
		true,
		(WoodRegistrableCollection.Type type) -> new CatwalkStairsBlock(
			copySettings(
				BigTechBlocks.VANILLA_PLANKS.get(type),
				type.prefix + "catwalk_stairs"
			)
		)
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(void.class)
	public static final LadderBlock IRON_LADDER = register(
		new LadderBlock(
			copySettings(Blocks.IRON_BLOCK, "iron_ladder")
			.strength(1.0F)
			.notSolid()
			.nonOpaque()
			.pistonBehavior(PistonBehavior.DESTROY)
		)
	);
	@UseDataGen(void.class)
	public static final LadderBlock STEEL_LADDER = register(
		new LadderBlock(
			copySettings(IRON_LADDER, "steel_ladder")
			.mapColor(MapColor.GRAY)
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_LADDERS = new CopperBlockCollection(
		true,
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = (
				copySettings(
					BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type),
					type.copperPrefix + "ladder"
				)
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
		true,
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = (
				settings(type.copperPrefix + "bars")
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
	public static final PaneBlock STEEL_BARS = register(
		new PaneBlock(
			copySettings(Blocks.IRON_BARS, "steel_bars")
			.mapColor(DyeColor.GRAY)
		)
	);
	@UseDataGen(void.class)
	public static final CopperBlockCollection COPPER_SLABS = new CopperBlockCollection(
		true,
		(CopperRegistrableCollection.Type type) -> {
			AbstractBlock.Settings settings = copySettings(
				BigTechBlocks.VANILLA_COPPER_BLOCKS.get(type),
				type.copperPrefix + "slab"
			);
			return (
				type.waxed
				? new SlabBlock(settings)
				: new OxidizableSlabBlock(type.level, settings)
			);
		}
	);
	@UseDataGen(void.class)
	public static final Block SMOOTH_OBSIDIAN = register(
		new Block(
			copySettings(Blocks.OBSIDIAN, "smooth_obsidian")
			.hardness(20.0F)
			//did you know that pistons hard-code the check for
			//obsidian instead of using the piston behavior on the block?
			//in fact, obsidian doesn't even specify a piston behavior at all,
			//so copying its properties won't automatically make it unpushable.
			.pistonBehavior(PistonBehavior.BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final Block STEEL_BLOCK = register(
		new Block(
			copySettings(Blocks.IRON_BLOCK, "steel_block")
			.mapColor(MapColor.GRAY)
		)
	);
	@UseDataGen(void.class)
	public static final LedBlockCollection LEDS = new LedBlockCollection(
		true,
		(LedColor color) -> new LedBlock(
			settings(color.prefix + "led")
			.mapColor(color.dyeColor())
			.pistonBehavior(PistonBehavior.DESTROY)
			.strength(0.2F, 0.0F)
			.noCollision()
		)
	);
	@UseDataGen(void.class)
	public static final CrystalBlockCollection CRYSTAL_LAMPS = new CrystalBlockCollection(
		true,
		(CrystalColor color) -> {
			return new CrystalLampBlock(
				settings(color.prefix + "crystal_lamp")
				.mapColor(color.closestDyeColor)
				.strength(0.5F)
				.sounds(BlockSoundGroup.GLASS)
				.luminance((BlockState state) -> state.get(Properties.LIT) ? 15 : 0)
			);
		}
	);

	public static void init() {}
}