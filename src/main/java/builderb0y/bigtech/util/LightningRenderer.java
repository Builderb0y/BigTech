package builderb0y.bigtech.util;

import java.util.random.RandomGenerator;

import net.fabricmc.loader.api.FabricLoader;
import org.joml.Matrix4f;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.BigTechMod;

public class LightningRenderer {

	public static RenderLayer LIGHTNING_LAYER;
	static {
		RenderLayer layer = RenderLayer.getLightning();
		got:
		if (FabricLoader.getInstance().isModLoaded("iris")) {
			try {
				layer = Class.forName("net.irisshaders.iris.pathways.LightningHandler").getDeclaredField("IRIS_LIGHTNING").get(null).as();
				BigTechMod.LOGGER.info("Using new iris lightning renderer.");
				break got;
			}
			catch (Exception ignored) {}
			try {
				layer = Class.forName("net.coderbot.iris.pipeline.LightningHandler").getDeclaredField("IRIS_LIGHTNING").get(null).as();
				BigTechMod.LOGGER.info("Using old iris lightning renderer.");
				break got;
			}
			catch (Exception ignored) {}
		}
		LIGHTNING_LAYER = layer;
	}

	public static void generatePoints(
		RandomGenerator random,
		double startX,
		double startY,
		double startZ,
		double endX,
		double endY,
		double endZ,
		int subdivision,
		double thickness,
		LightningRenderConsumer action
	) {
		generatePointsRecursive(
			random,
			startX,
			startY,
			startZ,
			0.0F,
			endX,
			endY,
			endZ,
			1.0F,
			subdivision,
			thickness,
			action
		);
	}

	public static void generatePointsRecursive(
		RandomGenerator random,
		double startX,
		double startY,
		double startZ,
		float startFrac,
		double endX,
		double endY,
		double endZ,
		float endFrac,
		int subdivision,
		double thickness,
		LightningRenderConsumer action
	) {
		if (subdivision > 0) {
			//half way between start and end.
			double midX = (startX + endX) * 0.5D;
			double midY = (startY + endY) * 0.5D;
			double midZ = (startZ + endZ) * 0.5D;
			float midFrac = (startFrac + endFrac) * 0.5F;
			//normally I'd generate a point on or in a unit sphere,
			//so that the direction is not biased in any direction,
			//but I think a cube is fine in this case.
			//the bias is not noticeable at all to me.
			double offsetX = random.nextDouble(-1.0D, +1.0D);
			double offsetY = random.nextDouble(-1.0D, +1.0D);
			double offsetZ = random.nextDouble(-1.0D, +1.0D);
			//now project the cube onto the plane defined by the point
			//(start + end) / 2, and the normal vector (end - start).
			double dx = endX - startX;
			double dy = endY - startY;
			double dz = endZ - startZ;
			double normalMagnitudeSquared = MathHelper.squaredMagnitude(dx, dy, dz);
			{
				double dot = (offsetX * dx + offsetY * dy + offsetZ * dz) / normalMagnitudeSquared;
				offsetX -= dx * dot;
				offsetY -= dy * dot;
				offsetZ -= dz * dot;

				//scale offset to be 0.1875x the length of (end - start).
				double currentLengthSquared = MathHelper.squaredMagnitude(offsetX, offsetY, offsetZ);
				//sqrt(a) / sqrt(b) = sqrt(a / b).
				double scalar = 0.1875D * Math.sqrt(normalMagnitudeSquared / currentLengthSquared);
				offsetX *= scalar;
				offsetY *= scalar;
				offsetZ *= scalar;
			}
			//offset midpoint.
			midX += offsetX;
			midY += offsetY;
			midZ += offsetZ;

			//do all that over again to generate the branch.
			double branchX = midX;
			double branchY = midY;
			double branchZ = midZ;

			offsetX = random.nextDouble(-1.0D, +1.0D);
			offsetY = random.nextDouble(-1.0D, +1.0D);
			offsetZ = random.nextDouble(-1.0D, +1.0D);
			{
				double dot = (offsetX * dx + offsetY * dy + offsetZ * dz) / normalMagnitudeSquared;
				offsetX -= dx * dot;
				offsetY -= dy * dot;
				offsetZ -= dz * dot;

				//scale offset to be 0.375x the length of (end - start).
				double currentLengthSquared = MathHelper.squaredMagnitude(offsetX, offsetY, offsetZ);
				//sqrt(a) / sqrt(b) = sqrt(a / b).
				double scalar = 0.375D * Math.sqrt(normalMagnitudeSquared / currentLengthSquared);
				offsetX *= scalar;
				offsetY *= scalar;
				offsetZ *= scalar;
			}
			branchX += offsetX;
			branchY += offsetY;
			branchZ += offsetZ;

			//now generate child branches.
			generatePointsRecursive(
				random,
				startX,
				startY,
				startZ,
				startFrac,
				midX,
				midY,
				midZ,
				midFrac,
				subdivision - 1,
				thickness,
				action
			);
			generatePointsRecursive(
				random,
				midX,
				midY,
				midZ,
				midFrac,
				endX,
				endY,
				endZ,
				endFrac,
				subdivision - 1,
				thickness,
				action
			);
			generatePointsRecursive(
				random,
				midX,
				midY,
				midZ,
				midFrac,
				branchX,
				branchY,
				branchZ,
				endFrac,
				subdivision - 1,
				thickness * 0.5D,
				action
			);
		}
		else { //subdivision == 0
			action.accept(
				startX,
				startY,
				startZ,
				startFrac,
				endX,
				endY,
				endZ,
				endFrac,
				thickness
			);
		}
	}

