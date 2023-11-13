package builderb0y.bigtech.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.*;
import builderb0y.bigtech.datagen.impl.ascenders.AscenderDataGenerator;
import builderb0y.bigtech.datagen.impl.ascenders.DescenderDataGenerator;
import builderb0y.bigtech.datagen.impl.belts.*;
import builderb0y.bigtech.datagen.impl.catwalkPlatforms.CopperCatwalkPlatformDataGenerator;
import builderb0y.bigtech.datagen.impl.catwalkPlatforms.IronCatwalkPlatformDataGenerator;
import builderb0y.bigtech.datagen.impl.catwalkStairs.CopperCatwalkStairsDataGenerator;
import builderb0y.bigtech.datagen.impl.catwalkStairs.IronCatwalkStairsDataGenerator;
import builderb0y.bigtech.datagen.impl.frames.CopperFrameDataGenerator;
import builderb0y.bigtech.datagen.impl.frames.GoldFrameDataGenerator;
import builderb0y.bigtech.datagen.impl.frames.IronFrameDataGenerator;
import builderb0y.bigtech.datagen.impl.frames.WoodenFrameDataGenerator;
import builderb0y.bigtech.datagen.impl.metalLadders.CopperLadderDataGenerator;
import builderb0y.bigtech.datagen.impl.metalLadders.IronLadderDataGenerator;

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
	@UseDataGen(TrapdoorBeltDataGenerator.class)
	public static final BeltBlockItem TRAPDOOR_BELT = registerBelt(
		BigTechBlocks.TRAPDOOR_BELT
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
	@UseDataGen(EjectorBeltDataGenerator.class)
	public static final BeltBlockItem EJECTOR_BELT = registerBelt(
		BigTechBlocks.EJECTOR_BELT
	);
	@UseDataGen(InjectorBeltDataGenerator.class)
	public static final BeltBlockItem INJECTOR_BELT = registerBelt(
		BigTechBlocks.INJECTOR_BELT
	);
	@UseDataGen(LauncherBeltDataGenerator.class)
	public static final BlockItem LAUNCHER_BELT = registerBelt(
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
	@UseDataGen(CopperNuggetDataGenerator.class)
	public static final Item COPPER_NUGGET = register(
		BigTechMod.modID("copper_nugget"),
		new Item(new Item.Settings())
	);
	@UseDataGen(IronFrameDataGenerator.class)
	public static final BlockItem IRON_FRAME = registerPlacer(
		BigTechBlocks.IRON_FRAME
	);
	@UseDataGen(GoldFrameDataGenerator.class)
	public static final BlockItem GOLD_FRAME = registerPlacer(
		BigTechBlocks.GOLD_FRAME
	);
	@UseDataGen(CopperFrameDataGenerator.class)
	public static final CopperItemCollection COPPER_FRAMES = new CopperItemCollection(
		"frame",
		type -> new BlockItem(
			BigTechBlocks.COPPER_FRAMES.get(type),
			new Item.Settings()
		)
	);
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
	@UseDataGen(IronLadderDataGenerator.class)
	public static final BlockItem IRON_LADDER = registerPlacer(
		BigTechBlocks.IRON_LADDER
	);
	@UseDataGen(CopperLadderDataGenerator.class)
	public static final CopperItemCollection COPPER_LADDERS = new CopperItemCollection(
		"ladder",
		type -> new BlockItem(
			BigTechBlocks.COPPER_LADDERS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(CopperSlabDataGenerator.class)
	public static final CopperItemCollection COPPER_SLABS = new CopperItemCollection(
		"slab",
		type -> new BlockItem(
			BigTechBlocks.COPPER_SLABS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(EncasedRedstoneBlockDataGenerator.class)
	public static final BlockItem ENCASED_REDSTONE_BLOCK = registerPlacer(
		BigTechBlocks.ENCASED_REDSTONE_BLOCK
	);
	@UseDataGen(EncasedSlimeBlockDataGenerator.class)
	public static final BlockItem
		ENCASED_SLIME_BLOCK = registerPlacer(BigTechBlocks.ENCASED_SLIME_BLOCK),
		ENCASED_HONEY_BLOCK = registerPlacer(BigTechBlocks.ENCASED_HONEY_BLOCK);
	@UseDataGen(LightningCableDataGenerator.class)
	public static final BlockItem
		IRON_LIGHTNING_CAbLE   = registerPlacer(BigTechBlocks.IRON_LIGHTNING_CABLE),
		GOLD_LIGHTNING_CABLE   = registerPlacer(BigTechBlocks.GOLD_LIGHTNING_CABLE),
		COPPER_LIGHTNING_CABLE = registerPlacer(BigTechBlocks.COPPER_LIGHTNING_CABLE);
	@UseDataGen(TransmuterDataGenerator.class)
	public static final BlockItem TRANSMUTER = registerPlacer(
		BigTechBlocks.TRANSMUTER
	);
	@UseDataGen(MagnetiteNuggetDataGenerator.class)
	public static final Item MAGNETITE_NUGGET = register(
		"magnetite_nugget",
		new Item(new Item.Settings())
	);
	@UseDataGen(MagnetiteIngotDataGenerator.class)
	public static final Item MAGNETITE_INGOT = register(
		"magnetite_ingot",
		new Item(new Item.Settings())
	);
	@UseDataGen(MagnetiteBlockDataGenerator.class)
	public static final BlockItem MAGNETITE_BLOCK = registerPlacer(
		BigTechBlocks.MAGNETITE_BLOCK
	);
	@UseDataGen(IronCatwalkPlatformDataGenerator.class)
	public static final BlockItem IRON_CATWALK_PLATFORM = registerPlacer(
		BigTechBlocks.IRON_CATWALK_PLATFORM
	);
	@UseDataGen(CopperCatwalkPlatformDataGenerator.class)
	public static final CopperItemCollection COPPER_CATWALK_PLATFORMS = new CopperItemCollection(
		"catwalk_platform",
		type -> new BlockItem(
			BigTechBlocks.COPPER_CATWALK_PLATFORMS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(IronCatwalkStairsDataGenerator.class)
	public static final CatwalkStairsBlockItem IRON_CATWALK_STAIRS = registerCatwalkStairs(
		BigTechBlocks.IRON_CATWALK_STAIRS
	);
	@UseDataGen(CopperCatwalkStairsDataGenerator.class)
	public static final CopperItemCollection COPPER_CATWALK_STAIRS = new CopperItemCollection(
		"catwalk_stairs",
		type -> new CatwalkStairsBlockItem(
			BigTechBlocks.COPPER_CATWALK_STAIRS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(CopperBarsDataGenerator.class)
	public static final CopperItemCollection COPPER_BARS = new CopperItemCollection(
		"bars",
		type -> new BlockItem(
			BigTechBlocks.COPPER_BARS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(MediumWeightedPressurePlateDataGenerator.class)
	public static final BlockItem MEDIUM_WEIGHTED_PRESSURE_PLATE = registerPlacer(
		BigTechBlocks.MEDIUM_WEIGHTED_PRESSURE_PLATE
	);
	@UseDataGen(CrystalClusterDataGenerator.class)
	public static final CrystalClusterItemCollection CRYSTAL_CLUSTERS = new CrystalClusterItemCollection(
		true,
		type -> new BlockItem(
			BigTechBlocks.CRYSTAl_ClUSTERS.get(type),
			new Item.Settings()
		)
	);
	@UseDataGen(CrystalDebrisDataGenerator.class)
	public static final Item CRYSTAL_DEBRIS = register(
		"crystal_debris",
		new Item(new Item.Settings())
	);
	@UseDataGen(CrystallineSandDataGenerator.class)
	public static final BlockItem CRYSTALLINE_SAND = registerPlacer(
		BigTechBlocks.CRYSTALLINE_SAND
	);
	@UseDataGen(LensDataGenerator.class)
	public static final Item LENS = register(
		"lens",
		new Item(new Item.Settings())
	);
	@UseDataGen(RedstoneTransmitterDataGenerator.class)
	public static final BlockItem REDSTONE_TRANSMITTER = registerPlacer(
		BigTechBlocks.REDSTONE_TRANSMITTER
	);
	@UseDataGen(RedstoneReceiverDataGenerator.class)
	public static final BlockItem REDSTONE_RECEIVER = registerPlacer(
		BigTechBlocks.REDSTONE_RECEIVER
	);
	@UseDataGen(BeamInterceptorDataGenerator.class)
	public static final BlockItem BEAM_INTERCEPTOR = register(
		"beam_interceptor",
		new ClientNbtCopyingBlockItem(
			BigTechBlocks.BEAM_INTERCEPTOR,
			new Item.Settings()
		)
	);
	@UseDataGen(MirrorDataGenerator.class)
	public static final BlockItem MIRROR = registerPlacer(
		BigTechBlocks.MIRROR
	);
	@UseDataGen(PhaseScramblerDataGenerator.class)
	public static final BlockItem PHASE_SCRAMBLER = registerPlacer(
		BigTechBlocks.PHASE_SCRAMBLER
	);
	@UseDataGen(PhaseAlignerDataGenerator.class)
	public static final BlockItem PHASE_ALIGNER = registerPlacer(
		BigTechBlocks.PHASE_ALIGNER
	);
	@UseDataGen(CopperCoilDataGenerator.class)
	public static final BlockItem COPPER_COIL = registerPlacer(
		BigTechBlocks.COPPER_COIL
	);

	public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> {
				if (tintIndex == 1) {
					NbtCompound nbt = stack.getSubNbt(BlockItem.BLOCK_ENTITY_TAG_KEY);
					if (nbt != null) {
						float[] color = nbt.getFloatArray("color");
						if (color.length == 3) {
							return MathHelper.packRgb(color[0], color[1], color[2]) | 0xFF000000;
						}
					}
				}
				return -1;
			},
			BEAM_INTERCEPTOR
		);
	}

	public static BeltBlockItem registerBelt(Block block) {
		return register(Registries.BLOCK.getId(block), new BeltBlockItem(block, new Item.Settings()));
	}

	public static CatwalkStairsBlockItem registerCatwalkStairs(Block block) {
		return register(Registries.BLOCK.getId(block), new CatwalkStairsBlockItem(block, new Item.Settings()));
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