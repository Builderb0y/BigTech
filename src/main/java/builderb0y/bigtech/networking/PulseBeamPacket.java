package builderb0y.bigtech.networking;

import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import org.joml.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.Random;

import builderb0y.bigtech.beams.base.*;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.util.Lockable;
import builderb0y.bigtech.util.Locked;

public class PulseBeamPacket implements S2CPlayPacket<PulseBeamPacket.Payload> {

	public static final PulseBeamPacket INSTANCE = new PulseBeamPacket();

	public void send(ServerWorld world, PulseBeam beam) {
		Collection<ServerPlayerEntity> worldPlayers = PlayerLookup.world(world);
		if (worldPlayers.isEmpty()) return;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		LongIterator iterator = beam.seen.keySet().longIterator();
		while (iterator.hasNext()) {
			long encodedSectionPos = iterator.nextLong();
			int x = ChunkSectionPos.unpackX(encodedSectionPos) << 4;
			int y = ChunkSectionPos.unpackY(encodedSectionPos) << 4;
			int z = ChunkSectionPos.unpackZ(encodedSectionPos) << 4;
			minX = Math.min(minX, x);
			minY = Math.min(minY, y);
			minZ = Math.min(minZ, z);
			maxX = Math.max(maxX, x | 15);
			maxY = Math.max(maxY, y | 15);
			maxZ = Math.max(maxZ, z | 15);
		}
		int serverViewDistance = world.getServer().getPlayerManager().getViewDistance() << 4;
		Payload payload = new Payload(beam);
		for (ServerPlayerEntity player : worldPlayers) {
			int playerViewDistance = Math.min(player.getViewDistance() << 4, serverViewDistance);
			BlockPos pos = player.getBlockPos();
			if (
				pos.getX() >= minX - playerViewDistance &&
				pos.getY() >= minY - playerViewDistance &&
				pos.getZ() >= minZ - playerViewDistance &&
				pos.getX() <= maxX + playerViewDistance &&
				pos.getY() <= maxY + playerViewDistance &&
				pos.getZ() <= maxZ + playerViewDistance
			) {
				BigTechNetwork.sendToClient(player, payload);
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Payload decode(RegistryByteBuf buffer) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) {
			throw new IllegalArgumentException("Received pulse beam while outside of world");
		}
		BeamType type = buffer.readRegistryValue(BeamType.REGISTRY_KEY);
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
		return new Payload(pulseBeam);
	}

	public static record Payload(PulseBeam beam) implements S2CPayload {

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeRegistryValue(BeamType.REGISTRY_KEY, this.beam.getType());
			buffer.writeUuid(this.beam.uuid);
			int sizePosition = buffer.writerIndex();
			int size = 0;
			buffer.writeInt(0); //allocate space to hold the size.

			ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> sectionIterator = this.beam.seen.long2ObjectEntrySet().fastIterator();
			while (sectionIterator.hasNext()) {
				Long2ObjectMap.Entry<BasicSectionBeamStorage> sectionEntry = sectionIterator.next();
				int sectionStartX = ChunkSectionPos.unpackX(sectionEntry.getLongKey()) << 4;
				int sectionStartY = ChunkSectionPos.unpackY(sectionEntry.getLongKey()) << 4;
				int sectionStartZ = ChunkSectionPos.unpackZ(sectionEntry.getLongKey()) << 4;
				ObjectIterator<Short2ObjectMap.Entry<Lockable<TreeSet<BeamSegment>>>> blockIterator = sectionEntry.getValue().short2ObjectEntrySet().fastIterator();
				while (blockIterator.hasNext()) {
					Short2ObjectMap.Entry<Lockable<TreeSet<BeamSegment>>> blockEntry = blockIterator.next();
					int x = sectionStartX + ChunkSectionPos.unpackLocalX(blockEntry.getShortKey());
					int y = sectionStartY + ChunkSectionPos.unpackLocalY(blockEntry.getShortKey());
					int z = sectionStartZ + ChunkSectionPos.unpackLocalZ(blockEntry.getShortKey());
					try (Locked<TreeSet<BeamSegment>> locked = blockEntry.getValue().read()) {
						for (BeamSegment segment : locked.value) {
							buffer.writeInt(x).writeInt(y).writeInt(z);
							buffer.writeByte(segment.direction().ordinal());
							buffer.writeMedium(BeamSegment.packRgb(segment.getEffectiveColor()));
							size++;
						}
					}
				}
			}

			buffer.setInt(sizePosition, size);
		}

		@Override
		@Environment(EnvType.CLIENT)
		public void process(ClientPlayNetworking.Context context) {
			BeamRendering.addPulse(this.beam);
		}
	}
}