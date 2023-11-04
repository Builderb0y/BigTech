package builderb0y.bigtech.beams;

import java.util.UUID;

import it.unimi.dsi.fastutil.longs.LongIterator;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;

import builderb0y.bigtech.networking.PulseBeamPacket;

public abstract class PulseBeam extends Beam {

	public PulseBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public void addToWorld() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		LongIterator iterator = this.seen.keySet().longIterator();
		while (iterator.hasNext()) {
			long encodedSectionPos = iterator.nextLong();
			int x = ChunkSectionPos.unpackX(encodedSectionPos) << 4;
			int y = ChunkSectionPos.unpackY(encodedSectionPos) << 4;
			int z = ChunkSectionPos.unpackZ(encodedSectionPos) << 4;
			minX = Math.min(minX, x);
			minY = Math.min(minY, y);
			minZ = Math.min(minZ, z);
			maxX = Math.max(maxX, x + 16);
			maxY = Math.max(maxY, y + 16);
			maxZ = Math.max(maxZ, z + 16);
		}
		final int _minX = minX - 32;
		final int _minY = minY - 32;
		final int _minZ = minZ - 32;
		final int _maxX = maxX + 32;
		final int _maxY = maxY + 32;
		final int _maxZ = maxZ + 32;
		PulseBeamPacket packet = new PulseBeamPacket(this);
		PlayerLookup.world(this.world.as()).stream().filter(player ->
			player.blockPos.x >= _minX &&
			player.blockPos.y >= _minY &&
			player.blockPos.z >= _minZ &&
			player.blockPos.x <= _maxX &&
			player.blockPos.y <= _maxY &&
			player.blockPos.z <= _maxZ
		)
		.forEach(player -> ServerPlayNetworking.send(player, packet));
	}
}