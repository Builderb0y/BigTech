package builderb0y.bigtech.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class BigTechEntityRenderers {

	public static void init() {
		EntityRendererRegistry.register(BigTechEntityTypes.MINER,                    MinerEntityRenderer::new);
		EntityRendererRegistry.register(BigTechEntityTypes.MAGNETIC_ARROW,   MagneticArrowEntityRenderer::new);
		EntityRendererRegistry.register(BigTechEntityTypes.STORM_CLOUD,         StormCloudEntityRenderer::new);
		EntityRendererRegistry.register(BigTechEntityTypes.STORM_LIGHTNING, StormLightningEntityRenderer::new);
	}
}