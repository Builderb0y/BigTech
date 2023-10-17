package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;

public class BigTechBlocks {

	public static void init() {}

	public static <B extends Block> B register(String name, B block) {
		return Registry.register(Registries.BLOCK, BigTechMod.modID(name), block);
	}
}