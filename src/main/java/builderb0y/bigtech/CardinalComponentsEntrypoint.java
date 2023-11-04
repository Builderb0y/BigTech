package builderb0y.bigtech;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;

import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.storage.chunk.ClientChunkBeamStorage;
import builderb0y.bigtech.beams.storage.chunk.CommonChunkBeamStorage;
import builderb0y.bigtech.beams.storage.chunk.DummyChunkBeamStorage;
import builderb0y.bigtech.beams.storage.chunk.ServerChunkBeamStorage;
import builderb0y.bigtech.beams.storage.world.ClientWorldBeamStorage;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.beams.storage.world.ServerWorldBeamStorage;

public class CardinalComponentsEntrypoint implements WorldComponentInitializer, ChunkComponentInitializer {

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(CommonChunkBeamStorage.KEY, chunk -> {
			if (chunk instanceof WorldChunk worldChunk) {
				if (worldChunk.world.isClient) {
					return new ClientChunkBeamStorage(worldChunk);
				}
				else {
					return new ServerChunkBeamStorage(worldChunk);
				}
			}
			else {
				return DummyChunkBeamStorage.INSTANCE;
			}
		});
	}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(CommonWorldBeamStorage.KEY, world -> {
			if (world.isClient) {
				return new ClientWorldBeamStorage(world);
			}
			else {
				return new ServerWorldBeamStorage(world);
			}
		});
	}
}