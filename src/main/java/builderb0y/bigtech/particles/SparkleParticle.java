package builderb0y.bigtech.particles;

import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class SparkleParticle extends BillboardParticle {

	public final Sprite sprite;

	public SparkleParticle(ClientWorld clientWorld, Sprite sprite, double x, double y, double z, double dx, double dy, double dz, float red, float green, float blue) {
		super(clientWorld, x, y, z, dx, dy, dz);
		this.sprite    = sprite;
		this.red       = red;
		this.green     = green;
		this.blue      = blue;
		this.maxAge    = clientWorld.random.nextBetween(10, 20);
		this.velocityX = dx; //enforce no randomness.
		this.velocityY = dy;
		this.velocityZ = dz;
	}

	@Override
	public float getSize(float tickDelta) {
		return (float)(1.0D - Math.cos((this.age + tickDelta) * MathHelper.TAU / this.maxAge) * 0.5D) * this.scale;
	}

	@Override public float getMinU() { return this.sprite.getMinU(); }
	@Override public float getMaxU() { return this.sprite.getMaxU(); }
	@Override public float getMinV() { return this.sprite.getMinV(); }
	@Override public float getMaxV() { return this.sprite.getMaxV(); }

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
}