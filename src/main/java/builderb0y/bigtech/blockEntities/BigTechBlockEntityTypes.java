package builderb0y.bigtech.blockEntities;

import java.util.Set;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.InventoryDataGenerator;

public class BigTechBlockEntityTypes {

	@UseDataGen(InventoryDataGenerator.class)
	public static final BlockEntityType<SorterBeltBlockEntity> SORTER_BELT = register(
		"sorter_belt",
		SorterBeltBlockEntity::new,
		BigTechBlocks.SORTER_BELT
	);
	@UseDataGen(InventoryDataGenerator.class)
	public static final BlockEntityType<TransmuterBlockEntity> TRANSMUTER = register(
		"transmuter",
		TransmuterBlockEntity::new,
		BigTechBlocks.TRANSMUTER
	);
	@UseDataGen(void.class) //handled by ItemBlock (BeamInterceptorDataGenerator).
	public static final BlockEntityType<BeamInterceptorBlockEntity> BEAM_INTERCEPTOR = register(
		"beam_interceptor",
		BeamInterceptorBlockEntity::new,
		BigTechBlocks.BEAM_INTERCEPTOR
	);
	@UseDataGen(void.class)
	public static final BlockEntityType<PrismBlockEntity> PRISM = register(
		"prism",
		PrismBlockEntity::new,
		BigTechBlocks.PRISM
	);
	@UseDataGen(void.class)
	public static final BlockEntityType<TeslaCoilBlockEntity> TESLA_COIL = register(
		"tesla_coil",
		TeslaCoilBlockEntity::new,
		BigTechBlocks.COPPER_COIL
	);

	public static <B extends BlockEntity> BlockEntityType<B> register(String name, BlockEntityFactory<B> factory, Block... blocks) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, BigTechMod.modID(name), new BlockEntityType<>(factory, Set.of(blocks), null));
	}

	public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		BlockEntityRendererFactories.register(TESLA_COIL, TeslaCoilBlockEntityRenderer::new);
	}
}