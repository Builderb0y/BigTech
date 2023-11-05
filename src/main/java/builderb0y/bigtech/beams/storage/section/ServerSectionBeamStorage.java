package builderb0y.bigtech.beams.storage.section;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.networking.IncrementalPersistentBeamPacket;

public class ServerSectionBeamStorage extends CommonSectionBeamStorage {

	public List<IncrementalPersistentBeamPacket.Change> changes = new ArrayList<>(16);
	public Collection<ServerPlayerEntity> sendChangesTo;

	public ServerSectionBeamStorage(WorldChunk chunk, int sectionCoordY) {
		super(chunk, sectionCoordY);
	}

	public Collection<ServerPlayerEntity> acquireWatchingPlayers() {
		if (this.sendChangesTo == null) {
			this.sendChangesTo = PlayerLookup.tracking(this.chunk.world.as(), this.chunk.pos);
		}
		return this.sendChangesTo;
	}

	@Override
	public void tick() {
		if (!this.changes.isEmpty) {
			IncrementalPersistentBeamPacket packet = new IncrementalPersistentBeamPacket(this.chunk.pos.x, this.sectionCoordY, this.chunk.pos.z, this.changes);
			for (ServerPlayerEntity player : this.sendChangesTo) {
				ServerPlayNetworking.send(player, packet);
			}
			this.changes = new ArrayList<>(16);
		}
	}

	@Override
	public boolean addSegment(int index, BeamSegment segment, boolean unique) {
		if (super.addSegment(index, segment, unique)) {
			if (this.acquireWatchingPlayers() != null) {
				this.changes.add(new IncrementalPersistentBeamPacket.AddChange((short)(index), segment.beam.uuid, segment.direction, segment.effectiveColor));
			}
			return true;
		}
		return false;
	}

	@Override
	public void removeSegment(int index, BeamSegment segment) {
		super.removeSegment(index, segment);
		if (this.acquireWatchingPlayers() != null) {
			this.changes.add(new IncrementalPersistentBeamPacket.RemoveChange((short)(index), segment.beam.uuid, segment.direction));
		}
	}
}