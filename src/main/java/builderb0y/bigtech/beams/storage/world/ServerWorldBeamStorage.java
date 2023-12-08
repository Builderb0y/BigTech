package builderb0y.bigtech.beams.storage.world;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.networking.ToggleBeamPacket;

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
			ToggleBeamPacket packet = ToggleBeamPacket.add(beam);
			for (ServerPlayerEntity player : players) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	@Override
	public void addBeamNoSync(PersistentBeam beam) {
		PersistentBeam removed = this.beamsByOrigin.get(beam.origin);
		if (removed != null) {
			BigTechMod.LOGGER.warn("Replacing beam at ${beam.origin}: ${removed} -> ${beam}");
			removed.removeFromWorld();
		}
		super.addBeamNoSync(beam);
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
			ToggleBeamPacket packet = ToggleBeamPacket.remove(removed);
			for (ServerPlayerEntity player : players) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	@Override
	public void clear() {
		super.clear();
		this.beamsByOrigin.clear();
	}
}