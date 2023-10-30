package builderb0y.bigtech.datagen.impl;

import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class SparkleParticleDataGenerator implements DataGenerator {

	public final ParticleType<?> particleType;

	public SparkleParticleDataGenerator(ParticleType<?> particleType) {
		this.particleType = particleType;
	}

	public Identifier getId() {
		return Registries.PARTICLE_TYPE.getId(this.particleType);
	}

	@Override
	public void run(DataGenContext context) {
		context.writeToFile(
			context.particlePath(this.id),
			//language=json
			"""
			{
				"textures": [
					"bigtech:sparkle"
				]
			}"""
		);
	}
}