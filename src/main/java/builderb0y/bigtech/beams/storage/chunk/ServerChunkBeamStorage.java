package builderb0y.bigtech.beams.storage.chunk;

import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.storage.section.ServerSectionBeamStorage;

public class ServerChunkBeamStorage extends CommonChunkBeamStorage<ServerSectionBeamStorage> {

	public ServerChunkBeamStorage(WorldChunk chunk) {
		super(chunk);
	}

	@Override
	public ServerSectionBeamStorage newSection(int sectionCoordY) {
		return new ServerSectionBeamStorage(this.chunk, sectionCoordY);
	}
}