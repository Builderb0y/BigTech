package builderb0y.bigtech;

import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.world.ClientWorldBeamStorage;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.beams.storage.world.ServerWorldBeamStorage;

public class CardinalComponentsEntrypoint implements WorldComponentInitializer, ChunkComponentInitializer {

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(ChunkBeamStorageHolder.KEY, ChunkBeamStorageHolder::new);
	}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(CommonWorldBeamStorage.KEY, (World world) -> {
			if (world instanceof ServerWorld serverWorld) {
				return new ServerWorldBeamStorage(serverWorld);
			}
			else {
				return new ClientWorldBeamStorage(world);
			}
		});
	}
}