package builderb0y.bigtech;

import net.fabricmc.api.ClientModInitializer;

import builderb0y.bigtech.blocks.BigTechBlocks;

public class BigTechClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BigTechBlocks.initClient();
	}
}