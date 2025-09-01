package builderb0y.bigtech;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.client.MinecraftClient;

import builderb0y.bigtech.beams.base.BeamRendering;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGen;
import builderb0y.bigtech.entities.BigTechEntityRenderers;
import builderb0y.bigtech.gui.handledScreens.BigTechHandledScreens;
import builderb0y.bigtech.items.BigTechItems;
import builderb0y.bigtech.items.BigTechSelectProperties;
import builderb0y.bigtech.models.BigTechModels;
import builderb0y.bigtech.networking.BigTechNetwork;
import builderb0y.bigtech.particles.BigTechParticles;
import builderb0y.bigtech.tweaks.PlacementPreview;
import builderb0y.bigtech.tweaks.PlacementSpeed;

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
		BigTechEntityRenderers.init();
		BigTechParticles.initClient();
		BeamRendering.initClient();
		PlacementPreview.init();
		PlacementSpeed.init();
		BigTechSelectProperties.init();
		if (DataGen.isEnabled()) DataGen.run();
		BigTechMod.LOGGER.info("Done initializing on client.");
		if (BigTechMod.AUDIT) MinecraftClient.getInstance().execute(() -> MixinEnvironment.getCurrentEnvironment().audit());
	}
}