package builderb0y.bigtech.beams.storage.chunk;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.CopyableComponent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.BigTechMod;

public class ChunkBeamStorageHolder implements Supplier<CommonChunkBeamStorage>, CopyableComponent<ChunkBeamStorageHolder> {

	public static final ComponentKey<ChunkBeamStorageHolder> KEY = ComponentRegistry.getOrCreate(BigTechMod.modID("chunk_beam_storage"), ChunkBeamStorageHolder.class);

	public Chunk chunk;
	public CommonChunkBeamStorage storage;

	public ChunkBeamStorageHolder(Chunk chunk) {
		this.chunk = chunk;
	}

	@Override
	public @Nullable CommonChunkBeamStorage get() {
		Chunk chunk = this.chunk;
		if (chunk instanceof WorldChunk worldChunk) {
			if (worldChunk.getWorld().isClient) {
				this.storage = new ClientChunkBeamStorage(worldChunk);
			}
			else {
				this.storage = new ServerChunkBeamStorage(worldChunk);
			}
		}
		this.chunk = null;
		return this.storage;
	}

	public @NotNull CommonChunkBeamStorage require() {
		Chunk chunk = this.chunk;
		CommonChunkBeamStorage storage = this.get();
		if (storage != null) return storage;
		else throw new IllegalStateException("Could not get CommonChunkBeamStorage from ${chunk}");
	}

	@Override
	public void copyFrom(ChunkBeamStorageHolder that, RegistryWrapper.WrapperLookup registryLookup) {
		CommonChunkBeamStorage thisStorage = this.get();
		CommonChunkBeamStorage thatStorage = that.get();
		if (thisStorage != null && thatStorage != null) {
			thisStorage.copyFrom(thatStorage);
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		CommonChunkBeamStorage storage = this.get();
		if (storage != null) storage.readFromNbt(tag);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		CommonChunkBeamStorage storage = this.get();
		if (storage != null) storage.writeToNbt(tag);
	}
}