package builderb0y.bigtech.beams.storage.world;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.PersistentBeam;
import builderb0y.bigtech.networking.TogglePersistentBeamPacket;

public class ServerWorldBeamStorage extends CommonWorldBeamStorage {

	public final Map<BlockPos, PersistentBeam> beamsByOrigin = new Object2ObjectOpenHashMap<>(32);

	public ServerWorldBeamStorage(World world) {
		super(world);
	}

	@Override
	public PersistentBeam getBeam(BlockPos pos) {
		return this.beamsByOrigin.get(pos);
	}

	@Override
	public void addBeam(PersistentBeam beam) {
		super.addBeam(beam);
		Collection<ServerPlayerEntity> players = PlayerLookup.world(this.world.as());
		if (!players.isEmpty) {
			TogglePersistentBeamPacket packet = new TogglePersistentBeamPacket(true, beam.uuid, beam.type);
			for (ServerPlayerEntity player : players) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	@Override
	public void addBeamNoSync(PersistentBeam beam) {
		if (this.beamsById.containsKey(beam.uuid) || this.beamsByOrigin.containsKey(beam.origin)) {
			throw new IllegalArgumentException("Duplicate beam with ID ${beam.uuid} and origin ${beam.origin}: ${beam}");
		}
		this.beamsById.put(beam.uuid, beam);
		this.beamsByOrigin.put(beam.origin, beam);
	}

	@Override
	public void removeBeam(UUID uuid) {
		PersistentBeam removed = this.beamsById.remove(uuid);
		if (removed != null) {
			this.beamsByOrigin.remove(removed.origin);
		}
		else {
			BigTechMod.LOGGER.warn("Attempt to remove non-existent beam ${uuid}.");
			return;
		}
		Collection<ServerPlayerEntity> players = PlayerLookup.world(this.world.as());
		if (!players.isEmpty) {
			TogglePersistentBeamPacket packet = new TogglePersistentBeamPacket(false, uuid, null);
			for (ServerPlayerEntity player : players) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	@Override
	public void removeBeam(BlockPos pos) {
		PersistentBeam removed = this.beamsByOrigin.remove(pos);
		if (removed != null) {
			this.beamsById.remove(removed.uuid);
		}
		else {
			BigTechMod.LOGGER.warn("Attempt to remove non-existent beam at ${pos}.");
			return;
		}
		Collection<ServerPlayerEntity> players = PlayerLookup.world(this.world.as());
		if (!players.isEmpty) {
			TogglePersistentBeamPacket packet = new TogglePersistentBeamPacket(false, removed.uuid, null);
			for (ServerPlayerEntity player : players) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.beamsByOrigin.clear();
		super.readFromNbt(tag);
	}
}