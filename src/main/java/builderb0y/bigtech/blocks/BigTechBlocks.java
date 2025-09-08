package builderb0y.bigtech.blocks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.*;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.blockEntities.AssemblerBlockEntity;
import builderb0y.bigtech.blockEntities.BeamInterceptorBlockEntity;
import builderb0y.bigtech.mixins.AbstractBlockSettings_RegistryKeyAccessor;
import builderb0y.bigtech.registrableCollections.RegistrableCollection;

/**
welcome! the actual block declarations are in
{@link MaterialBlocks}, {@link DecoBlocks}, and {@link FunctionalBlocks}.
if I ever add a 4th creative tab, that'll get its own class too.
*/
public class BigTechBlocks {

	public static final BlockSetType COPPER_BLOCK_SET_TYPE = (
		BlockSetTypeBuilder
		.copyOf(BlockSetType.IRON)
		.soundGroup(BlockSoundGroup.COPPER)
		.build(BigTechMod.modID("copper"))
	);
	public static final BlockSetType STEEL_BLOCK_SET_TYPE = (
		BlockSetTypeBuilder
		.copyOf(BlockSetType.IRON)
		.build(BigTechMod.modID("steel"))
	);
	public static final BlockSoundGroup CONDUCTIVE_ANVIL_SOUND_GROUP = new BlockSoundGroup(
		1.0F,
		1.0F,
		SoundEvents.BLOCK_COPPER_BREAK,
		SoundEvents.BLOCK_COPPER_STEP,
		SoundEvents.BLOCK_ANVIL_PLACE,
		SoundEvents.BLOCK_COPPER_HIT,
		SoundEvents.BLOCK_ANVIL_FALL
	);
	public static final CopperBlockCollection VANILLA_COPPER_BLOCKS = new CopperBlockCollection(
		false,
		Blocks.COPPER_BLOCK,
		Blocks.EXPOSED_COPPER,
		Blocks.WEATHERED_COPPER,
		Blocks.OXIDIZED_COPPER,
		Blocks.WAXED_COPPER_BLOCK,
		Blocks.WAXED_EXPOSED_COPPER,
		Blocks.WAXED_WEATHERED_COPPER,
		Blocks.WAXED_OXIDIZED_COPPER
	);
	public static final WoodBlockCollection VANILLA_PLANKS = new WoodBlockCollection(
		false,
		Blocks.OAK_PLANKS,
		Blocks.SPRUCE_PLANKS,
		Blocks.BIRCH_PLANKS,
		Blocks.JUNGLE_PLANKS,
		Blocks.ACACIA_PLANKS,
		Blocks.DARK_OAK_PLANKS,
		Blocks.MANGROVE_PLANKS,
		Blocks.CHERRY_PLANKS,
		Blocks.PALE_OAK_PLANKS,
		Blocks.CRIMSON_PLANKS,
		Blocks.WARPED_PLANKS
	);

