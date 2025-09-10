package builderb0y.bigtech.entities;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.joml.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.models.IntRng;
import builderb0y.bigtech.util.BigTechMath;

public class StormCloudEntityRenderer extends EntityRenderer<StormCloudEntity, StormCloudEntityRenderer.State> {

	public static final float
		QUAD_HORIZONTAL_DIAMETER = 8.0F,
		QUAD_HORIZONTAL_RADIUS   = QUAD_HORIZONTAL_DIAMETER * 0.5F,
		QUAD_VERTICAL_DIAMETER   = 4.0F,
		QUAD_VERTICAL_RADIUS     = QUAD_VERTICAL_DIAMETER * 0.5F;

	public StormCloudEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	public static double mod1(double value) {
		return value - Math.floor(value);
	}

	public static double mod8(double value) {
		return mod1(value * (1.0D / QUAD_HORIZONTAL_DIAMETER)) * QUAD_HORIZONTAL_DIAMETER;
	}

	public static int pack(int offsetX, int offsetZ) {
		return (offsetZ << 16) | (offsetX & 0xFFFF);
	}

	public static int unpackX(int packed) {
		return (int)(short)(packed);
	}

	public static int unpackZ(int packed) {
		return packed >> 16;
	}

	@Override
	public void render(State state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		//radiusSquared can be negative for like one tick when
		//blasting the cloud with a "less rainy" firework.
		if (state.radiusSquared <= 0) return;
		double originX = state.x - mod8(state.age * 0.03D); //match vanilla speed.
		double originZ = state.z;
		VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getDebugQuads());
		MatrixStack.Entry matrixEntry = matrices.peek();
		Int2FloatOpenHashMap brightnesses = new Int2FloatOpenHashMap((int)(state.radiusSquared * (4.0D / (QUAD_HORIZONTAL_DIAMETER * QUAD_HORIZONTAL_DIAMETER))));
		brightnesses.defaultReturnValue(1.0F);
		for (
			int offsetX = 1;
			this.drawTopBottom(
				state,
				matrixEntry,
				vertices,
				originX,
				originZ,
				offsetX,
				0,
				brightnesses
			);
			offsetX++
		) {
			for (
				int offsetZ = -1;
				this.drawTopBottom(
					state,
					matrixEntry,
					vertices,
					originX,
					originZ,
					offsetX,
					offsetZ,
					brightnesses
				);
				offsetZ--
			);
			for (
				int offsetZ = 1;
				this.drawTopBottom(
					state,
					matrixEntry,
					vertices,
					originX,
					originZ,
					offsetX,
					offsetZ,
					brightnesses
				);
				offsetZ++
			);
		}
		for (
			int offsetX = 0;
			this.drawTopBottom(
				state,
				matrixEntry,
				vertices,
				originX,
				originZ,
				offsetX,
				0,
				brightnesses
			);
			offsetX--
		) {
			for (
				int offsetZ = -1;
				this.drawTopBottom(
					state,
					matrixEntry,
					vertices,
					originX,
					originZ,
					offsetX,
					offsetZ,
					brightnesses
				);
				offsetZ--
			);
			for (
				int offsetZ = 1;
				this.drawTopBottom(
					state,
					matrixEntry,
					vertices,
					originX,
					originZ,
					offsetX,
					offsetZ,
					brightnesses
				);
				offsetZ++
			);
		}
		for (
			ObjectIterator<Int2FloatMap.Entry> iterator = brightnesses.int2FloatEntrySet().fastIterator();
			iterator.hasNext();
		) {
			Int2FloatMap.Entry entry = iterator.next();
			int offsetX = unpackX(entry.getIntKey());
			int offsetZ = unpackZ(entry.getIntKey());
			float selfBrightness = entry.getFloatValue();
			double x = originX + offsetX * QUAD_HORIZONTAL_DIAMETER;
			double z = originZ + offsetZ * QUAD_HORIZONTAL_DIAMETER;

			float otherBrightness, brightness, quadCenterX, quadCenterZ;
			otherBrightness = brightnesses.get(pack(offsetX - 1, offsetZ));
			if (otherBrightness == 1.0F) {
				brightness = (selfBrightness + otherBrightness) * 0.5F;
				quadCenterX = (float)(x - state.x);
				quadCenterZ = (float)(z - state.z);

				this.vertex(state, matrixEntry, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.9F);
				this.vertex(state, matrixEntry, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.9F);
				this.vertex(state, matrixEntry, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.9F);
				this.vertex(state, matrixEntry, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.9F);
			}

			otherBrightness = brightnesses.get(pack(offsetX, offsetZ - 1));
			if (otherBrightness == 1.0F) {
				brightness = (selfBrightness + otherBrightness) * 0.5F;
				quadCenterX = (float)(x - state.x);
				quadCenterZ = (float)(z - state.z);

				this.vertex(state, matrixEntry, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.8F);
				this.vertex(state, matrixEntry, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.8F);
				this.vertex(state, matrixEntry, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.8F);
				this.vertex(state, matrixEntry, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.8F);
			}

			otherBrightness = brightnesses.get(pack(offsetX + 1, offsetZ));
			if (otherBrightness == 1.0F) {
				brightness = (selfBrightness + otherBrightness) * 0.5F;
				quadCenterX = (float)(x - state.x);
				quadCenterZ = (float)(z - state.z);

				this.vertex(state, matrixEntry, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.9F);
				this.vertex(state, matrixEntry, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.9F);
				this.vertex(state, matrixEntry, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.9F);
				this.vertex(state, matrixEntry, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.9F);
			}

			otherBrightness = brightnesses.get(pack(offsetX, offsetZ + 1));
			if (otherBrightness == 1.0F) {
				brightness = (selfBrightness + otherBrightness) * 0.5F;
				quadCenterX = (float)(x - state.x);
				quadCenterZ = (float)(z - state.z);

				this.vertex(state, matrixEntry, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.8F);
				this.vertex(state, matrixEntry, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.8F);
				this.vertex(state, matrixEntry, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.8F);
				this.vertex(state, matrixEntry, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.8F);
			}
		}
	}

	public boolean drawTopBottom(
		State state,
		MatrixStack.Entry matrices,
		VertexConsumer vertices,
		double originX,
		double originZ,
		int offsetX,
		int offsetZ,
		Int2FloatMap brightnesses
	) {
		double x = originX + offsetX * QUAD_HORIZONTAL_DIAMETER;
		double z = originZ + offsetZ * QUAD_HORIZONTAL_DIAMETER;
		float brightness = state.getBrightness(x, z);
		if (brightness < 1.0F) {
			float quadCenterX = (float)(x - state.x);
			float quadCenterZ = (float)(z - state.z);

			this.vertex(state, matrices, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.7F);
			this.vertex(state, matrices, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness * 0.7F);
			this.vertex(state, matrices, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.7F);
			this.vertex(state, matrices, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, -QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness * 0.7F);

			this.vertex(state, matrices, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness);
			this.vertex(state, matrices, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ - QUAD_HORIZONTAL_RADIUS, brightness);
			this.vertex(state, matrices, vertices, quadCenterX + QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness);
			this.vertex(state, matrices, vertices, quadCenterX - QUAD_HORIZONTAL_RADIUS, +QUAD_VERTICAL_RADIUS, quadCenterZ + QUAD_HORIZONTAL_RADIUS, brightness);

			brightnesses.put(pack(offsetX, offsetZ), brightness);

			return true;
		}
		else {
			return false;
		}
	}

	public void vertex(State state, MatrixStack.Entry matrices, VertexConsumer vertices, float x, float y, float z, float brightness) {
		matrices.getPositionMatrix().transformPosition(x, y, z, state.scratchVector);
		x = state.scratchVector.x;
		y = state.scratchVector.y;
		z = state.scratchVector.z;
		vertices.vertex(x, y, z).color(state.cloudRed * brightness, state.cloudGreen * brightness, state.cloudBlue * brightness, 0.8F);
	}

	@Override
	public Box getBoundingBox(StormCloudEntity entity) {
		double radius = Math.sqrt(entity.radiusSquared);
		return new Box(
			entity.getX() - radius,
			entity.getY() - QUAD_VERTICAL_RADIUS,
			entity.getZ() - radius,
			entity.getX() + radius,
			entity.getY() + QUAD_VERTICAL_RADIUS,
			entity.getZ() + radius
		);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void updateRenderState(StormCloudEntity entity, State state, float tickProgress) {
		super.updateRenderState(entity, state, tickProgress);
		state.radiusSquared = ((double)(entity.radiusSquared)) - ((double)(tickProgress));
		state.seed = entity.seed;
		int cloudColor = MinecraftClient.getInstance().world.getCloudsColor(tickProgress);
		state.cloudRed   = ColorHelper.getRedFloat  (cloudColor);
		state.cloudGreen = ColorHelper.getGreenFloat(cloudColor);
		state.cloudBlue  = ColorHelper.getBlueFloat (cloudColor);
	}

	public static class State extends EntityRenderState {

		public double radiusSquared;
		public Vector3f scratchVector = new Vector3f();
		public int seed;
		public float cloudRed, cloudGreen, cloudBlue;

		public float getBrightness(double x, double z) {
			double fraction = BigTechMath.square(x - this.x) + BigTechMath.square(z - this.z);
			fraction = 1.0D - fraction / this.radiusSquared;
			if (fraction > 0.0D) {
				fraction *= fraction;
				double coefficient = this.radiusSquared / StormCloudEntity.REQUIRED_RADIUS_SQUARED_FOR_HALF_BRIGHTNESS_IN_CENTER;
				float noise = (
					+ 0.5F * this.getNoise(
						x * (0.125D / QUAD_HORIZONTAL_DIAMETER),
						z * (0.125D / QUAD_HORIZONTAL_DIAMETER)
					)
					+ 0.25F * this.getNoise(
						x * (0.25D / QUAD_HORIZONTAL_DIAMETER),
						z * (0.25D / QUAD_HORIZONTAL_DIAMETER)
					)
					+ 0.125F * this.getNoise(
						x * (0.5D / QUAD_HORIZONTAL_DIAMETER),
						z * (0.5D / QUAD_HORIZONTAL_DIAMETER)
					)
				);
				return (float)(1.0D / (fraction * coefficient * noise + 1.0D));
			}
			return 1.0F;
		}

		public float getNoise(double x, double z) {
			double
				floorX  = Math.floor(x),
				floorZ  = Math.floor(z),
				fracX   = x - floorX,
				fracZ   = z - floorZ;
			float
				smoothX = (float)(fracX * fracX * (3.0D - 2.0D * fracX)),
				smoothZ = (float)(fracZ * fracZ * (3.0D - 2.0D * fracZ));
			int
				iFloorX = (int)(floorX),
				iFloorZ = (int)(floorZ),
				iCeilX  = iFloorX + 1,
				iCeilZ  = iFloorZ + 1;

			return MathHelper.lerp(
				smoothX,
				MathHelper.lerp(
					smoothZ,
					IntRng.toPositiveFloat(IntRng.permute(this.seed, iFloorX, iFloorZ)),
					IntRng.toPositiveFloat(IntRng.permute(this.seed, iFloorX, iCeilZ))
				),
				MathHelper.lerp(
					smoothZ,
					IntRng.toPositiveFloat(IntRng.permute(this.seed, iCeilX, iFloorZ)),
					IntRng.toPositiveFloat(IntRng.permute(this.seed, iCeilX, iCeilZ))
				)
			);
		}
	}
}