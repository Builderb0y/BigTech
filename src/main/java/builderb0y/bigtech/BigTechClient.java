package builderb0y.bigtech;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGen;
import builderb0y.bigtech.handledScreens.BigTechHandledScreens;
import builderb0y.bigtech.models.BigTechModels;
import builderb0y.bigtech.particles.BigTechParticles;

@Environment(EnvType.CLIENT)
public class BigTechClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BigTechMod.LOGGER.info("Initializing on client...");
		BigTechBlocks.initClient();
		BigTechHandledScreens.initClient();
		BigTechModels.init();
		BigTechParticles.initClient();
		if (DataGen.isEnabled) DataGen.run();
		BigTechMod.LOGGER.info("Done initializing on client.");
	}
}