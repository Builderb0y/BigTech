package builderb0y.bigtech.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.belts.NormalBeltDataGenerator;
import builderb0y.bigtech.datagen.impl.belts.SpeedyBeltDataGenerator;

public class BigTechItems {

	@UseDataGen(NormalBeltDataGenerator.class)
	public static final BeltBlockItem BELT = register("belt", new BeltBlockItem(BigTechBlocks.BELT, new Item.Settings()));
	@UseDataGen(SpeedyBeltDataGenerator.class)
	public static final BeltBlockItem SPEEDY_BELT = register("speedy_belt", new BeltBlockItem(BigTechBlocks.SPEEDY_BELT, new Item.Settings()));

	public static void init() {}

	public static BlockItem registerPlacer(Block block) {
		return Registry.register(Registries.ITEM, Registries.BLOCK.getId(block), new BlockItem(block, new Item.Settings()));
	}

	public static <I extends Item> I register(String name, I item) {
		return Registry.register(Registries.ITEM, BigTechMod.modID(name), item);
	}
}