package builderb0y.bigtech.beams.storage.chunk;

import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.storage.section.ClientSectionBeamStorage;

public class ClientChunkBeamStorage extends CommonChunkBeamStorage {

	public final Box box;

	public ClientChunkBeamStorage(WorldChunk chunk) {
		super(chunk);
		this.box = new Box(
			chunk.getPos().getStartX() - 0.5D,
			chunk.minY() - 0.5D,
			chunk.getPos().getStartZ() - 0.5D,
			chunk.getPos().getStartX() + 16.5D,
			chunk.maxY() + 0.5D,
			chunk.getPos().getStartZ() + 0.5D
		);
	}

	@Override
	public ClientSectionBeamStorage newSection(int sectionY) {
		return new ClientSectionBeamStorage(this.chunk, sectionY);
	}
}