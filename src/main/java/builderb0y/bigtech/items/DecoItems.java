package builderb0y.bigtech.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.*;
import builderb0y.bigtech.datagen.impl.catwalkPlatforms.CopperCatwalkPlatformDataGenerator;
import builderb0y.bigtech.datagen.impl.catwalkPlatforms.IronCatwalkPlatformDataGenerator;
import builderb0y.bigtech.datagen.impl.catwalkPlatforms.WoodCatwalkPlatformDataGenerator;
import builderb0y.bigtech.datagen.impl.catwalkStairs.CopperCatwalkStairsDataGenerator;
import builderb0y.bigtech.datagen.impl.catwalkStairs.IronCatwalkStairsDataGenerator;
import builderb0y.bigtech.datagen.impl.catwalkStairs.WoodCatwalkStairsDataGenerator;
import builderb0y.bigtech.datagen.impl.frames.CopperFrameDataGenerator;
import builderb0y.bigtech.datagen.impl.frames.GoldFrameDataGenerator;
import builderb0y.bigtech.datagen.impl.frames.IronFrameDataGenerator;
import builderb0y.bigtech.datagen.impl.frames.WoodenFrameDataGenerator;
import builderb0y.bigtech.datagen.impl.metalLadders.CopperLadderDataGenerator;
import builderb0y.bigtech.datagen.impl.metalLadders.IronLadderDataGenerator;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;
import builderb0y.bigtech.registrableCollections.LedRegistrableCollection.LedColor;
import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

import static builderb0y.bigtech.items.BigTechItems.register;
import static builderb0y.bigtech.items.BigTechItems.registerPlacer;
import static builderb0y.bigtech.items.BigTechItems.settings;

public class DecoItems {

	//////////////////////////////// frames ////////////////////////////////

	@UseDataGen(IronFrameDataGenerator.class)
	public static final BlockItem IRON_FRAME = registerPlacer(
		DecoBlocks.IRON_FRAME
	);
	@UseDataGen(GoldFrameDataGenerator.class)
	public static final BlockItem GOLD_FRAME = registerPlacer(
		DecoBlocks.GOLD_FRAME
	);
	@UseDataGen(CopperFrameDataGenerator.class)
	public static final CopperItemCollection COPPER_FRAMES = new CopperItemCollection(
		true,
		(CopperRegistrableCollection.Type type) -> new BlockItem(
			DecoBlocks.COPPER_FRAMES.get(type),
			settings(type.copperPrefix + "frame")
		)
	);
	@UseDataGen(WoodenFrameDataGenerator.class)
	public static final WoodItemCollection WOOD_FRAMES = new WoodItemCollection(
		true,
		(WoodRegistrableCollection.Type type) -> new BlockItem(
			DecoBlocks.WOOD_FRAMES.get(type),
			settings(type.prefix + "frame")
		)
	);

	//////////////////////////////// catwalk platforms ////////////////////////////////

	@UseDataGen(IronCatwalkPlatformDataGenerator.class)
	public static final BlockItem IRON_CATWALK_PLATFORM = registerPlacer(
		DecoBlocks.IRON_CATWALK_PLATFORM
	);
	@UseDataGen(CopperCatwalkPlatformDataGenerator.class)
	public static final CopperItemCollection COPPER_CATWALK_PLATFORMS = new CopperItemCollection(
		true,
		(CopperRegistrableCollection.Type type) -> new BlockItem(
			DecoBlocks.COPPER_CATWALK_PLATFORMS.get(type),
			settings(type.copperPrefix + "catwalk_platform")
		)
	);
	@UseDataGen(WoodCatwalkPlatformDataGenerator.class)
	public static final WoodItemCollection WOOD_CATWALK_PLATFORMS = new WoodItemCollection(
		true,
		(WoodRegistrableCollection.Type type) -> new BlockItem(
			DecoBlocks.WOOD_CATWALK_PLATFORMS.get(type),
			settings(type.prefix + "catwalk_platform")
		)
	);

	//////////////////////////////// catwalk stairs ////////////////////////////////

	@UseDataGen(IronCatwalkStairsDataGenerator.class)
	public static final CatwalkStairsBlockItem IRON_CATWALK_STAIRS = register(
		new CatwalkStairsBlockItem(
			DecoBlocks.IRON_CATWALK_STAIRS,
			settings("iron_catwalk_stairs")
		)
	);
	@UseDataGen(CopperCatwalkStairsDataGenerator.class)
	public static final CopperItemCollection COPPER_CATWALK_STAIRS = new CopperItemCollection(
		true,
		(CopperRegistrableCollection.Type type) -> new CatwalkStairsBlockItem(
			DecoBlocks.COPPER_CATWALK_STAIRS.get(type),
			settings(type.copperPrefix + "catwalk_stairs")
		)
	);
	@UseDataGen(WoodCatwalkStairsDataGenerator.class)
	public static final WoodItemCollection WOOD_CATWALK_STAIRS = new WoodItemCollection(
		true,
		(WoodRegistrableCollection.Type type) -> new CatwalkStairsBlockItem(
			DecoBlocks.WOOD_CATWALK_STAIRS.get(type),
			settings(type.prefix + "catwalk_stairs")
		)
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(IronLadderDataGenerator.class)
	public static final BlockItem IRON_LADDER = registerPlacer(
		DecoBlocks.IRON_LADDER
	);
	@UseDataGen(CopperLadderDataGenerator.class)
	public static final CopperItemCollection COPPER_LADDERS = new CopperItemCollection(
		true,
		(CopperRegistrableCollection.Type type) -> new BlockItem(
			DecoBlocks.COPPER_LADDERS.get(type),
			settings(type.copperPrefix + "ladder")
		)
	);
	@UseDataGen(CopperBarsDataGenerator.class)
	public static final CopperItemCollection COPPER_BARS = new CopperItemCollection(
		true,
		(CopperRegistrableCollection.Type type) -> new BlockItem(
			DecoBlocks.COPPER_BARS.get(type),
			settings(type.copperPrefix + "bars")
		)
	);
	@UseDataGen(CopperSlabDataGenerator.class)
	public static final CopperItemCollection COPPER_SLABS = new CopperItemCollection(
		true,
		(CopperRegistrableCollection.Type type) -> new BlockItem(
			DecoBlocks.COPPER_SLABS.get(type),
			settings(type.copperPrefix + "slab")
		)
	);
	@UseDataGen(SmoothObsidianDataGenerator.class)
	public static final BlockItem SMOOTH_OBSIDIAN = registerPlacer(
		DecoBlocks.SMOOTH_OBSIDIAN
	);
	@UseDataGen(LedDataGenerator.class)
	public static final LedItemCollection LEDS = new LedItemCollection(
		true,
		(LedColor color) -> new BlockItem(
			DecoBlocks.LEDS.get(color),
			settings(color.prefix + "led")
		)
	);
	@UseDataGen(CrystalLampDataGenerator.class)
	public static final CrystalItemCollection CRYSTAL_LAMPS = new CrystalItemCollection(
		true,
		(CrystalColor color) -> new BlockItem(
			DecoBlocks.CRYSTAL_LAMPS.get(color),
			settings(color.prefix + "crystal_lamp")
		)
	);

	public static void init() {}
}