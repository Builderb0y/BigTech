package builderb0y.bigtech.blockEntities;

import java.util.SplittableRandom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import builderb0y.bigtech.blockEntities.TeslaCoilBlockEntity.Target;
import builderb0y.bigtech.util.LightningRenderer;

@Environment(EnvType.CLIENT)
public class TeslaCoilBlockEntityRenderer implements BlockEntityRenderer<TeslaCoilBlockEntity> {

	public TeslaCoilBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

	@Override
	public void render(
		TeslaCoilBlockEntity blockEntity,
		float tickDelta,
		MatrixStack matrices,
		VertexConsumerProvider vertexConsumers,
		int light,
		int overlay
	) {
		VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.lightning);
		Matrix4f matrix = matrices.peek().positionMatrix;
		Vec3d camera = MinecraftClient.getInstance().gameRenderer.camera.pos.subtract(blockEntity.pos.toCenterPos());
		for (int index = 0; index < 8; index++) {
			Target target = blockEntity.targets[index];
			if (target == null) continue;
			float age = index + tickDelta;
			LightningRenderer.generatePoints(
				new SplittableRandom(target.seed),
				target.startX - blockEntity.pos.x,
				target.startY - blockEntity.pos.y,
				target.startZ - blockEntity.pos.z,
				target.endX   - blockEntity.pos.x,
				target.endY   - blockEntity.pos.y,
				target.endZ   - blockEntity.pos.z,
				6,
				0.0625D,
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
					double renderedThickness = thickness - age * (0.125F * 0.0625F);
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
	}
}