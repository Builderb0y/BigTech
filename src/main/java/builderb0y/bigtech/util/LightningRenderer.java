package builderb0y.bigtech.util;

import java.util.random.RandomGenerator;

import org.joml.Matrix4f;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.MathHelper;

public class LightningRenderer {

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
			//generate a point on the unit sphere.
			double unitX, unitY, unitZ;
			{
				double x = random.nextDouble(-1.0D, 1.0D);
				double y = random.nextDouble(Math.PI * 2.0D);
				double r = Math.sqrt(1.0D - x * x);
				unitX = Math.cos(y) * r;
				unitY = Math.sin(y) * r;
				unitZ = x;
			}
			//take the cross product between (end - start) and unit.
			//this will allow us to offset mid in a direction that is perpendicular to (end - start).
			//matrix:
			//[ x  y  z]
			//[dx dy dz]
			//[ux uy uz]
			double crossX, crossY, crossZ;
			{
				double dx = endX - startX;
				double dy = endY - startY;
				double dz = endZ - startZ;
				crossX = dy * unitZ - dz * unitY;
				crossY = dz * unitX - dx * unitZ;
				crossZ = dx * unitY - dy * unitX;
				//scale cross product to be half the length of (end - start).
				double currentLengthSquared = MathHelper.squaredMagnitude(crossX, crossY, crossZ);
				double desiredLengthSquared = MathHelper.squaredMagnitude(dx, dy, dz);
				//sqrt(a) / sqrt(b) = sqrt(a / b).
				double scalar = 0.25D / Math.sqrt(desiredLengthSquared / currentLengthSquared);
				crossX *= scalar;
				crossY *= scalar;
				crossZ *= scalar;
			}
			//offset midpoint.
			midX += crossX;
			midY += crossY;
			midZ += crossZ;

			//do all that over again to generate the branch.
			double branchX = midX;
			double branchY = midY;
			double branchZ = midZ;

			{
				double x = random.nextDouble(-1.0D, 1.0D);
				double y = random.nextDouble(Math.PI * 2.0D);
				double r = Math.sqrt(1.0D - x * x);
				unitX = Math.cos(y) * r;
				unitY = Math.sin(y) * r;
				unitZ = x;
			}

			{
				double dx = endX - startX;
				double dy = endY - startY;
				double dz = endZ - startZ;
				crossX = dy * unitZ - dz * unitY;
				crossY = dz * unitX - dx * unitZ;
				crossZ = dx * unitY - dy * unitX;
				//scale cross product to be half the length of (end - start).
				double currentLengthSquared = MathHelper.squaredMagnitude(crossX, crossY, crossZ);
				double desiredLengthSquared = MathHelper.squaredMagnitude(dx, dy, dz);
				//sqrt(a) / sqrt(b) = sqrt(a / b).
				double scalar = 0.5D / Math.sqrt(desiredLengthSquared / currentLengthSquared);
				crossX *= scalar;
				crossY *= scalar;
				crossZ *= scalar;
			}
			branchX += crossX;
			branchY += crossY;
			branchZ += crossZ;

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

		buffer.vertex(matrix, (float)(startX + crossX), (float)(startY + crossY), (float)(startZ + crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F).next();
		buffer.vertex(matrix, (float)(  endX + crossX), (float)(  endY + crossY), (float)(  endZ + crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F).next();
		buffer.vertex(matrix, (float)(  endX         ), (float)(  endY         ), (float)(  endZ         )).color(1.0F, 1.0F,  1.0F, 0.5F).next();
		buffer.vertex(matrix, (float)(startX         ), (float)(startY         ), (float)(startZ         )).color(1.0F, 1.0F,  1.0F, 0.5F).next();

		buffer.vertex(matrix, (float)(startX         ), (float)(startY         ), (float)(startZ         )).color(1.0F, 1.0F,  1.0F, 0.5F).next();
		buffer.vertex(matrix, (float)(  endX         ), (float)(  endY         ), (float)(  endZ         )).color(1.0F, 1.0F,  1.0F, 0.5F).next();
		buffer.vertex(matrix, (float)(  endX - crossX), (float)(  endY - crossY), (float)(  endZ - crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F).next();
		buffer.vertex(matrix, (float)(startX - crossX), (float)(startY - crossY), (float)(startZ - crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F).next();
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