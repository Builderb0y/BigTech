package builderb0y.bigtech.beams.storage.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;

public class ClientWorldBeamStorage extends CommonWorldBeamStorage {

	public ClientWorldBeamStorage(World world) {
		super(world);
	}

	@Override
	public PersistentBeam getBeam(BlockPos pos) {
		throw new UnsupportedOperationException("Beam origins do not exist on the client.");
	}

	@Override
	public void removeBeam(BlockPos pos) {
		throw new UnsupportedOperationException("Beam origins do not exist on the client.");
	}

	@Override
	public void clientTick() {
		this.clientTick0();
	}

	@Environment(EnvType.CLIENT)
	public void clientTick0() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return;
		int minX = MathHelper.floor(player.x - 32.0D) >> 4;
		int minZ = MathHelper.floor(player.z - 32.0D) >> 4;
		int maxX = MathHelper.floor(player.x + 32.0D) >> 4;
		int maxZ = MathHelper.floor(player.z + 32.0D) >> 4;
		for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
			for (int chunkX = minX; chunkX <= maxX; chunkX++) {
				Chunk chunk = this.world.getChunk(chunkX, chunkZ);
				if (chunk != null) {
					ChunkBeamStorageHolder.KEY.get(chunk).require().tick();
				}
			}
		}
	}
}