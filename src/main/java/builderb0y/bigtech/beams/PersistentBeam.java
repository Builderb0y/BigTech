package builderb0y.bigtech.beams;

import java.util.Map;
import java.util.UUID;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;

import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.storage.chunk.CommonChunkBeamStorage;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.util.Async;

public abstract class PersistentBeam extends Beam {

	public PersistentBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	public abstract void onBlockChanged(BlockPos pos, BlockState oldState, BlockState newState);

	@Override
	public void addToWorld() {
		CommonWorldBeamStorage.KEY.get(this.world).addBeam(this);
		try (Async async = new Async()) {
			ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> iterator = this.seen.long2ObjectEntrySet().fastIterator();
			while (iterator.hasNext()) {
				Long2ObjectMap.Entry<BasicSectionBeamStorage> entry = iterator.next();
				CommonSectionBeamStorage existing = (
					CommonChunkBeamStorage.KEY.get(
						this.world.getChunk(
							ChunkSectionPos.unpackX(entry.longKey),
							ChunkSectionPos.unpackZ(entry.longKey)
						)
					)
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
					CommonChunkBeamStorage.KEY.get(
						this.world.getChunk(
							ChunkSectionPos.unpackX(entry.longKey),
							ChunkSectionPos.unpackZ(entry.longKey)
						)
					)
					.getSection(ChunkSectionPos.unpackY(entry.longKey))
				);
				BasicSectionBeamStorage newStorage = entry.value;
				async.run(() -> existing.removeAll(newStorage));
			}
		}
		CommonWorldBeamStorage.KEY.get(this.world).removeBeam(this.uuid);
	}

	public void onAdded() {
		for (Map.Entry<BlockPos, BeamCallback> entry : this.callbacks.entrySet()) {
			entry.value.onBeamAdded(entry.key, this.world.getBlockState(entry.key), this);
		}
	}

	public void onRemoved() {
		for (Map.Entry<BlockPos, BeamCallback> entry : this.callbacks.entrySet()) {
			entry.value.onBeamRemoved(entry.key, this.world.getBlockState(entry.key), this);
		}
	}
}