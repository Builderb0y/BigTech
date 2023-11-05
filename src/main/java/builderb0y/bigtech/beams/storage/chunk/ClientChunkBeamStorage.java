package builderb0y.bigtech.beams.storage.chunk;

import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.storage.section.ClientSectionBeamStorage;

public class ClientChunkBeamStorage extends CommonChunkBeamStorage {

	public ClientChunkBeamStorage(WorldChunk chunk) {
		super(chunk);
	}

	@Override
	public ClientSectionBeamStorage newSection(int sectionY) {
		return new ClientSectionBeamStorage(this.chunk, sectionY);
	}
}