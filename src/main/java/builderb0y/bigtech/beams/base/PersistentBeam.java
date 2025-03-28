package builderb0y.bigtech.beams.base;

import java.util.*;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.networking.AddBeamPacket;
import builderb0y.bigtech.networking.RemoveBeamPacket;
import builderb0y.bigtech.util.AsyncConsumer;
import builderb0y.bigtech.util.AsyncRunner;
import builderb0y.bigtech.util.NbtReadingException;

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

	public static void notifyBlockChanged(World world, BlockPos pos, BlockState oldState, BlockState newState) {
		notifyBlockChanged(world.getChunk(pos).<WorldChunk>as(), pos, oldState, newState);
	}

	public static void notifyBlockChanged(WorldChunk chunk, BlockPos pos, BlockState oldState, BlockState newState) {
		if (chunk.getWorld() instanceof ServerWorld serverWorld) {
			CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(chunk).require().get(pos.getY() >> 4);
			if (sectionStorage != null) {
				LinkedList<BeamSegment> segments = sectionStorage.checkSegments(pos);
				if (segments != null) {
					for (BeamSegment segment : segments) {
						segment.beam().<PersistentBeam>as().onBlockChanged(serverWorld, pos, oldState, newState);
					}
				}
			}
		}
	}

	public abstract void onBlockChanged(ServerWorld world, BlockPos pos, BlockState oldState, BlockState newState);

	public void onEntityCollision(ServerWorld world, BlockPos pos, Entity entity) {}

	public int getLightLevel(BeamSegment segment) {
		return 0;
	}

	@Override
	public void addToWorld(ServerWorld world) {
		if (this.origin == null) {
			throw new IllegalStateException("Beam has not been fired yet: ${this}");
		}
		CommonWorldBeamStorage.KEY.get(world).addBeam(this);
		try (AsyncRunner async = new AsyncRunner()) {
			for (
				ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> iterator = this.seen.long2ObjectEntrySet().fastIterator();
				iterator.hasNext();
			) {
				Long2ObjectMap.Entry<BasicSectionBeamStorage> entry = iterator.next();
				int sectionX = ChunkSectionPos.unpackX(entry.getLongKey());
				int sectionY = ChunkSectionPos.unpackY(entry.getLongKey());
				int sectionZ = ChunkSectionPos.unpackZ(entry.getLongKey());
				CommonSectionBeamStorage existing = (
					ChunkBeamStorageHolder.KEY.get(
						world.getChunk(sectionX, sectionZ)
					)
					.require()
					.getSection(sectionY)
				);
				BasicSectionBeamStorage newStorage = entry.getValue();
				async.submit(() -> existing.addAll(newStorage, false));
				AddBeamPacket.INSTANCE.send(world, sectionX, sectionY, sectionZ, this.uuid, newStorage);
			}
		}
		this.onAdded(world);
	}

	public void removeFromWorld(ServerWorld world) {
		record SyncResult(
			Collection<ServerPlayerEntity> players,
			int sectionX,
			int sectionY,
			int sectionZ,
			UUID uuid,
			ShortCollection segmentPositions
		) {

			public static void send(SyncResult result) {
				if (result != null) {
					RemoveBeamPacket.send(
						result.players,
						result.sectionX,
						result.sectionY,
						result.sectionZ,
						result.uuid,
						result.segmentPositions
					);
				}
			}
		}
		try (AsyncConsumer<SyncResult> syncer = new AsyncConsumer<>(SyncResult::send)) {
			ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> sectionIterator = this.seen.long2ObjectEntrySet().fastIterator();
			while (sectionIterator.hasNext()) {
				Long2ObjectMap.Entry<BasicSectionBeamStorage> sectionEntry = sectionIterator.next();
				int sectionX = ChunkSectionPos.unpackX(sectionEntry.getLongKey());
				int sectionY = ChunkSectionPos.unpackY(sectionEntry.getLongKey());
				int sectionZ = ChunkSectionPos.unpackZ(sectionEntry.getLongKey());
				CommonSectionBeamStorage existing = (
					ChunkBeamStorageHolder.KEY.get(
						world.getChunk(sectionX, sectionZ)
					)
					.require()
					.getSection(sectionY)
				);
				BasicSectionBeamStorage newStorage = sectionEntry.getValue();
				syncer.submit(() -> {
					existing.removeAll(newStorage);
					Collection<ServerPlayerEntity> tracking = PlayerLookup.tracking(world, new ChunkPos(sectionX, sectionZ));
					if (tracking.isEmpty()) return null;
					ShortList positions = new ShortArrayList(newStorage.size());
					ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> blockIterator = newStorage.short2ObjectEntrySet().fastIterator();
					while (blockIterator.hasNext()) {
						Short2ObjectMap.Entry<LinkedList<BeamSegment>> blockEntry = blockIterator.next();
						for (BeamSegment segment : blockEntry.getValue()) {
							if (segment.visible()) {
								positions.add(blockEntry.getShortKey());
								break;
							}
						}
					}
					return new SyncResult(tracking, sectionX, sectionY, sectionZ, this.uuid, positions);
				});
			}
		}
		CommonWorldBeamStorage.KEY.get(world).removeBeam(this.uuid);
		this.onRemoved(world);
	}

	public void onAdded(ServerWorld world) {
		for (BlockPos pos : this.callbacks) {
			BlockState state = world.getBlockState(pos);
			BeamInteractor interactor = BeamInteractor.LOOKUP.find(world, pos, state, null, this);
			if (interactor instanceof BeamCallback callback) {
				callback.onBeamAdded(world, pos, state, this);
			}
		}
	}

	public void onRemoved(ServerWorld world) {
		for (BlockPos pos : this.callbacks) {
			BlockState state = world.getBlockState(pos);
			BeamInteractor interactor = BeamInteractor.LOOKUP.find(world, pos, state, null, this);
			if (interactor instanceof BeamCallback callback) {
				callback.onBeamRemoved(world, pos, state, this);
			}
		}
	}

	public void writeToNbt(NbtCompound nbt) {
		nbt
		.withIdentifier("type", BeamType.REGISTRY.getId(this.getType()))
		.withUuid("uuid", this.uuid)
		.withBlockPos("origin", this.origin)
		.withLongArray("callbacks", this.callbacks.stream().mapToLong(BlockPos::asLong).toArray())
		.withSubList("segment_sections", (NbtList list) -> {
			try (AsyncRunner async = new AsyncRunner()) {
				ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> sectionIterator = this.seen.long2ObjectEntrySet().fastIterator();
				while (sectionIterator.hasNext()) {
					Long2ObjectMap.Entry<BasicSectionBeamStorage> sectionEntry = sectionIterator.next();
					long sectionPos = sectionEntry.getLongKey();
					BasicSectionBeamStorage section = sectionEntry.getValue();
					NbtCompound sectionNbt = list.createCompound().withLong("pos", sectionPos);
					async.submit(() -> section.writeToNbt(sectionNbt, false));
				}
			}
		});
	}

	public void readFromNbt(NbtCompound nbt) throws NbtReadingException {
		this.origin = nbt.requireBlockPos("origin");
		NbtList sectionsNbt = nbt.getList("segment_sections").orElse(null);
		if (sectionsNbt != null) {
			try (AsyncRunner async = new AsyncRunner()) {
				for (NbtCompound sectionNbt : sectionsNbt.<Iterable<NbtCompound>>as()) {
					Long sectionPos = sectionNbt.getLong("pos").orElse(null);
					if (sectionPos == null) continue;
					BasicSectionBeamStorage section = this.seen.getSegments(sectionPos);
					async.submit(() -> section.readFromNbt(sectionNbt, this));
				}
			}
		}
		long[] callbacks = nbt.getLongArray("callbacks").orElse(null);
		if (callbacks != null) Arrays.stream(callbacks).mapToObj(BlockPos::fromLong).forEach(this.callbacks::add);
	}
}