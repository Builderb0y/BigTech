package builderb0y.bigtech;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;

import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGen;
import builderb0y.bigtech.entities.BetterLightningEntityRenderer;
import builderb0y.bigtech.entities.BigTechEntityTypes;
import builderb0y.bigtech.entities.MinerEntityRenderer;
import builderb0y.bigtech.gui.handledScreens.BigTechHandledScreens;
import builderb0y.bigtech.items.BigTechItems;
import builderb0y.bigtech.models.BigTechModels;
import builderb0y.bigtech.networking.BigTechNetwork;
import builderb0y.bigtech.particles.BigTechParticles;

@Environment(EnvType.CLIENT)
public class BigTechClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BigTechMod.LOGGER.info("Initializing on client...");
		BigTechBlocks.initClient();
		BigTechItems.initClient();
		BigTechBlockEntityTypes.initClient();
		BigTechHandledScreens.initClient();
		BigTechNetwork.initClient();
		BigTechModels.init();
		EntityRendererRegistry.register(EntityType.LIGHTNING_BOLT, BetterLightningEntityRenderer::new);
		EntityRendererRegistry.register(BigTechEntityTypes.MINER, MinerEntityRenderer::new);
		BigTechParticles.initClient();
		if (DataGen.isEnabled()) DataGen.run();
		BigTechMod.LOGGER.info("Done initializing on client.");
		if (BigTechMod.audit) MinecraftClient.getInstance().execute(() -> MixinEnvironment.getCurrentEnvironment().audit());
	}
}