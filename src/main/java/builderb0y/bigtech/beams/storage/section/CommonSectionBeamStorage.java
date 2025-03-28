package builderb0y.bigtech.beams.storage.section;

import java.util.LinkedList;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public abstract class CommonSectionBeamStorage extends BasicSectionBeamStorage {

	public final WorldChunk chunk;

	public CommonSectionBeamStorage(WorldChunk chunk, int sectionCoordY) {
		super(chunk.getPos().x, sectionCoordY, chunk.getPos().z);
		this.chunk = chunk;
	}

	public void readFromNbt(NbtCompound tag) {
		this.readFromNbt(tag, CommonWorldBeamStorage.KEY.get(this.chunk.getWorld()));
	}

	@Override
	public LinkedList<BeamSegment> getSegments(int index) {
		this.chunk.markNeedsSaving();
		return super.getSegments(index);
	}
}