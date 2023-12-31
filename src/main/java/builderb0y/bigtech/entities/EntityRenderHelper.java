package builderb0y.bigtech.entities;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class EntityRenderHelper {

	public VertexConsumer vertexConsumer;
	public Matrix4f positionMatrix;
	public Matrix3f normalMatrix;
	public int lightmap;
	public float textureScaleX = 1.0F, textureScaleY = 1.0F;
	public Vector4f positionVector = new Vector4f();
	public Vector3f normalVector = new Vector3f();

	public EntityRenderHelper vertexConsumer(VertexConsumer vertexConsumer) {
		this.vertexConsumer = vertexConsumer;
		return this;
	}

	public EntityRenderHelper transform(Matrix4f positionMatrix, Matrix3f normalMatrix) {
		this.positionMatrix = positionMatrix;
		this.normalMatrix = normalMatrix;
		return this;
	}

	public EntityRenderHelper transform(MatrixStack.Entry entry) {
		return this.transform(entry.positionMatrix, entry.normalMatrix);
	}

	public EntityRenderHelper lightmap(int lightmap) {
		this.lightmap = lightmap;
		return this;
	}

	public EntityRenderHelper textureScale(float scaleX, float scaleY) {
		this.textureScaleX = scaleX;
		this.textureScaleY = scaleY;
		return this;
	}

	public void quad(
		float x0, float x1, float x2, float x3,
		float y0, float y1, float y2, float y3,
		float z0, float z1, float z2, float z3,
		float u0, float u1, float v0, float v1,
		float r,  float g,  float b,  float a,
		float nx, float ny, float nz
	) {
		this.quad(
			x0, x1, x2, x3,
			y0, y1, y2, y3,
			z0, z1, z2, z3,
			u0, u1, v0, v1,
			(((int)(a * 255.0F)) << 24) |
			(((int)(r * 255.0F)) << 16) |
			(((int)(g * 255.0F)) <<  8) |
			(((int)(b * 255.0F))      ),
			nx, ny, nz
		);
	}

	public void quad(
		float x0, float x1, float x2, float x3,
		float y0, float y1, float y2, float y3,
		float z0, float z1, float z2, float z3,
		float u0, float u1, float v0, float v1,
		float nx, float ny, float nz
	) {
		this.quad(
			x0, x1, x2, x3,
			y0, y1, y2, y3,
			z0, z1, z2, z3,
			u0, u1, v0, v1,
			-1,
			nx, ny, nz
		);
	}

	public void quad(
		float x0, float x1, float x2, float x3,
		float y0, float y1, float y2, float y3,
		float z0, float z1, float z2, float z3,
		float u0, float u1, float v0, float v1,
		int color,
		float nx, float ny, float nz
	) {
		//normally VertexConsumer.vertex() and normal(),
		//specifically the variants that take a matrix,
		//allocate a new vector to do the transformation.
		//I use a shared vector instead,
		//which means doing the transformations manually.

		this.normalMatrix.transform(this.normalVector.set(nx, ny, nz));
		nx = this.normalVector.x;
		ny = this.normalVector.y;
		nz = this.normalVector.z;

		this.positionMatrix.transform(this.positionVector.set(x0, y0, z0, 1.0F));
		x0 = this.positionVector.x;
		y0 = this.positionVector.y;
		z0 = this.positionVector.z;

		if (!(nx * x0 + ny * y0 + nz * z0 < 0.0F)) return; //early discard back faces.

		this.positionMatrix.transform(this.positionVector.set(x1, y1, z1, 1.0F));
		x1 = this.positionVector.x;
		y1 = this.positionVector.y;
		z1 = this.positionVector.z;

		this.positionMatrix.transform(this.positionVector.set(x2, y2, z2, 1.0F));
		x2 = this.positionVector.x;
		y2 = this.positionVector.y;
		z2 = this.positionVector.z;

		this.positionMatrix.transform(this.positionVector.set(x3, y3, z3, 1.0F));
		x3 = this.positionVector.x;
		y3 = this.positionVector.y;
		z3 = this.positionVector.z;

		u0 *= this.textureScaleX;
		u1 *= this.textureScaleX;
		v0 *= this.textureScaleY;
		v1 *= this.textureScaleY;

		//debug:
		//color = MathHelper.packRgb(nx * 0.5F + 0.5F, ny * 0.5F + 0.5F, nz * 0.5F + 0.5F) | 0xFF000000;

		this.vertexConsumer.vertex(x0, y0, z0).color(color).texture(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(this.lightmap).normal(nx, ny, nz).next();
		this.vertexConsumer.vertex(x1, y1, z1).color(color).texture(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(this.lightmap).normal(nx, ny, nz).next();
		this.vertexConsumer.vertex(x2, y2, z2).color(color).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(this.lightmap).normal(nx, ny, nz).next();
		this.vertexConsumer.vertex(x3, y3, z3).color(color).texture(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(this.lightmap).normal(nx, ny, nz).next();
	}
}