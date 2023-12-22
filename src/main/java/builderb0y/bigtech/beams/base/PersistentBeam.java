package builderb0y.bigtech.beams.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
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
import builderb0y.bigtech.networking.BigTechClientNetwork;
import builderb0y.bigtech.networking.RemoveBeamPacket;
import builderb0y.bigtech.util.AsyncConsumer;
import builderb0y.bigtech.util.AsyncRunner;

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
		if (!chunk.world.isClient) {
			CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(chunk).require().get(pos.y >> 4);
			if (sectionStorage != null) {
				LinkedList<BeamSegment> segments = sectionStorage.checkSegments(pos);
				if (segments != null) {
					for (BeamSegment segment : segments) {
						((PersistentBeam)(segment.beam)).onBlockChanged(pos, oldState, newState);
					}
				}
			}
		}
	}

	public abstract void onBlockChanged(BlockPos pos, BlockState oldState, BlockState newState);

	public void onEntityCollision(BlockPos pos, Entity entity) {}

	@Override
	public void addToWorld() {
		if (this.origin == null) {
			throw new IllegalStateException("Beam has not been fired yet: ${this}");
		}
		CommonWorldBeamStorage.KEY.get(this.world).addBeam(this);
		try (AsyncRunner async = new AsyncRunner()) {
			ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> iterator = this.seen.long2ObjectEntrySet().fastIterator();
			while (iterator.hasNext()) {
				Long2ObjectMap.Entry<BasicSectionBeamStorage> entry = iterator.next();
				int sectionX = ChunkSectionPos.unpackX(entry.longKey);
				int sectionY = ChunkSectionPos.unpackY(entry.longKey);
				int sectionZ = ChunkSectionPos.unpackZ(entry.longKey);
				CommonSectionBeamStorage existing = (
					ChunkBeamStorageHolder.KEY.get(
						this.world.getChunk(sectionX, sectionZ)
					)
					.require()
					.getSection(sectionY)
				);
				BasicSectionBeamStorage newStorage = entry.value;
				async.run(() -> existing.addAll(newStorage, false));
				BigTechClientNetwork.send(
					PlayerLookup.tracking(this.world.as(), new ChunkPos(sectionX, sectionZ)),
					() -> AddBeamPacket.create(
						sectionX,
						sectionY,
						sectionZ,
						this.uuid,
						newStorage
					)
				);
			}
		}
		this.onAdded();
	}

	public void removeFromWorld() {
		record SyncResult(Collection<ServerPlayerEntity> players, RemoveBeamPacket packet) {

			public static void send(SyncResult result) {
				if (result != null) {
					for (ServerPlayerEntity player : result.players) {
						ServerPlayNetworking.send(player, result.packet);
					}
				}
			}
		}
		try (AsyncConsumer<SyncResult> syncer = new AsyncConsumer<>(SyncResult::send)) {
			ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> sectionIterator = this.seen.long2ObjectEntrySet().fastIterator();
			while (sectionIterator.hasNext()) {
				Long2ObjectMap.Entry<BasicSectionBeamStorage> sectionEntry = sectionIterator.next();
				int sectionX = ChunkSectionPos.unpackX(sectionEntry.longKey);
				int sectionY = ChunkSectionPos.unpackY(sectionEntry.longKey);
				int sectionZ = ChunkSectionPos.unpackZ(sectionEntry.longKey);
				CommonSectionBeamStorage existing = (
					ChunkBeamStorageHolder.KEY.get(
						this.world.getChunk(sectionX, sectionZ)
					)
					.require()
					.getSection(sectionY)
				);
				BasicSectionBeamStorage newStorage = sectionEntry.value;
				syncer.add(() -> {
					existing.removeAll(newStorage);
					Collection<ServerPlayerEntity> tracking = PlayerLookup.tracking(this.world.as(), new ChunkPos(sectionX, sectionZ));
					if (tracking.isEmpty) return null;
					ShortList positions = new ShortArrayList(newStorage.size());
					ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> blockIterator = newStorage.short2ObjectEntrySet().fastIterator();
					while (blockIterator.hasNext()) {
						Short2ObjectMap.Entry<LinkedList<BeamSegment>> blockEntry = blockIterator.next();
						for (BeamSegment segment : blockEntry.value) {
							if (segment.visible) {
								positions.add(blockEntry.shortKey);
								break;
							}
						}
					}
					return new SyncResult(tracking, new RemoveBeamPacket(sectionX, sectionY, sectionZ, this.uuid, positions));
				});
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

	public void writeToNbt(NbtCompound nbt) {
		nbt
		.withIdentifier("type", BeamType.REGISTRY.getId(this.type))
		.withUuid("uuid", this.uuid)
		.withBlockPos("origin", this.origin)
		.withLongArray("callbacks", this.callbacks.stream().mapToLong(BlockPos::asLong).toArray())
		.withSubList("segment_sections", list -> {
			try (AsyncRunner async = new AsyncRunner()) {
				ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> sectionIterator = this.seen.long2ObjectEntrySet().fastIterator();
				while (sectionIterator.hasNext()) {
					Long2ObjectMap.Entry<BasicSectionBeamStorage> sectionEntry = sectionIterator.next();
					long sectionPos = sectionEntry.longKey;
					BasicSectionBeamStorage section = sectionEntry.value;
					NbtCompound sectionNbt = list.createCompound();
					async.run(() -> {
						sectionNbt.putLong("pos", sectionPos);
						section.writeToNbt(sectionNbt, false);
					});
				}
			}
		});
	}

	public void readFromNbt(NbtCompound nbt) {
		this.origin = nbt.getBlockPos("origin");
		NbtList sectionsNbt = nbt.getList("segment_sections", NbtElement.COMPOUND_TYPE);
		try (AsyncRunner async = new AsyncRunner()) {
			for (NbtCompound sectionNbt : sectionsNbt.<Iterable<NbtCompound>>as()) {
				long sectionPos = sectionNbt.getLong("pos");
				BasicSectionBeamStorage section = this.seen.getSegments(sectionPos);
				async.run(() -> {
					section.readFromNbt(sectionNbt, this);
				});
			}
		}
		Arrays.stream(nbt.getLongArray("callbacks")).mapToObj(BlockPos::fromLong).forEach(this.callbacks::add);
	}
}