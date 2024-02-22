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
import builderb0y.bigtech.blocks.FunctionalBlocks;

public class BigTechBlockEntityTypes {

	public static final BlockEntityType<SorterBeltBlockEntity> SORTER_BELT = register(
		"sorter_belt",
		SorterBeltBlockEntity::new,
		FunctionalBlocks.SORTER_BELT
	);
	public static final BlockEntityType<TransmuterBlockEntity> TRANSMUTER = register(
		"transmuter",
		TransmuterBlockEntity::new,
		FunctionalBlocks.TRANSMUTER
	);
	public static final BlockEntityType<BeamInterceptorBlockEntity> BEAM_INTERCEPTOR = register(
		"beam_interceptor",
		BeamInterceptorBlockEntity::new,
		FunctionalBlocks.BEAM_INTERCEPTOR
	);
	public static final BlockEntityType<TripwireBlockEntity> TRIPWIRE = register(
		"tripwire",
		TripwireBlockEntity::new,
		FunctionalBlocks.TRIPWIRE
	);
	public static final BlockEntityType<PrismBlockEntity> PRISM = register(
		"prism",
		PrismBlockEntity::new,
		FunctionalBlocks.PRISM
	);
	public static final BlockEntityType<ShortRangeDestroyerBlockEntity> SHORT_RANGE_DESTROYER = register(
		"short_range_destroyer",
		ShortRangeDestroyerBlockEntity::new,
		FunctionalBlocks.SHORT_RANGE_DESTROYER
	);
	public static final BlockEntityType<LongRangeDestroyerBlockEntity> LONG_RANGE_DESTROYER = register(
		"long_range_destroyer",
		LongRangeDestroyerBlockEntity::new,
		FunctionalBlocks.LONG_RANGE_DESTROYER
	);
	public static final BlockEntityType<TeslaCoilBlockEntity> TESLA_COIL = register(
		"tesla_coil",
		TeslaCoilBlockEntity::new,
		FunctionalBlocks.COPPER_COIL
	);
	public static final BlockEntityType<LightningJarBlockEntity> LIGHTNING_JAR = register(
		"lightning_jar",
		LightningJarBlockEntity::new,
		FunctionalBlocks.SMALL_LIGHTNING_JAR,
		FunctionalBlocks.LARGE_LIGHTNING_JAR
	);
	public static final BlockEntityType<IgnitorBlockEntity> IGNITOR = register(
		"ignitor",
		IgnitorBlockEntity::new,
		FunctionalBlocks.IGNITOR,
		FunctionalBlocks.IGNITOR_BEAM
	);
	public static final BlockEntityType<SilverIodideCannonBlockEntity> SILVER_IODIDE_CANNON = register(
		"silver_iodide_cannon",
		SilverIodideCannonBlockEntity::new,
		FunctionalBlocks.SILVER_IODIDE_CANNON
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