	public static void addQuads(
		VertexConsumer buffer,
		Matrix4f matrix,
		double cameraX,
		double cameraY,
		double cameraZ,
		double startX,
		double startY,
		double startZ,
		double endX,
		double endY,
		double endZ,
		double thickness
	) {
		//align lightning quads so they face the player.
		//we do this by taking the cross product of
		//(end - start) and (camera - start)
		//to get a vector which is on an axis parallel to the screen.
		//then, we just offset the vertices along this axis.
		double dx = endX - startX;
		double dy = endY - startY;
		double dz = endZ - startZ;
		double cx = cameraX - startX;
		double cy = cameraY - startY;
		double cz = cameraZ - startZ;
		double crossX = dy * cz - dz * cy;
		double crossY = dz * cx - dx * cz;
		double crossZ = dx * cy - dy * cx;
		double currentLength = MathHelper.magnitude(crossX, crossY, crossZ);
		double scalar = thickness / currentLength;
		crossX *= scalar;
		crossY *= scalar;
		crossZ *= scalar;

		buffer
		.vertex(matrix, (float)(startX + crossX), (float)(startY + crossY), (float)(startZ + crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F)
		.vertex(matrix, (float)(  endX + crossX), (float)(  endY + crossY), (float)(  endZ + crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F)
		.vertex(matrix, (float)(  endX         ), (float)(  endY         ), (float)(  endZ         )).color(1.0F, 1.0F,  1.0F, 0.5F)
		.vertex(matrix, (float)(startX         ), (float)(startY         ), (float)(startZ         )).color(1.0F, 1.0F,  1.0F, 0.5F)

		.vertex(matrix, (float)(startX         ), (float)(startY         ), (float)(startZ         )).color(1.0F, 1.0F,  1.0F, 0.5F)
		.vertex(matrix, (float)(  endX         ), (float)(  endY         ), (float)(  endZ         )).color(1.0F, 1.0F,  1.0F, 0.5F)
		.vertex(matrix, (float)(  endX - crossX), (float)(  endY - crossY), (float)(  endZ - crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F)
		.vertex(matrix, (float)(startX - crossX), (float)(startY - crossY), (float)(startZ - crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F);
	}

	public static interface LightningRenderConsumer {

		public abstract void accept(
			double startX,
			double startY,
			double startZ,
			float startFrac,
			double endX,
			double endY,
			double endZ,
			float endFrac,
			double thickness
		);
	}
}