	public static void init() {
		MaterialBlocks.init();
		DecoBlocks.init();
		FunctionalBlocks.init();
		LandPathNodeTypesRegistry.register(FunctionalBlocks.         BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks.  SPEEDY_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks.   BRAKE_BELT, (BlockState state, boolean neighbor) -> !state.get(Properties.POWERED) && !neighbor ? PathNodeType.RAIL : null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks.TRAPDOOR_BELT, (BlockState state, boolean neighbor) -> state.get(Properties.POWERED) != state.get(Properties.INVERTED) && !neighbor ? PathNodeType.RAIL : null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks.DIRECTOR_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks.DETECTOR_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks.  SORTER_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks. EJECTOR_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks.INJECTOR_BELT, PathNodeType.RAIL, null);
		LandPathNodeTypesRegistry.register(FunctionalBlocks.LAUNCHER_BELT, PathNodeType.RAIL, null);
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		BlockRenderLayerMap.putBlocks(
			BlockRenderLayer.CUTOUT,
			FunctionalBlocks.TRAPDOOR_BELT,
			DecoBlocks.IRON_FRAME,
			DecoBlocks.STEEL_FRAME,
			DecoBlocks.GOLD_FRAME,
			DecoBlocks.SILVER_FRAME,
			DecoBlocks.ELECTRUM_FRAME,
			DecoBlocks.IRON_LADDER,
			DecoBlocks.STEEL_LADDER,
			FunctionalBlocks.STEEL_DOOR,
			FunctionalBlocks.TRANSMUTER,
			FunctionalBlocks.SMALL_LIGHTNING_JAR,
			FunctionalBlocks.LARGE_LIGHTNING_JAR,
			DecoBlocks.IRON_CATWALK_PLATFORM,
			DecoBlocks.IRON_CATWALK_STAIRS,
			FunctionalBlocks.BEAM_INTERCEPTOR,
			FunctionalBlocks.ASSEMBLER
		);
		BlockRenderLayerMap.putBlocks(
			BlockRenderLayer.CUTOUT,
			Stream.of(
				DecoBlocks.COPPER_FRAMES,
				DecoBlocks.WOOD_FRAMES,
				DecoBlocks.COPPER_LADDERS,
				DecoBlocks.COPPER_CATWALK_PLATFORMS,
				DecoBlocks.COPPER_CATWALK_STAIRS,
				DecoBlocks.COPPER_BARS,
				DecoBlocks.WOOD_CATWALK_PLATFORMS,
				DecoBlocks.WOOD_CATWALK_STAIRS
			)
			.flatMap(RegistrableCollection::stream)
			.toArray(Block[]::new)
		);
		BlockRenderLayerMap.putBlocks(
			BlockRenderLayer.CUTOUT_MIPPED,
			FunctionalBlocks.SILVER_IODIDE_CANNON,
			FunctionalBlocks.WOODEN_HOPPER
		);
		BlockRenderLayerMap.putBlocks(
			BlockRenderLayer.TRANSLUCENT,
			FunctionalBlocks.ASCENDER,
			FunctionalBlocks.DESCENDER,
			FunctionalBlocks.PHASE_SCRAMBLER,
			FunctionalBlocks.PHASE_ALIGNER
		);
		BlockRenderLayerMap.putBlocks(
			BlockRenderLayer.TRIPWIRE,
			FunctionalBlocks.PRISM
		);
		BlockRenderLayerMap.putBlocks(
			BlockRenderLayer.TRANSLUCENT,
			MaterialBlocks.CRYSTAL_CLUSTERS
			.stream()
			.toArray(Block[]::new)
		);
		ColorProviderRegistry.BLOCK.register(
			(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) -> {
				if (tintIndex == 0 && world != null && pos != null && world.getBlockEntity(pos) instanceof BeamInterceptorBlockEntity blockEntity && blockEntity.color != null) {
					return BeamSegment.packRgb(blockEntity.color) | 0xFF000000;
				}
				return -1;
			},
			FunctionalBlocks.BEAM_INTERCEPTOR
		);
		ColorProviderRegistry.BLOCK.register(
			(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) -> {
				if (tintIndex == 0 && world != null && pos != null && world.getBlockEntity(pos) instanceof AssemblerBlockEntity assembler && assembler.color != null) {
					return BeamSegment.packRgb(assembler.color) | 0xFF000000;
				}
				return -1;
			},
			FunctionalBlocks.ASSEMBLER
		);
	}

	public static RegistryKey<Block> key(String name) {
		return RegistryKey.of(RegistryKeys.BLOCK, BigTechMod.modID(name));
	}

	public static AbstractBlock.Settings settings(String name) {
		return AbstractBlock.Settings.create().registryKey(key(name));
	}

	public static AbstractBlock.Settings copySettings(Block block, String name) {
		return AbstractBlock.Settings.copy(block).registryKey(key(name));
	}

	public static final boolean checkCodecs = FabricLoader.getInstance().isDevelopmentEnvironment();
	public static final String getCodec = FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_4970", "method_53969", "()Lcom/mojang/serialization/MapCodec;");

	public static <B extends Block> B register(B block) {
		if (checkCodecs && block.getClass() != Block.class) {
			Method method;
			try {
				method = block.getClass().getDeclaredMethod(getCodec);
			}
			catch (NoSuchMethodException exception) {
				throw new IllegalStateException(block.getClass() + " does not override getCodec()!", exception);
			}
			if (!Modifier.isAbstract(method.getModifiers())) {
				Field field;
				try {
					field = block.getClass().getDeclaredField("CODEC");
				}
				catch (NoSuchFieldException exception) {
					throw new IllegalStateException(block.getClass() + " does not have a CODEC field?", exception);
				}
				try {
					field.setAccessible(true);
					method.setAccessible(true);
					if (field.get(null) != method.invoke(block)) {
						throw new IllegalStateException(block.getClass() + " does not return its own MapCodec!");
					}
				}
				catch (ReflectiveOperationException exception) {
					throw new IllegalStateException(block.getClass() + " refuses to tell me what its codec is.");
				}
			}
		}
		return Registry.register(Registries.BLOCK, block.getSettings().<AbstractBlockSettings_RegistryKeyAccessor>as().bigtech_getRegistryKey().getValue(), block);
	}
}