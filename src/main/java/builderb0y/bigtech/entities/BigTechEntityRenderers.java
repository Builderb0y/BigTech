package builderb0y.bigtech.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class BigTechEntityRenderers {

	public static void init() {
		EntityRendererRegistry.register(EntityType.LIGHTNING_BOLT, BetterLightningEntityRenderer::new);
		EntityRendererRegistry.register(BigTechEntityTypes.MINER, MinerEntityRenderer::new);
		EntityRendererRegistry.register(BigTechEntityTypes.MAGNETIC_ARROW, MagneticArrowEntityRenderer::new);
	}
}