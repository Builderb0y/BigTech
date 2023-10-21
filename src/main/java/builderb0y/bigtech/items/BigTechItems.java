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
import builderb0y.bigtech.datagen.impl.ascenders.AscenderDataGenerator;
import builderb0y.bigtech.datagen.impl.ascenders.DescenderDataGenerator;
import builderb0y.bigtech.datagen.impl.belts.*;

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