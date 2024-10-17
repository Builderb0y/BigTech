package builderb0y.bigtech.entities;

import java.util.SplittableRandom;
import java.util.random.RandomGenerator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import builderb0y.bigtech.config.BigTechConfig;
import builderb0y.bigtech.util.LightningRenderer;

@Environment(EnvType.CLIENT)
public class BetterLightningEntityRenderer extends EntityRenderer<LightningEntity> {

	public BetterLightningEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(
		LightningEntity entity,
		float yaw,
		float tickDelta,
		MatrixStack matrices,
		VertexConsumerProvider vertexConsumers,
		int light
	) {
		VertexConsumer buffer = vertexConsumers.getBuffer(LightningRenderer.LIGHTNING_LAYER);
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		//use java.util.SplittableRandom over java.util.Random
		//because it's faster due to the non-atomic seed.
		RandomGenerator random = new SplittableRandom(entity.seed);
		Vec3d camera = MinecraftClient.getInstance().gameRenderer.getCamera().getPos().subtract(entity.getPos());
		float age = entity.age + tickDelta;
		LightningRenderer.generatePoints(
			random,
			random.nextDouble(-64.0D,  64.0D),
			random.nextDouble(128.0D, 192.0D),
			random.nextDouble(-64.0D,  64.0D),
			0.0D,
			0.0D,
			0.0D,
			BigTechConfig.INSTANCE.get().client.lightningRendererQuality,
			0.5D,
			(
				double startX,
				double startY,
				double startZ,
				float startFrac,
				double endX,
				double endY,
				double endZ,
				float endFrac,
				double thickness
			) -> {
				if (startFrac >= age * 0.125F) return;
				float decay = Math.max(age - 8.0F, 0.0F) * 0.125F;
				double renderedThickness = thickness - decay * 0.5F;
				if (renderedThickness <= 0.0D) return;
				LightningRenderer.addQuads(
					buffer,
					matrix,
					camera.x,
					camera.y,
					camera.z,
					startX,
					startY,
					startZ,
					endX,
					endY,
					endZ,
					renderedThickness
				);
			}
		);
	}

	@Override
	@SuppressWarnings("deprecation")
	public Identifier getTexture(LightningEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE; //same thing LightningEntityRenderer returns.
	}
}