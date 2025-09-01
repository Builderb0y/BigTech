package builderb0y.bigtech.blockEntities;

import java.util.Collection;
import java.util.Set;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Direction;

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
	public static final BlockEntityType<ShortRangeDeployerBlockEntity> SHORT_RANGE_DEPLOYER = register(
		"short_range_deployer",
		ShortRangeDeployerBlockEntity::new,
		FunctionalBlocks.SHORT_RANGE_DEPLOYER
	);
	public static final BlockEntityType<LongRangeDeployerBlockEntity> LONG_RANGE_DEPLOYER = register(
		"long_range_deployer",
		LongRangeDeployerBlockEntity::new,
		FunctionalBlocks.LONG_RANGE_DEPLOYER
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
	public static final BlockEntityType<IgniterBlockEntity> IGNITER = register(
		"igniter",
		IgniterBlockEntity::new,
		FunctionalBlocks.IGNITER,
		FunctionalBlocks.IGNITER_BEAM
	);
	static {
		Registries.BLOCK_ENTITY_TYPE.addAlias(
			BigTechMod.modID("ignitor"),
			BigTechMod.modID("igniter")
		);
	}
	public static final BlockEntityType<SilverIodideCannonBlockEntity> SILVER_IODIDE_CANNON = register(
		"silver_iodide_cannon",
		SilverIodideCannonBlockEntity::new,
		FunctionalBlocks.SILVER_IODIDE_CANNON
	);
	public static final BlockEntityType<StoneCraftingTableBlockEntity> STONE_CRAFTING_TABLE = register(
		"stone_crafting_table",
		StoneCraftingTableBlockEntity::new,
		FunctionalBlocks.STONE_CRAFTING_TABLE
	);
	public static final BlockEntityType<SpawnerInterceptorBlockEntity> SPAWNER_INTERCEPTOR = register(
		"spawner_interceptor",
		SpawnerInterceptorBlockEntity::new,
		FunctionalBlocks.SPAWNER_INTERCEPTOR
	);
	public static final BlockEntityType<ConductiveAnvilBlockEntity> CONDUCTIVE_ANVIL = register(
		"conductive_anvil",
		ConductiveAnvilBlockEntity::new,
		FunctionalBlocks.CONDUCTIVE_ANVILS.asList()
	);
	public static final BlockEntityType<PulsarBlockEntity> PULSAR = register(
		"pulsar",
		PulsarBlockEntity::new,
		FunctionalBlocks.PULSAR
	);
	public static final BlockEntityType<TechnoCrafterBlockEntity> TECHNO_CRAFTER = register(
		"techno_crafter",
		TechnoCrafterBlockEntity::new,
		FunctionalBlocks.TECHNO_CRAFTER
	);
	public static final BlockEntityType<CrucibleBlockEntity> CRUCIBLE = register(
		"crucible",
		CrucibleBlockEntity::new,
		FunctionalBlocks.CRUCIBLE
	);
	public static final BlockEntityType<AssemblerBlockEntity> ASSEMBLER = register(
		"assembler",
		AssemblerBlockEntity::new,
		FunctionalBlocks.ASSEMBLER
	);
	public static final BlockEntityType<MicroProcessorBlockEntity> MICRO_PROCESSOR = register(
		"micro_processor",
		MicroProcessorBlockEntity::new,
		FunctionalBlocks.MICRO_PROCESSOR
	);

	public static <B extends BlockEntity> BlockEntityType<B> register(String name, BlockEntityFactory<B> factory, Block... blocks) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, BigTechMod.modID(name), new BlockEntityType<>(factory, Set.of(blocks)));
	}

	public static <B extends BlockEntity> BlockEntityType<B> register(String name, BlockEntityFactory<B> factory, Collection<Block> blocks) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, BigTechMod.modID(name), new BlockEntityType<>(factory, Set.copyOf(blocks)));
	}

	public static void init() {
		ItemStorage.SIDED.registerForBlockEntity(
			(AssemblerBlockEntity assembler, Direction direction) -> new AssemblerBlockEntity.MainStorage(assembler),
			ASSEMBLER
		);
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		BlockEntityRendererFactories.register(TESLA_COIL,           TeslaCoilBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(CRUCIBLE,              CrucibleBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(MICRO_PROCESSOR, MicroProcessorBlockEntityRenderer::new);
	}
}