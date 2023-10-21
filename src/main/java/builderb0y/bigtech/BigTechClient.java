package builderb0y.bigtech;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.handledScreens.BigTechHandledScreens;

@Environment(EnvType.CLIENT)
public class BigTechClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BigTechBlocks.initClient();
		BigTechHandledScreens.initClient();
	}
}