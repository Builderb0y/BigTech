package builderb0y.bigtech.particles;

import org.joml.Vector3f;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleType;

public class SparkleParticleEffect extends DustParticleEffect {

	public SparkleParticleEffect(Vector3f vector3f, float f) {
		super(vector3f, f);
	}

	@Override
	public ParticleType<DustParticleEffect> getType() {
		return BigTechParticles.SPARKLE_TYPE;
	}
}