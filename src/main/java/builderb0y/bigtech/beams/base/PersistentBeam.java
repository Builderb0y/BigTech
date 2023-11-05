package builderb0y.bigtech.beams.base;

import java.util.Map;
import java.util.UUID;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.CommonChunkBeamStorage;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.util.Async;

/**
a laser beam which stays in the world until manually removed.
they will spawn particles while in the world.
additionally, they have a hook for performing an action when a block in their path changes.
they may use this to, for example, discard themselves and fire a new laser from their origin position.
*/
public abstract class PersistentBeam extends Beam {

	public PersistentBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	public abstract void onBlockChanged(BlockPos pos, BlockState oldState, BlockState newState);

	@Override
	public void addToWorld() {
		if (this.origin == null) {
			throw new IllegalStateException("Beam has not been fired yet: ${this}");
		}
		CommonWorldBeamStorage.KEY.get(this.world).addBeam(this);
		try (Async async = new Async()) {
			ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> iterator = this.seen.long2ObjectEntrySet().fastIterator();
			while (iterator.hasNext()) {
				Long2ObjectMap.Entry<BasicSectionBeamStorage> entry = iterator.next();
				CommonSectionBeamStorage existing = (
					ChunkBeamStorageHolder.KEY.get(
						this.world.getChunk(
							ChunkSectionPos.unpackX(entry.longKey),
							ChunkSectionPos.unpackZ(entry.longKey)
						)
					)
					.require()
					.getSection(ChunkSectionPos.unpackY(entry.longKey))
				);
				BasicSectionBeamStorage newStorage = entry.value;
				async.run(() -> existing.addAll(newStorage, false));
			}
		}
		this.onAdded();
	}

	public void removeFromWorld() {
		try (Async async = new Async()) {
			ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> iterator = this.seen.long2ObjectEntrySet().fastIterator();
			while (iterator.hasNext()) {
				Long2ObjectMap.Entry<BasicSectionBeamStorage> entry = iterator.next();
				CommonSectionBeamStorage existing = (
					ChunkBeamStorageHolder.KEY.get(
						this.world.getChunk(
							ChunkSectionPos.unpackX(entry.longKey),
							ChunkSectionPos.unpackZ(entry.longKey)
						)
					)
					.require()
					.getSection(ChunkSectionPos.unpackY(entry.longKey))
				);
				BasicSectionBeamStorage newStorage = entry.value;
				async.run(() -> existing.removeAll(newStorage));
			}
		}
		CommonWorldBeamStorage.KEY.get(this.world).removeBeam(this.uuid);
		this.onRemoved();
	}

	public void onAdded() {
		for (BlockPos pos : this.callbacks) {
			BlockState state = this.world.getBlockState(pos);
			BeamInteractor interactor = BeamInteractor.LOOKUP.find(this.world, pos, state, null, this);
			if (interactor instanceof BeamCallback callback) {
				callback.onBeamAdded(pos, state, this);
			}
		}
	}

	public void onRemoved() {
		for (BlockPos pos : this.callbacks) {
			BlockState state = this.world.getBlockState(pos);
			BeamInteractor interactor = BeamInteractor.LOOKUP.find(this.world, pos, state, null, this);
			if (interactor instanceof BeamCallback callback) {
				callback.onBeamRemoved(pos, state, this);
			}
		}
	}
}