package builderb0y.bigtech.beams.storage.section;

import java.util.LinkedList;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public abstract class CommonSectionBeamStorage extends BasicSectionBeamStorage {

	public final WorldChunk chunk;
	public final int sectionCoordY;

	public CommonSectionBeamStorage(WorldChunk chunk, int sectionCoordY) {
		this.chunk = chunk;
		this.sectionCoordY = sectionCoordY;
	}

	public void readFromNbt(NbtCompound tag) {
		this.readFromNbt(tag, CommonWorldBeamStorage.KEY.get(this.chunk.world));
	}

	@Override
	public LinkedList<BeamSegment> getSegments(int index) {
		this.chunk.needsSaving = true;
		return super.getSegments(index);
	}
}