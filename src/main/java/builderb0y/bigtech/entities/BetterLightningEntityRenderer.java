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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
		VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.lightning);
		Matrix4f matrix = matrices.peek().positionMatrix;
		RandomGenerator random = new SplittableRandom(entity.seed);
		Vec3d camera = MinecraftClient.getInstance().gameRenderer.camera.pos;
		this.recursiveGenerateLightning(
			buffer,
			matrix,
			camera.x - entity.x,
			camera.y - entity.y,
			camera.z - entity.z,
			random,
			random.nextDouble(-64.0D, 64.0D),
			128.0D,
			random.nextDouble(-64.0D, 64.0D),
			0.0F,
			0.0D,
			0.0D,
			0.0D,
			1.0F,
			8,
			0.5D,
			entity.age + tickDelta
		);
	}

	public void recursiveGenerateLightning(
		VertexConsumer buffer,
		Matrix4f matrix,
		double cameraX,
		double cameraY,
		double cameraZ,
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
		float age
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
			this.recursiveGenerateLightning(
				buffer,
				matrix,
				cameraX,
				cameraY,
				cameraZ,
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
				age
			);
			this.recursiveGenerateLightning(
				buffer,
				matrix,
				cameraX,
				cameraY,
				cameraZ,
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
				age
			);
			this.recursiveGenerateLightning(
				buffer,
				matrix,
				cameraX,
				cameraY,
				cameraZ,
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
				age
			);
		}
		else { //subdivision <= 0
			if (startFrac >= age / 8.0F) return;
			float decay = Math.max(age - 8.0F, 0.0F) * 0.125F;
			double renderedThickness = thickness - decay * 0.5F;
			if (renderedThickness < 0.0D) return;

			double dx = endX - startX;
			double dy = endY - startY;
			double dz = endZ - startZ;
			double cx = cameraX - startX;
			double cy = cameraY - startY;
			double cz = cameraZ - startZ;
			//align lightning quads so they face the player.
			//we do this by taking the cross product of
			//(end - start) and (camera - start)
			//to get a vector which is on an axis parallel to the screen.
			//then, we just offset the vertices along this axis.

			//matrix:
			//[ x  y  z]
			//[dx dy dz]
			//[cx cy cz]
			double crossX = dy * cz - dz * cy;
			double crossY = dz * cx - dx * cz;
			double crossZ = dx * cy - dy * cx;
			double currentLength = MathHelper.magnitude(crossX, crossY, crossZ);
			double scalar = renderedThickness / currentLength;
			crossX *= scalar;
			crossY *= scalar;
			crossZ *= scalar;

			//2 quads are emitted per lightning segment.
			//one on one side of the line connecting start and end,
			//and the other on the other side of that line.
			//the central vertices have a white color and 50% opacity.
			//the outer vertices have a blue-ish color and are fully transparent.
			//this allows a nice gradient to be constructed for the lightning
			//which is brightest in the center, and fades out to the sides.
			buffer.vertex(matrix, (float)(startX + crossX), (float)(startY + crossY), (float)(startZ + crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F).next();
			buffer.vertex(matrix, (float)(  endX + crossX), (float)(  endY + crossY), (float)(  endZ + crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F).next();
			buffer.vertex(matrix, (float)(  endX         ), (float)(  endY         ), (float)(  endZ         )).color(1.0F, 1.0F,  1.0F, 0.5F).next();
			buffer.vertex(matrix, (float)(startX         ), (float)(startY         ), (float)(startZ         )).color(1.0F, 1.0F,  1.0F, 0.5F).next();

			buffer.vertex(matrix, (float)(startX         ), (float)(startY         ), (float)(startZ         )).color(1.0F, 1.0F,  1.0F, 0.5F).next();
			buffer.vertex(matrix, (float)(  endX         ), (float)(  endY         ), (float)(  endZ         )).color(1.0F, 1.0F,  1.0F, 0.5F).next();
			buffer.vertex(matrix, (float)(  endX - crossX), (float)(  endY - crossY), (float)(  endZ - crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F).next();
			buffer.vertex(matrix, (float)(startX - crossX), (float)(startY - crossY), (float)(startZ - crossZ)).color(0.0F, 0.25F, 0.5F, 0.0F).next();
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public Identifier getTexture(LightningEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE; //same thing LightningEntityRenderer returns.
	}
}