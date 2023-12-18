package builderb0y.bigtech.compat;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;

import builderb0y.bigtech.entities.BigTechEntityTypes;
import builderb0y.bigtech.entities.MinerEntity;

public class LambDynamicLightsCompat implements DynamicLightsInitializer {

	@Override
	public void onInitializeDynamicLights() {
		DynamicLightHandlers.registerDynamicLightHandler(
			BigTechEntityTypes.MINER,
			(MinerEntity miner) -> (
				miner.controllingPassenger != null
				? (int)(miner.dataTracker.get(MinerEntity.FUEL_FRACTION) * 15.0F)
				: 0
			)
		);
	}
}