package builderb0y.bigtech.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.BlockStateResolver;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.item.model.ItemModelTypes;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.registry.Registries;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.blocks.MaterialBlocks;

@Environment(EnvType.CLIENT)
public class BigTechModels {

	public static void init() {
		ItemModelTypes.ID_MAPPER.put(BigTechMod.modID("prism"), PrismRenderer.UnbakedItemModel.CODEC);
		ItemModelTypes.ID_MAPPER.put(BigTechMod.modID("crystal_cluster"), CrystalClusterRenderer.UnbakedItemModel.CODEC);
		TintSourceTypes.ID_MAPPER.put(BigTechMod.modID("beam_interceptor"), BeamInterceptorTintSource.CODEC);
		ModelLoadingPlugin.register((ModelLoadingPlugin.Context pluginContext) -> {
			MaterialBlocks.CRYSTAL_CLUSTERS.forEach((Block block) -> {
				pluginContext.registerBlockStateResolver(block, (BlockStateResolver.Context resolutionContext) -> {
					block.getStateManager().getStates().forEach((BlockState state) -> {
						resolutionContext.setModel(state, new CrystalClusterRenderer.UnbakedBlockModel(BigTechMod.modID("block/" + Registries.BLOCK.getId(block).getPath())));
					});
				});
			});
			pluginContext.registerBlockStateResolver(FunctionalBlocks.PRISM, (BlockStateResolver.Context resolutionContext) -> {
				FunctionalBlocks.PRISM.getStateManager().getStates().forEach((BlockState state) -> {
					resolutionContext.setModel(state, new PrismRenderer.UnbakedBlockModel());
				});
			});
		});
	}
}