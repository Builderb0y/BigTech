package builderb0y.bigtech.compat;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;

import builderb0y.bigtech.entities.BigTechEntityTypes;
import builderb0y.bigtech.entities.MinerEntity;

public class LambDynamicLightsCompat implements DynamicLightsInitializer {

	@Override
	public void onInitializeDynamicLights(ItemLightSourceManager ignored) {
		DynamicLightHandlers.registerDynamicLightHandler(
			BigTechEntityTypes.MINER,
			(MinerEntity miner) -> (
				(int)(miner.getDataTracker().get(MinerEntity.FUEL_FRACTION) * 15.0F)
			)
		);
	}
}