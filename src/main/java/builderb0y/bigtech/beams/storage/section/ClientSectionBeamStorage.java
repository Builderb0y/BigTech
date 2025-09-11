package builderb0y.bigtech.beams.storage.section;

import java.util.TreeSet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;

import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.base.BeamMeshBuilder;
import builderb0y.bigtech.beams.base.BeamMeshBuilder.AdjacentSegmentLoader;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.util.Lockable;

public class ClientSectionBeamStorage extends CommonSectionBeamStorage {

	public final Box box;
	@Environment(EnvType.CLIENT)
	public Mesh mesh;

	public ClientSectionBeamStorage(WorldChunk chunk, int sectionCoordY) {
		super(chunk, sectionCoordY);
		this.box = new Box(
			chunk.getPos().getStartX() - 0.5D,
			(sectionCoordY << 4) - 0.5D,
			chunk.getPos().getStartZ() - 0.5D,
			chunk.getPos().getStartX() + 16.5D,
			(sectionCoordY << 4) + 16.5D,
			chunk.getPos().getStartZ() + 16.5D
		);
	}

	@Environment(EnvType.CLIENT)
	public Mesh getMesh() {
		if (this.mesh == null) {
			this.mesh = BeamMeshBuilder.build(this, AdjacentSegmentLoader.PERSISTENT);
		}
		return this.mesh;
	}

	@Override
	public Lockable<TreeSet<BeamSegment>> getSegments(int index) {
		this.invalidate();
		return super.getSegments(index);
	}

	public void invalidate() {
		this.invalidateClient();
	}

	@Environment(EnvType.CLIENT)
	public void invalidateClient() {
		this.mesh = null;
	}
}