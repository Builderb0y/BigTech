package builderb0y.bigtech.blockEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import builderb0y.bigtech.items.FunctionalItems;

@Environment(EnvType.CLIENT)
public class MicroProcessorBlockEntityRenderer implements BlockEntityRenderer<MicroProcessorBlockEntity> {

	public MicroProcessorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

	@Override
	public void render(
		MicroProcessorBlockEntity processor,
		float tickProgress,
		MatrixStack matrices,
		VertexConsumerProvider vertexConsumers,
		int light,
		int overlay,
		Vec3d cameraPos
	) {
		if (!processor.name.getString().isBlank()) {
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			HitResult crosshairTarget = MinecraftClient.getInstance().crosshairTarget;
			if (
				(
					player.getMainHandStack().isOf(FunctionalItems.MAGNIFYING_GLASS) ||
					player.getOffHandStack().isOf(FunctionalItems.MAGNIFYING_GLASS)
				)
				&& crosshairTarget instanceof BlockHitResult blockTarget
				&& blockTarget.getBlockPos().equals(processor.getPos())
			) {
				matrices.push();
				matrices.translate(0.5F, 0.75F, 0.5F);
				matrices.multiply(MinecraftClient.getInstance().gameRenderer.getCamera().getRotation());
				matrices.scale(0.025F, -0.025F, 0.025F);
				Matrix4f matrix = matrices.peek().getPositionMatrix();
				TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
				int backgroundColor = (int)(MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
				textRenderer.draw(
					processor.name,
					textRenderer.getWidth(processor.name) * -0.5F,
					0,
					-1,
					false,
					matrix,
					vertexConsumers,
					TextRenderer.TextLayerType.NORMAL,
					backgroundColor,
					LightmapTextureManager.applyEmission(light, 2)
				);
				matrices.pop();
			}
		}
	}

	@Override
	public int getRenderDistance() {
		return 16;
	}
}