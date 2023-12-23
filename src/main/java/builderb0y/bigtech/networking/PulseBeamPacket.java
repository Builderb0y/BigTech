package builderb0y.bigtech.networking;

import java.util.LinkedList;
import java.util.UUID;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import org.joml.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.Random;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.*;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;

public record PulseBeamPacket(PulseBeam beam) implements S2CPlayPacket {

	public static PulseBeamPacket parse(PacketByteBuf buffer) {
		return parse0(buffer);
	}

	@Environment(EnvType.CLIENT)
	public static PulseBeamPacket parse0(PacketByteBuf buffer) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) {
			throw new IllegalArgumentException("Received pulse beam while outside of world");
		}
		BeamType type = buffer.readRegistryValue(BeamType.REGISTRY);
		UUID uuid = buffer.readUuid();
		Beam beam = type.factory.create(world, uuid);
		if (!(beam instanceof PulseBeam pulseBeam)) {
			throw new IllegalArgumentException("Received pulse beam that wasn't a pulse beam: ${beam}");
		}
		int segmentCount = buffer.readInt();
		for (int index = 0; index < segmentCount; index++) {
			int x = buffer.readInt();
			int y = buffer.readInt();
			int z = buffer.readInt();
			BeamDirection direction = BeamDirection.VALUES[buffer.readByte()];
			Vector3f color = BeamSegment.unpackRgb(buffer.readUnsignedMedium());
			pulseBeam.seen.addSegment(x, y, z, new BeamSegment(pulseBeam, direction, false, color), false);
		}
		return new PulseBeamPacket(pulseBeam);
	}

	@Override
	public void write(PacketByteBuf buffer) {
		buffer.writeRegistryValue(BeamType.REGISTRY, this.beam.type);
		buffer.writeUuid(this.beam.uuid);
		int sizePosition = buffer.writerIndex();
		int size = 0;
		buffer.writeInt(0); //allocate space to hold the size.

		ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> sectionIterator = this.beam.seen.long2ObjectEntrySet().fastIterator();
		while (sectionIterator.hasNext()) {
			Long2ObjectMap.Entry<BasicSectionBeamStorage> sectionEntry = sectionIterator.next();
			int sectionStartX = ChunkSectionPos.unpackX(sectionEntry.longKey);
			int sectionStartY = ChunkSectionPos.unpackY(sectionEntry.longKey);
			int sectionStartZ = ChunkSectionPos.unpackZ(sectionEntry.longKey);
			ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> blockIterator = sectionEntry.value.short2ObjectEntrySet().fastIterator();
			while (blockIterator.hasNext()) {
				Short2ObjectMap.Entry<LinkedList<BeamSegment>> blockEntry = blockIterator.next();
				int x = sectionStartX + ChunkSectionPos.unpackLocalX(blockEntry.shortKey);
				int y = sectionStartY + ChunkSectionPos.unpackLocalY(blockEntry.shortKey);
				int z = sectionStartZ + ChunkSectionPos.unpackLocalZ(blockEntry.shortKey);
				for (BeamSegment segment : blockEntry.value) {
					buffer.writeInt(x).writeInt(y).writeInt(z);
					buffer.writeByte(segment.direction.ordinal());
					buffer.writeMedium(BeamSegment.packRgb(segment.effectiveColor));
					size++;
				}
			}
		}

		buffer.setInt(sizePosition, size);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		//todo: migrate to actual geometry instead of spawning particles.
		Random random = player.world.random;
		ObjectIterator < Long2ObjectMap.Entry <BasicSectionBeamStorage>> sectionIterator = this.beam.seen.long2ObjectEntrySet().fastIterator();
		while (sectionIterator.hasNext()) {
			Long2ObjectMap.Entry<BasicSectionBeamStorage> sectionEntry = sectionIterator.next();
			double sectionStartX = (ChunkSectionPos.unpackX(sectionEntry.longKey) << 4) + 0.5D;
			double sectionStartY = (ChunkSectionPos.unpackY(sectionEntry.longKey) << 4) + 0.5D;
			double sectionStartZ = (ChunkSectionPos.unpackZ(sectionEntry.longKey) << 4) + 0.5D;
			ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> blockIterator = sectionEntry.value.short2ObjectEntrySet().fastIterator();
			while (blockIterator.hasNext()) {
				Short2ObjectMap.Entry<LinkedList<BeamSegment>> blockEntry = blockIterator.next();
				double centerX = sectionStartX + ChunkSectionPos.unpackLocalX(blockEntry.shortKey);
				double centerY = sectionStartY + ChunkSectionPos.unpackLocalY(blockEntry.shortKey);
				double centerZ = sectionStartZ + ChunkSectionPos.unpackLocalZ(blockEntry.shortKey);
				for (BeamSegment segment : blockEntry.value) {
					double fraction = random.nextDouble();
					double dx       = segment.direction.x;
					double dy       = segment.direction.y;
					double dz       = segment.direction.z;
					double x        = centerX + dx * fraction;
					double y        = centerY + dy * fraction;
					double z        = centerZ + dz * fraction;
					player.world.addParticle(new DustParticleEffect(new Vector3f(segment.effectiveColor), 1.0F), x, y, z, dx, dy, dz);
				}
			}
		}
	}

	@Override
	public PacketType<?> getType() {
		return BigTechClientNetwork.PULSE_BEAM;
	}
}