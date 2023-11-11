package builderb0y.bigtech;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.entity.EntityType;

import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGen;
import builderb0y.bigtech.entities.BetterLightningEntityRenderer;
import builderb0y.bigtech.handledScreens.BigTechHandledScreens;
import builderb0y.bigtech.items.BigTechItems;
import builderb0y.bigtech.models.BigTechModels;
import builderb0y.bigtech.particles.BigTechParticles;

@Environment(EnvType.CLIENT)
public class BigTechClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BigTechMod.LOGGER.info("Initializing on client...");
		BigTechBlocks.initClient();
		BigTechItems.initClient();
		BigTechHandledScreens.initClient();
		BigTechModels.init();
		EntityRendererRegistry.register(EntityType.LIGHTNING_BOLT, BetterLightningEntityRenderer::new);
		BigTechParticles.initClient();
		if (DataGen.isEnabled) DataGen.run();
		BigTechMod.LOGGER.info("Done initializing on client.");
	}
}