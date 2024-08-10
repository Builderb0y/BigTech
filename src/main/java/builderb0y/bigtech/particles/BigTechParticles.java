package builderb0y.bigtech.particles;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
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
			DustParticleEffect.CODEC,
			DustParticleEffect.PACKET_CODEC
		)
	);

	public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		ParticleFactoryRegistry.getInstance().register(
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
					parameters.getColor().x,
					parameters.getColor().y,
					parameters.getColor().z
				)
			)
		);
	}

	public static class SimpleParticleType<T extends ParticleEffect> extends ParticleType<T> {

		public final MapCodec<T> codec;
		public final PacketCodec<? super RegistryByteBuf, T> packetCodec;

		public SimpleParticleType(boolean alwaysShow, MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
			super(alwaysShow);
			this.codec = codec;
			this.packetCodec = packetCodec;
		}

		@Override
		public MapCodec<T> getCodec() {
			return this.codec;
		}

		@Override
		public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
			return this.packetCodec;
		}
	}
}