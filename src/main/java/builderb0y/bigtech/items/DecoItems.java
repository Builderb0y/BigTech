package builderb0y.bigtech.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.CopperBarsDataGenerator;
import builderb0y.bigtech.datagen.impl.CopperSlabDataGenerator;
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

public class DecoItems {

	//////////////////////////////// frames ////////////////////////////////

	@UseDataGen(IronFrameDataGenerator.class)
	public static final BlockItem IRON_FRAME = BigTechItems.registerPlacer(
		DecoBlocks.IRON_FRAME
	);
	@UseDataGen(GoldFrameDataGenerator.class)
	public static final BlockItem GOLD_FRAME = BigTechItems.registerPlacer(
		DecoBlocks.GOLD_FRAME
	);
	@UseDataGen(CopperFrameDataGenerator.class)
	public static final CopperItemCollection COPPER_FRAMES = new CopperItemCollection(
		"frame",
		type -> new BlockItem(
			DecoBlocks.COPPER_FRAMES.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(WoodenFrameDataGenerator.class)
	public static final WoodItemCollection WOOD_FRAMES = new WoodItemCollection(
		"frame",
		type -> new BlockItem(
			DecoBlocks.WOOD_FRAMES.get(type),
			new Item.Settings()
		)
	);

	//////////////////////////////// catwalk platforms ////////////////////////////////

	@UseDataGen(IronCatwalkPlatformDataGenerator.class)
	public static final BlockItem IRON_CATWALK_PLATFORM = BigTechItems.registerPlacer(
		DecoBlocks.IRON_CATWALK_PLATFORM
	);
	@UseDataGen(CopperCatwalkPlatformDataGenerator.class)
	public static final CopperItemCollection COPPER_CATWALK_PLATFORMS = new CopperItemCollection(
		"catwalk_platform",
		type -> new BlockItem(
			DecoBlocks.COPPER_CATWALK_PLATFORMS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(WoodCatwalkPlatformDataGenerator.class)
	public static final WoodItemCollection WOOD_CATWALK_PLATFORMS = new WoodItemCollection(
		"catwalk_platform",
		type -> new BlockItem(
			DecoBlocks.WOOD_CATWALK_PLATFORMS.get(type),
			new Item.Settings()
		)
	);

	//////////////////////////////// catwalk stairs ////////////////////////////////

	@UseDataGen(IronCatwalkStairsDataGenerator.class)
	public static final CatwalkStairsBlockItem IRON_CATWALK_STAIRS = BigTechItems.registerCatwalkStairs(
		DecoBlocks.IRON_CATWALK_STAIRS
	);
	@UseDataGen(CopperCatwalkStairsDataGenerator.class)
	public static final CopperItemCollection COPPER_CATWALK_STAIRS = new CopperItemCollection(
		"catwalk_stairs",
		type -> new CatwalkStairsBlockItem(
			DecoBlocks.COPPER_CATWALK_STAIRS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(WoodCatwalkStairsDataGenerator.class)
	public static final WoodItemCollection WOOD_CATWALK_STAIRS = new WoodItemCollection(
		"catwalk_stairs",
		type -> new CatwalkStairsBlockItem(
			DecoBlocks.WOOD_CATWALK_STAIRS.get(type),
			new Item.Settings()
		)
	);

	//////////////////////////////// misc ////////////////////////////////

	@UseDataGen(IronLadderDataGenerator.class)
	public static final BlockItem IRON_LADDER = BigTechItems.registerPlacer(
		DecoBlocks.IRON_LADDER
	);
	@UseDataGen(CopperLadderDataGenerator.class)
	public static final CopperItemCollection COPPER_LADDERS = new CopperItemCollection(
		"ladder",
		type -> new BlockItem(
			DecoBlocks.COPPER_LADDERS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(CopperBarsDataGenerator.class)
	public static final CopperItemCollection COPPER_BARS = new CopperItemCollection(
		"bars",
		type -> new BlockItem(
			DecoBlocks.COPPER_BARS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(CopperSlabDataGenerator.class)
	public static final CopperItemCollection COPPER_SLABS = new CopperItemCollection(
		"slab",
		type -> new BlockItem(
			DecoBlocks.COPPER_SLABS.get(type),
			new Item.Settings()
		)
	);

	public static void init() {}
}