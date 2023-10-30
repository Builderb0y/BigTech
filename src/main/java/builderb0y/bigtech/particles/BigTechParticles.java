package builderb0y.bigtech.particles;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.SparkleParticleDataGenerator;

public class BigTechParticles {

	@UseDataGen(SparkleParticleDataGenerator.class)
	public static final ParticleType<DustParticleEffect> SPARKLE_TYPE = Registry.register(
		Registries.PARTICLE_TYPE,
		BigTechMod.modID("sparkle"),
		new SimpleParticleType<>(
			false,
			DustParticleEffect.PARAMETERS_FACTORY,
			DustParticleEffect.CODEC
		)
	);

	public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		ParticleFactoryRegistry.instance.register(
			SPARKLE_TYPE,
			(FabricSpriteProvider spriteProvider) -> (
				(
					DustParticleEffect parameters,
					ClientWorld world,
					double x,
					double y,
					double z,
					double velocityX,
					double velocityY,
					double velocityZ
				)
				-> new SparkleParticle(
					world,
					spriteProvider.getSprite(world.random),
					x,
					y,
					z,
					velocityX,
					velocityY,
					velocityZ,
					parameters.color.x,
					parameters.color.y,
					parameters.color.z
				)
			)
		);
	}

	public static class SimpleParticleType<T extends ParticleEffect> extends ParticleType<T> {

		public final Codec<T> codec;

		public SimpleParticleType(boolean alwaysShow, ParticleEffect.Factory<T> parametersFactory, Codec<T> codec) {
			super(alwaysShow, parametersFactory);
			this.codec = codec;
		}

		@Override
		public Codec<T> getCodec() {
			return this.codec;
		}
	}
}