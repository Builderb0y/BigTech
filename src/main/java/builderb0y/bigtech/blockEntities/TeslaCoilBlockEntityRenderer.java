package builderb0y.bigtech.blockEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import builderb0y.bigtech.blockEntities.TeslaCoilBlockEntity.Target;
import builderb0y.fractallightning.LightningRenderer;
import builderb0y.fractallightning.LightningRendererImpl;

@Environment(EnvType.CLIENT)
public class TeslaCoilBlockEntityRenderer implements BlockEntityRenderer<TeslaCoilBlockEntity> {

	public TeslaCoilBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

	@Override
	public void render(
		TeslaCoilBlockEntity blockEntity,
		float tickProgress,
		MatrixStack matrices,
		VertexConsumerProvider vertexConsumers,
		int light,
		int overlay,
		Vec3d camera
	) {
		VertexConsumer buffer = vertexConsumers.getBuffer(LightningRenderer.LIGHTNING_LAYER);
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		BlockPos blockEntityPos = blockEntity.getPos();
		for (int index = 0; index < 8; index++) {
			Target target = blockEntity.targets[index];
			if (target == null) continue;
			new LightningRendererImpl(matrix, buffer, index + tickProgress) {

				@Override
				public float adjustWidth(float width, float startFrac, float endFrac) {
					return width - this.age * eighthWidth;
				}
			}
			.generatePoints(
				target.seed,
				(float)(target.startX - blockEntityPos.getX()),
				(float)(target.startY - blockEntityPos.getY()),
				(float)(target.startZ - blockEntityPos.getZ()),
				(float)(target.endX   - blockEntityPos.getX()),
				(float)(target.endY   - blockEntityPos.getY()),
				(float)(target.endZ   - blockEntityPos.getZ()),
				LightningRendererImpl.baseWidth
			);
		}
	}
}