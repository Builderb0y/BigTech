package builderb0y.bigtech.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.CopperNuggetDataGenerator;
import builderb0y.bigtech.datagen.impl.EncasedRedstoneBlockDataGenerator;
import builderb0y.bigtech.datagen.impl.ascenders.AscenderDataGenerator;
import builderb0y.bigtech.datagen.impl.ascenders.DescenderDataGenerator;
import builderb0y.bigtech.datagen.impl.belts.*;
import builderb0y.bigtech.datagen.impl.frames.*;

public class BigTechItems {

	@UseDataGen(NormalBeltDataGenerator.class)
	public static final BeltBlockItem BELT = registerBelt(
		BigTechBlocks.BELT
	);
	@UseDataGen(SpeedyBeltDataGenerator.class)
	public static final BeltBlockItem SPEEDY_BELT = registerBelt(
		BigTechBlocks.SPEEDY_BELT
	);
	@UseDataGen(BrakeBeltDataGenerator.class)
	public static final BeltBlockItem BRAKE_BELT = registerBelt(
		BigTechBlocks.BRAKE_BELT
	);
	@UseDataGen(DirectorBeltDataGenerator.class)
	public static final BlockItem DIRECTOR_BELT = registerPlacer(
		BigTechBlocks.DIRECTOR_BELT
	);
	@UseDataGen(DetectorBeltDataGenerator.class)
	public static final BeltBlockItem DETECTOR_BELT = registerBelt(
		BigTechBlocks.DETECTOR_BELT
	);
	@UseDataGen(SorterBeltDataGenerator.class)
	public static final BeltBlockItem SORTER_BELT = registerBelt(
		BigTechBlocks.SORTER_BELT
	);
	@UseDataGen(LauncherBeltDataGenerator.class)
	public static final BlockItem LAUNCHER_BELT = registerPlacer(
		BigTechBlocks.LAUNCHER_BELT
	);
	@UseDataGen(AscenderDataGenerator.class)
	public static final AscenderBlockItem ASCENDER = register(
		"ascender",
		new AscenderBlockItem(
			BigTechBlocks.ASCENDER,
			new Item.Settings()
		)
	);
	@UseDataGen(DescenderDataGenerator.class)
	public static final AscenderBlockItem DESCENDER = register(
		"descender",
		new AscenderBlockItem(
			BigTechBlocks.DESCENDER,
			new Item.Settings()
		)
	);
	@UseDataGen(IronFrameDataGenerator.class)
	public static final BlockItem IRON_FRAME = registerPlacer(
		BigTechBlocks.IRON_FRAME
	);
	@UseDataGen(CopperNuggetDataGenerator.class)
	public static final Item COPPER_NUGGET = register(
		BigTechMod.modID("copper_nugget"),
		new Item(new Item.Settings())
	);
	@UseDataGen(CopperFrameDataGenerator.class)
	public static final BlockItem COPPER_FRAME = registerPlacer(
		BigTechBlocks.COPPER_FRAME
	);
	@UseDataGen(GoldFrameDataGenerator.class)
	public static final BlockItem GOLD_FRAME = registerPlacer(
		BigTechBlocks.GOLD_FRAME
	);
	@UseDataGen(DegradedCopperFrameDataGenerator.class)
	public static final BlockItem
		EXPOSED_COPPER_FRAME   = registerPlacer(BigTechBlocks.EXPOSED_COPPER_FRAME),
		WEATHERED_COPPER_FRAME = registerPlacer(BigTechBlocks.WEATHERED_COPPER_FRAME),
		OXIDIZED_COPPER_FRAME  = registerPlacer(BigTechBlocks.OXIDIZED_COPPER_FRAME);
	@UseDataGen(WaxedCopperFrameDataGenerator.class)
	public static final BlockItem
		WAXED_COPPER_FRAME           = registerPlacer(BigTechBlocks.WAXED_COPPER_FRAME),
		WAXED_EXPOSED_COPPER_FRAME   = registerPlacer(BigTechBlocks.WAXED_EXPOSED_COPPER_FRAME),
		WAXED_WEATHERED_COPPER_FRAME = registerPlacer(BigTechBlocks.WAXED_WEATHERED_COPPER_FRAME),
		WAXED_OXIDIZED_COPPER_FRAME  = registerPlacer(BigTechBlocks.WAXED_OXIDIZED_COPPER_FRAME);
	@UseDataGen(WoodenFrameDataGenerator.class)
	public static final BlockItem
		OAK_FRAME      = registerPlacer(BigTechBlocks.OAK_FRAME),
		SPRUCE_FRAME   = registerPlacer(BigTechBlocks.SPRUCE_FRAME),
		BIRCH_FRAME    = registerPlacer(BigTechBlocks.BIRCH_FRAME),
		JUNGLE_FRAME   = registerPlacer(BigTechBlocks.JUNGLE_FRAME),
		ACACIA_FRAME   = registerPlacer(BigTechBlocks.ACACIA_FRAME),
		DARK_OAK_FRAME = registerPlacer(BigTechBlocks.DARK_OAK_FRAME),
		CHERRY_FRAME   = registerPlacer(BigTechBlocks.CHERRY_FRAME),
		MANGROVE_FRAME = registerPlacer(BigTechBlocks.MANGROVE_FRAME),
		CRIMSON_FRAME  = registerPlacer(BigTechBlocks.CRIMSON_FRAME),
		WARPED_FRAME   = registerPlacer(BigTechBlocks.WARPED_FRAME);
	@UseDataGen(EncasedRedstoneBlockDataGenerator.class)
	public static final BlockItem ENCASED_REDSTONE_BLOCK = registerPlacer(
		BigTechBlocks.ENCASED_REDSTONE_BLOCK
	);

	public static void init() {}

	public static BeltBlockItem registerBelt(Block block) {
		return register(Registries.BLOCK.getId(block), new BeltBlockItem(block, new Item.Settings()));
	}

	public static BlockItem registerPlacer(Block block) {
		return register(Registries.BLOCK.getId(block), new BlockItem(block, new Item.Settings()));
	}

	public static <I extends Item> I register(String name, I item) {
		return register(BigTechMod.modID(name), item);
	}

	public static <I extends Item> I register(Identifier id, I item) {
		return Registry.register(Registries.ITEM, id, item);
	}
}