package builderb0y.bigtech.blocks;

import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.belts.DirectionalBeltBlock;
import builderb0y.bigtech.blocks.belts.SpeedyBeltBlock;
import builderb0y.bigtech.datagen.base.UseDataGen;

public class BigTechBlocks {

	@UseDataGen(void.class)
	public static final DirectionalBeltBlock BELT = register(
		"belt",
		new DirectionalBeltBlock(
			AbstractBlock
			.Settings
			.create()
			.mapColor(MapColor.STONE_GRAY)
			.strength(0.2F)
		)
	);
	@UseDataGen(void.class)
	public static final SpeedyBeltBlock SPEEDY_BELT = register(
		"speedy_belt",
		new SpeedyBeltBlock(
			AbstractBlock
				.Settings
				.create()
				.mapColor(MapColor.STONE_GRAY)
				.strength(0.2F)
		)
	);

	public static void init() {
		LandPathNodeTypesRegistry.register(BELT, PathNodeType.RAIL, null);
	}

	public static <B extends Block> B register(String name, B block) {
		return Registry.register(Registries.BLOCK, BigTechMod.modID(name), block);
	}
}