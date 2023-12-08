package builderb0y.bigtech.beams.storage.chunk;

import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.storage.section.ClientSectionBeamStorage;

public class ClientChunkBeamStorage extends CommonChunkBeamStorage {

	public final Box box;

	public ClientChunkBeamStorage(WorldChunk chunk) {
		super(chunk);
		this.box = new Box(
			chunk.pos.startX - 0.5D,
			chunk.bottomY - 0.5D,
			chunk.pos.startZ - 0.5D,
			chunk.pos.startX + 16.5D,
			chunk.topY + 0.5D,
			chunk.pos.startZ + 0.5D
		);
	}

	@Override
	public ClientSectionBeamStorage newSection(int sectionY) {
		return new ClientSectionBeamStorage(this.chunk, sectionY);
	}
}