package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import builderb0y.bigtech.BigTechMod;

public class BigTechBlockTags {

	public static final TagKey<Block>
		BELTS        = of("belts"),
		BELT_SUPPORT = of("belt_support");

	public static TagKey<Block> of(String name) {
		return TagKey.of(RegistryKeys.BLOCK, BigTechMod.modID(name));
	}
}