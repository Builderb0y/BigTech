package builderb0y.bigtech.beams.storage.world;

import java.util.Map;
import java.util.UUID;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.util.NbtReadingException;

public abstract class CommonWorldBeamStorage implements AutoSyncedComponent {

	public static final ComponentKey<CommonWorldBeamStorage> KEY = ComponentRegistry.getOrCreate(BigTechMod.modID("world_beam_storage"), CommonWorldBeamStorage.class);

	public final World world;
	public final Map<UUID, PersistentBeam> beamsById = new Object2ObjectOpenHashMap<>(32);

	public CommonWorldBeamStorage(World world) {
		this.world = world;
	}

	public void addBeam(PersistentBeam beam) {
		this.addBeamNoSync(beam);
	}

	public void addBeamNoSync(PersistentBeam beam) {
		Beam old = this.beamsById.putIfAbsent(beam.uuid, beam);
		if (old != null) {
			throw new IllegalArgumentException("Duplicate beam with ID ${beam.uuid}: ${old} -> ${beam}.");
		}
	}

	public void removeBeam(UUID uuid) {
		PersistentBeam removed = this.beamsById.remove(uuid);
		if (removed == null) {
			BigTechMod.LOGGER.warn("Attempt to remove non-existent beam ${uuid}.");
		}
	}

	public PersistentBeam getBeam(UUID uuid) {
		return this.beamsById.get(uuid);
	}

	public abstract PersistentBeam getBeam(BlockPos pos);

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList beamsTag = tag.createSubList("beams");
		for (PersistentBeam beam : this.beamsById.values()) {
			beam.writeToNbt(beamsTag.createCompound());
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		this.clear();
		NbtList beams = tag.getList("beams").orElse(null);
		if (beams != null) {
			for (NbtCompound beamTag : beams.<Iterable<NbtCompound>>as()) {
				Identifier typeID = beamTag.getIdentifier("type").orElse(null);
				if (typeID == null) {
					BigTechMod.LOGGER.warn("Skipping beam with no type");
					continue;
				}
				BeamType type = BeamType.REGISTRY.get(typeID);
				if (type == null) {
					BigTechMod.LOGGER.warn("Skipping beam with unknown type: ${typeID}");
					continue;
				}
				UUID uuid = beamTag.getUuid("uuid").orElse(null);
				if (uuid == null) {
					BigTechMod.LOGGER.warn("Skipping beam with no UUID: ${typeID}");
					continue;
				}
				Beam beam = type.factory.create(this.world, uuid);
				if (!(beam instanceof PersistentBeam persistentBeam)) {
					BigTechMod.LOGGER.warn("Skipping non-persistent beam ${typeID}");
					continue;
				}
				try {
					persistentBeam.readFromNbt(beamTag);
				}
				catch (NbtReadingException exception) {
					BigTechMod.LOGGER.warn("Skipping beam with malformed data:", exception);
					continue;
				}
				this.addBeamNoSync(persistentBeam);
			}
		}
	}

	@Override
	public void writeSyncPacket(RegistryByteBuf buffer, ServerPlayerEntity recipient) {
		int count = this.beamsById.size();
		buffer.writeVarInt(count);
		for (PersistentBeam beam : this.beamsById.values()) {
			buffer.writeRegistryValue(BeamType.REGISTRY_KEY, beam.getType());
			buffer.writeUuid(beam.uuid);
		}
	}

	@Override
	public void applySyncPacket(RegistryByteBuf buffer) {
		this.clear();
		int count = buffer.readVarInt();
		for (int index = 0; index < count; index++) {
			BeamType type = buffer.readRegistryValue(BeamType.REGISTRY_KEY);
			if (type == null) {
				BigTechMod.LOGGER.warn("Received beam of unknown type.");
				continue;
			}
			UUID uuid = buffer.readUuid();
			Beam beam = type.factory.create(this.world, uuid);
			if (!(beam instanceof PersistentBeam persistentBeam)) {
				BigTechMod.LOGGER.warn("Received non-persistent beam??? ${BeamType.REGISTRY.getId(type)}");
				continue;
			}
			this.addBeamNoSync(persistentBeam);
		}
	}

	public void clear() {
		this.beamsById.clear();
	}
}