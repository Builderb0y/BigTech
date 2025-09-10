package builderb0y.bigtech.entities;

import org.joml.Matrix4f;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

import builderb0y.fractallightning.LightningRenderer;
import builderb0y.fractallightning.LightningRendererImpl;

public class StormLightningEntityRenderer extends EntityRenderer<StormLightningEntity, StormLightningEntityRenderer.State> {

	public StormLightningEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(State state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		VertexConsumer buffer = vertexConsumers.getBuffer(LightningRenderer.LIGHTNING_LAYER);
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		new LightningRendererImpl(matrix, buffer, state.age).generatePoints(
			state.seed,
			(float)(state.startX - state.x),
			(float)(state.startY - state.y),
			(float)(state.startZ - state.z),
			0.0F,
			0.0F,
			0.0F,
			LightningRendererImpl.baseWidth
		);
	}

	@Override
	public boolean canBeCulled(StormLightningEntity entity) {
		return false;
	}

	@Override
	public void updateRenderState(StormLightningEntity entity, State state, float tickProgress) {
		super.updateRenderState(entity, state, tickProgress);
		state.seed = entity.seed;
		state.startX = entity.getStartX();
		state.startY = entity.getStartY();
		state.startZ = entity.getStartZ();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	public static class State extends EntityRenderState {

		public long seed;
		public double startX, startY, startZ;
	}
}