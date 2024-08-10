package builderb0y.bigtech.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import builderb0y.bigtech.BigTechMod;

@Environment(EnvType.CLIENT)
public class MinerEntityRenderer extends EntityRenderer<MinerEntity> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/entity/miner.png");
	public static final float
		TEXTURE_SCALE_X = 1.0F / 128.0F,
		TEXTURE_SCALE_Y = 1.0F / 128.0F,
		MODEL_SCALE     = 1.0F /  16.0F;

	public MinerEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(MinerEntity miner, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		super.render(miner, yaw, tickDelta, matrices, vertexConsumers, light);
		matrices.push();
		try {
			matrices.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
			matrices.peek().getPositionMatrix().rotateY(yaw * ((float)(Math.PI / -180.0F)));
			matrices.peek().  getNormalMatrix().rotateY(yaw * ((float)(Math.PI / -180.0F)));

			//copy-pasted from MinecartEntityRenderer.
			float ticks    = miner.getDamageWobbleTicks() - tickDelta;
			float strength = miner.getDamageWobbleStrength() - tickDelta;
			if (strength < 0.0F) {
				strength = 0.0F;
			}

			if (ticks > 0.0F) {
				//change X axis to Z axis.
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(ticks) * ticks * strength / 10.0F * miner.getDamageWobbleSide()));
			}
			//end of copy-pasted region.

			EntityRenderHelper helper = (
				new EntityRenderHelper()
				.vertexConsumer(
					vertexConsumers.getBuffer(
						RenderLayer.getEntityCutout(
							this.getTexture(miner)
						)
					)
				)
				.transform(matrices.peek())
				.textureScale(TEXTURE_SCALE_X, TEXTURE_SCALE_Y)
				.lightmap(light)
			);
			this.renderMainInner(helper);
			if (!miner.isThePlayerRiding0() || MinecraftClient.getInstance().options.getPerspective() != Perspective.FIRST_PERSON) {
				this.renderMainOuter(helper);
				this.renderScoop(helper);
				this.renderNumbers(helper, miner.number);
				if (miner.getControllingPassenger() != null) {
					float brightness = miner.getDataTracker().get(MinerEntity.FUEL_FRACTION);
					if (brightness > 0.0F) {
						this.renderLights(
							helper.vertexConsumer(
								vertexConsumers.getBuffer(
									RenderLayer.getEntityTranslucentEmissive(
										this.getTexture(miner)
									)
								)
							),
							brightness
						);
					}
				}
			}
		}
		finally {
			matrices.pop();
		}
	}

	public void renderMainOuter(EntityRenderHelper helper) {
		//main body
		helper.quad( 14,  14, -14, -14,    31, 31, 31, 31,      8, -15, -15,   8,     29,  57,  31,  54,     0,  1,  0); //top
		helper.quad(-14, -14,  14,  14,     1,  1,  1,  1,      8, -15, -15,   8,      0,  28,  31,  54,     0, -1,  0); //bottom
		helper.quad(-14, -14,  14,  14,    31,  1,  1, 31,      8,   8,   8,   8,     24,  52,   0,  30,     0,  0,  1); //front
		helper.quad( 14,  14, -14, -14,    31,  1,  1, 31,    -15, -15, -15, -15,     77, 105,   0,  30,     0,  0, -1); //back
		helper.quad( 14,  14,  14,  14,    31,  7,  7, 31,      8,   8, -15, -15,     53,  76,   0,  24,     1,  0,  0); //left
		helper.quad(-14, -14, -14, -14,    31,  7,  7, 31,    -15, -15,   8,   8,      0,  23,   0,  24,    -1,  0,  0); //right

		//left wheels
		helper.quad( 15,  15,  15,  15,     7,  0,  0,  7,      8,   8, -15, -15,     77, 100, 111, 118,     1,  0,  0); //left
		helper.quad( 15,  15,  14,  14,     7,  0,  0,  7,    -15, -15, -15, -15,    101, 102, 111, 118,     0,  0, -1); //back
		helper.quad( 14,  14,  14,  14,     1,  0,  0,  1,    -15, -15,   8,   8,    103, 126, 117, 118,    -1,  0,  0); //right
		helper.quad( 14,  14,  15,  15,     7,  0,  0,  7,      8,   8,   8,   8,    127, 128, 111, 118,     0,  0,  1); //front
		helper.quad( 14,  15,  15,  14,     7,  7,  7,  7,      8,   8, -15, -15,     77, 100, 109, 110,     0,  1,  0); //top
		helper.quad( 15,  14,  14,  15,     0,  0,  0,  0,      8,   8, -15, -15,    103, 126, 109, 110,     0, -1,  0); //bottom

		//right wheels
		helper.quad(-15, -15, -15, -15,     7,  0,  0,  7,    -15, -15,   8,   8,     77, 100, 121, 128,    -1,  0,  0); //right
		helper.quad(-15, -15, -14, -14,     7,  0,  0,  7,      8,   8,   8,   8,    101, 102, 121, 128,     0,  0,  1); //front
		helper.quad(-14, -14, -14, -14,     1,  0,  0,  1,      8,   8, -15, -15,    103, 126, 127, 128,     1,  0,  0); //left
		helper.quad(-14, -14, -15, -15,     7,  0,  0,  7,    -15, -15, -15, -15,    127, 128, 121, 128,     0,  0, -1); //back
		helper.quad(-14, -15, -15, -14,     7,  7,  7,  7,    -15, -15,   8,   8,     77, 100, 119, 120,     0,  1,  0); //top
		helper.quad(-15, -14, -14, -15,     0,  0,  0,  0,    -15, -15,   8,   8,    103, 126, 119, 120,     0, -1,  0); //bottom
	}

	public void renderMainInner(EntityRenderHelper helper) {
		//main interior
		helper.quad( 14,  14, -14, -14,    31,  1,  1, 31,      8,   8,   8,   8,     24,  52,  55,  85,     0,  0, -1); //front
		helper.quad( 14,  14,  14,  14,    31,  1,  1, 31,    -15, -15,   8,   8,      0,  23,  55,  85,    -1,  0,  0); //left door
		helper.quad(-14, -14, -14, -14,    31,  1,  1, 31,      8,   8, -15, -15,     53,  76,  55,  85,     1,  0,  0); //right door
		helper.quad(-14, -14,  14,  14,    31, 31, 31, 31,      8, -15, -15,   8,     58,  86,  31,  54,     0, -1,  0); //ceiling
		helper.quad(-14, -14,  14,  14,    31, 21, 21, 31,    -15, -15, -15, -15,     97, 125,  31,  41,     0,  0,  1); //back wall
		helper.quad(-14, -14,  14,  14,    21, 21, 21, 21,    -15, -11, -11, -15,     97, 125,  42,  46,     0,  1,  0); //top of chair
		helper.quad(-14, -14,  14,  14,    21,  9,  9, 21,    -11, -11, -11, -11,     97, 125,  47,  59,     0,  0,  1); //front of chair
		helper.quad(-14, -14,  14,  14,     9,  9,  9,  9,    -11,  -3,  -3, -11,     97, 125,  60,  68,     0,  1,  0); //top of seat
		helper.quad(-14, -14,  14,  14,     9,  1,  1,  9,     -3,  -3,  -3,  -3,     97, 125,  69,  77,     0,  0,  1); //front of foot area
		helper.quad(-14, -14,  14,  14,     1,  1,  1,  1,     -3,   8,   8,  -3,     97, 125,  78,  89,     0,  1,  0); //bottom of foot area
		helper.quad( -2,   2,   2,  -2,    13, 13, 13, 13,      8,   8, -11, -11,     77,  96,  68,  72,     0,  1,  0); //top of arm rest
		helper.quad(  2,   2,   2,   2,    13,  1,  1, 13,      8,   8, -11, -11,     77,  96,  73,  85,     1,  0,  0); //left side of arm rest
		helper.quad( -2,  -2,  -2,  -2,     1, 13, 13,  1,      8,   8, -11, -11,     77,  96,  55,  67,    -1,  0,  0); //right side of arm rest

		//left head rest, pillow
		helper.quad(  5,   5,  11,  11,    27, 23, 23, 27,    -11, -11, -11, -11,     88,  94, 101, 105,     0,  0,  1); //front
		helper.quad( 11,  11,  11,  11,    27, 23, 23, 27,    -11, -11, -14, -14,     95,  98, 101, 105,     1,  0,  0); //left
		helper.quad( 11,  11,   5,   5,    27, 23, 23, 27,    -14, -14, -14, -14,     77,  83, 101, 105,     0,  0, -1); //back
		helper.quad(  5,   5,   5,   5,    27, 23, 23, 27,    -14, -14, -11, -11,     84,  87, 101, 105,    -1,  0,  0); //right
		helper.quad(  5,   5,  11,  11,    27, 27, 27, 27,    -14, -11, -11, -14,     77,  83,  97, 100,     0,  1,  0); //top
		helper.quad(  5,   5,  11,  11,    23, 23, 23, 23,    -11, -14, -14, -11,     88,  94,  97, 100,     0, -1,  0); //bottom

		//left head rest, left bar
		helper.quad( 10,  10,   9,   9,    23, 21, 21, 23,    -13, -13, -13, -13,     77,  78, 106, 108,     0,  0, -1); //back
		helper.quad(  9,   9,   9,   9,    23, 21, 21, 23,    -13, -13, -12, -12,     79,  80, 106, 108,    -1,  0,  0); //right
		helper.quad(  9,   9,  10,  10,    23, 21, 21, 23,    -12, -12, -12, -12,     81,  82, 106, 108,     0,  0,  1); //front
		helper.quad( 10,  10,  10,  10,    23, 21, 21, 23,    -12, -12, -13, -13,     83,  84, 106, 108,    1,  0,  0); //left

		//left head rest, right bar
		helper.quad(  7,   7,   6,   6,    23, 21, 21, 23,    -13, -13, -13, -13,     88,  89, 106, 108,     0,  0, -1); //back
		helper.quad(  6,   6,   6,   6,    23, 21, 21, 23,    -13, -13, -12, -12,     90,  91, 106, 108,    -1,  0,  0); //right
		helper.quad(  6,   6,   7,   7,    23, 21, 21, 23,    -12, -12, -12, -12,     92,  93, 106, 108,     0,  0,  1); //front
		helper.quad(  7,   7,   7,   7,    23, 21, 21, 23,    -12, -12, -13, -13,     94,  95, 106, 108,     1,  0,  0); //left

		//right head rest, pillow
		helper.quad(-11, -11,  -5,  -5,    27, 23, 23, 27,    -11, -11, -11, -11,    114, 120, 101, 105,     0,  0,  1); //front
		helper.quad( -5,  -5,  -5,  -5,    27, 23, 23, 27,    -11, -11, -14, -14,    121, 124, 101, 105,     1,  0,  0); //left
		helper.quad( -5,  -5, -11, -11,    27, 23, 23, 27,    -14, -14, -14, -14,    103, 109, 101, 105,     0,  0, -1); //back
		helper.quad(-11, -11, -11, -11,    27, 23, 23, 27,    -14, -14, -11, -11,    110, 113, 101, 105,    -1,  0,  0); //right
		helper.quad(-11, -11,  -5,  -5,    27, 27, 27, 27,    -14, -11, -11, -14,    103, 109,  97, 100,     0,  1,  0); //top
		helper.quad(-11, -11,  -5,  -5,    23, 23, 23, 23,    -11, -14, -14, -11,    114, 120,  97, 100,     0, -1,  0); //bottom

		//right head rest, left bar
		helper.quad( -6,  -6,  -7,  -7,    23, 21, 21, 23,    -13, -13, -13, -13,    103, 104, 106, 108,     0,  0, -1); //back
		helper.quad( -7,  -7,  -7,  -7,    23, 21, 21, 23,    -13, -13, -12, -12,    105, 106, 106, 108,    -1,  0,  0); //right
		helper.quad( -7,  -7,  -6,  -6,    23, 21, 21, 23,    -12, -12, -12, -12,    107, 108, 106, 108,     0,  0,  1); //front
		helper.quad( -6,  -6,  -6,  -6,    23, 21, 21, 23,    -12, -12, -13, -13,    109, 110, 106, 108,     1,  0,  0); //left

		//right head rest, right bar
		helper.quad( -9,  -9, -10, -10,    23, 21, 21, 23,    -13, -13, -13, -13,    114, 115, 106, 108,     0,  0, -1); //back
		helper.quad(-10, -10, -10, -10,    23, 21, 21, 23,    -13, -13, -12, -12,    116, 117, 106, 108,    -1,  0,  0); //right
		helper.quad(-10, -10,  -9,  -9,    23, 21, 21, 23,    -12, -12, -12, -12,    118, 119, 106, 108,     0,  0,  1); //front
		helper.quad( -9,  -9,  -9,  -9,    23, 21, 21, 23,    -12, -12, -13, -13,    120, 121, 106, 108,     1,  0,  0); //left
	}

	public void renderScoop(EntityRenderHelper helper) {
		//scoopy bit
		helper.quad(-15, -15, 15, 15,    0,  0,  0,  0,    15, 14, 14, 15,    33, 63, 102, 103,    0, -1, 0); //bottom

		for (int i1 = 0; i1 < 24;) {
			int i2 = i1 + 1;
			float dup;
			float bottomFront = (dup = i1 - 12) * dup * (5.0F / 144.0F) + 10;
			float topFront    = (dup = i2 - 12) * dup * (5.0F / 144.0F) + 10;
			float bottomBack  = bottomFront - 1.0F;
			float topBack     = topFront - 1.0F;

			float minV = 128 - i2;
			float maxV = 128 - i1;

			//glorious math simplification:
			//rise = 1
			//run = bottomFront - topFront
			//normalVector = normalize(vec2(run, rise))
			//	= vec2(run, rise) / length(vec2(run, rise))
			//	= vec2(run, rise) / sqrt(run * run + rise * rise)
			//put more directly, rise and run both need to be divided by the above length.
			//scale = 1 / sqrt(run * run + rise * rise)
			//	= fastInvSqrt(run * run + 1), because rise * rise = 1
			//rise *= scale is the same thing as rise = scale, because rise is 1
			//run *= scale is the same thing as run *= rise, because rise = scale
			//we can also remove scale entirely, since it's equivalent to rise.
			float run = bottomFront - topFront;
			float rise = MathHelper.inverseSqrt(run * run + 1.0F);
			run *= rise;

			helper.quad(-15, -15,  15,  15,    i2, i1, i1, i2,    topFront, bottomFront, bottomFront, topFront,     0, 30, minV, maxV,     0,  run,  rise); //front
			helper.quad( 15,  15, -15, -15,    i2, i1, i1, i2,    topBack,  bottomBack,  bottomBack,  topBack,     33, 63, minV, maxV,     0, -run, -rise); //back
			helper.quad( 15,  15,  15,  15,    i2, i1, i1, i2,    topFront, bottomFront, bottomBack,  topBack,     31, 32, minV, maxV,     1, 0, 0); //left
			helper.quad(-15, -15, -15, -15,    i2, i1, i1, i2,    topBack,  bottomBack,  bottomFront, topFront,    64, 65, minV, maxV,    -1, 0, 0); //right
			i1 = i2;
		}

		helper.quad(-15, -15, 15, 15,    24, 24, 24, 24,      14,  15,  15,  14,     0, 30, 102, 103,     0,  1, 0); //top

		//top right connector
		helper.quad( -9,  -9, -7, -7,    16, 16, 16, 16,       8,  10,  10,   8,     3,  5,  88,  90,     0,  1, 0); //top
		helper.quad( -7,  -7, -7, -7,    16, 14, 14, 16,      10,  10,   8,   8,     5,  7,  90,  92,     1,  0, 0); //left
		helper.quad( -9,  -9, -9, -9,    16, 14, 14, 16,       8,   8,  10,  10,     1,  3,  90,  92,    -1,  0, 0); //right
		helper.quad( -9,  -9, -7, -7,    14, 14, 14, 14,      10,   8,   8,  10,     3,  5,  92,  94,     0, -1, 0); //bottom

		//bottom right connector
		helper.quad( -9,  -9, -7, -7,    10, 10, 10, 10,       8,  10,  10,   8,     3,  5,  95,  97,     0,  1, 0); //top
		helper.quad( -7,  -7, -7, -7,    10,  8,  8, 10,      10,  10,   8,   8,     5,  7,  97,  99,     1,  0, 0); //left
		helper.quad( -9,  -9, -9, -9,    10,  8,  8, 10,       8,   8,  10,  10,     1,  3,  97,  99,    -1,  0, 0); //right
		helper.quad( -9,  -9, -7, -7,     8,  8,  8,  8,      10,   8,   8,  10,     3,  5,  99, 101,     0, -1, 0); //bottom

		//top left connector
		helper.quad(  7,   7,  9,  9,    16, 16, 16, 16,       8,  10,  10,   8,    10, 12,  88,  90,     0,  1, 0); //top
		helper.quad(  9,   9,  9,  9,    16, 14, 14, 16,      10,  10,   8,   8,    12, 14,  90,  92,     1,  0, 0); //left
		helper.quad(  7,   7,  7,  7,    16, 14, 14, 16,       8,   8,  10,  10,     8, 10,  90,  92,    -1,  0, 0); //right
		helper.quad(  7,   7,  9,  9,    14, 14, 14, 14,      10,   8,   8,  10,    10, 12,  92,  94,     0, -1, 0); //bottom

		//bottom left connector
		helper.quad(  7,   7,  9,  9,    10, 10, 10, 10,       8,  10,  10,   8,    10, 12,  95,  97,     0,  1, 0); //top
		helper.quad(  9,   9,  9,  9,    10,  8,  8, 10,      10,  10,   8,   8,    12, 14,  97,  99,     1,  0, 0); //left
		helper.quad(  7,   7,  7,  7,    10,  8,  8, 10,       8,   8,  10,  10,     8, 10,  97,  99,    -1,  0, 0); //right
		helper.quad(  7,   7,  9,  9,     8,  8,  8,  8,      10,   8,   8,  10,    10, 12,  99, 101,     0, -1, 0); //bottom
	}

	public void renderNumbers(EntityRenderHelper helper, int number) {
		for (int digitIndex = 0; digitIndex < 3; digitIndex++) {
			int digit = number % 10;
			number /= 10;
			float minU, minV;
			if (digit < 5) {
				minU = digit * 4 + 106;
				minV = 0;
			}
			else {
				minU = (digit - 5) * 4 + 106;
				minV = 6;
			}
			float offset = digitIndex * 4.5F;
			helper.quad(offset - 3, offset - 3, offset - 6, offset - 6,    12, 7, 7, 12,     -15.0625F,  -15.0625F, -15.0625F, -15.0625F,    minU, minU + 3, minV, minV + 5,     0, 0, -1);
		}
	}

	public void renderLights(EntityRenderHelper helper, float brightness) {
		int color = (((int)(brightness * 255.0F)) << 24) | 0x00FFFFFF;
		helper.quad(-12, -12,  -8,  -8,    29, 25, 25, 29,      8.0625F,   8.0625F,   8.0625F,   8.0625F,    106, 110, 12, 16,    color,    0, 0,  1);
		helper.quad(  8,   8,  12,  12,    29, 25, 25, 29,      8.0625F,   8.0625F,   8.0625F,   8.0625F,    111, 115, 12, 16,    color,    0, 0,  1);
		helper.quad( 12,  12,   8,   8,    11,  7,  7, 11,    -15.0625F, -15.0625F, -15.0625F, -15.0625F,    116, 120, 12, 16,    color,    0, 0, -1);
		helper.quad( -8,  -8, -12, -12,    11,  7,  7, 11,    -15.0625F, -15.0625F, -15.0625F, -15.0625F,    121, 125, 12, 16,    color,    0, 0, -1);
	}

	@Override
	public Identifier getTexture(MinerEntity entity) {
		return TEXTURE;
	}
}