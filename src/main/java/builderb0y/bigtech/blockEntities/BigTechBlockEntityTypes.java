package builderb0y.bigtech.blockEntities;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlocks;

public class BigTechBlockEntityTypes {

	public static final BlockEntityType<SorterBeltBlockEntity> SORTER_BELT = register(
		"sorter_belt",
		SorterBeltBlockEntity::new,
		BigTechBlocks.SORTER_BELT
	);

	public static <B extends BlockEntity> BlockEntityType<B> register(String name, BlockEntityFactory<B> factory, Block... blocks) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, BigTechMod.modID(name), new BlockEntityType<>(factory, Set.of(blocks), null));
	}

	public static void init() {}
}