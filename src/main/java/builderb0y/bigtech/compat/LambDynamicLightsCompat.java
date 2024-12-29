package builderb0y.bigtech.compat;

import dev.lambdaurora.lambdynlights.api.DynamicLightsContext;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.entity.luminance.EntityLuminance;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import net.minecraft.entity.Entity;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.entities.MinerEntity;

public class LambDynamicLightsCompat implements DynamicLightsInitializer {

	@Override
	public void onInitializeDynamicLights(DynamicLightsContext context) {
		enum MinerEntityLuminance implements EntityLuminance {
			INSTANCE;

			public static final Type TYPE = Type.registerSimple(BigTechMod.modID("miner"), INSTANCE);

			@Override
			public @NotNull Type type() {
				return TYPE;
			}

			@Override
			public @Range(from = 0L, to = 15L) int getLuminance(ItemLightSourceManager manager, Entity entity) {
				return entity instanceof MinerEntity miner ? (int)(miner.getDataTracker().get(MinerEntity.FUEL_FRACTION) * 15.0F) : 0;
			}
		}
		MinerEntityLuminance.INSTANCE.getClass();
	}
}