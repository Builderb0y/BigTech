package builderb0y.bigtech.beams.storage.world;

import java.util.Map;
import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.Beam;
import builderb0y.bigtech.beams.BeamType;
import builderb0y.bigtech.beams.PersistentBeam;

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

	public abstract void removeBeam(BlockPos pos);

	public PersistentBeam getBeam(UUID uuid) {
		return this.beamsById.get(uuid);
	}

	public abstract PersistentBeam getBeam(BlockPos pos);

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.beamsById.clear();
		for (NbtCompound beamTag : tag.getList("beams", NbtElement.COMPOUND_TYPE).<Iterable<NbtCompound>>as()) {
			Identifier typeID = beamTag.getIdentifier("type");
			BeamType type = BeamType.REGISTRY.get(typeID);
			if (type == null) {
				BigTechMod.LOGGER.warn("Skipping beam with unknown type ${typeID}");
				continue;
			}
			UUID uuid = beamTag.getUuid("uuid");
			Beam beam = type.factory.create(this.world, uuid);
			if (beam instanceof PersistentBeam persistentBeam) {
				persistentBeam.origin = beamTag.getBlockPos("origin");
				this.addBeamNoSync(persistentBeam);
			}
			else {
				BigTechMod.LOGGER.warn("Skipping non-persistent beam ${typeID}");
			}
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList beamsTag = tag.createSubList("beams");
		for (PersistentBeam beam : this.beamsById.values()) {
			beamsTag
			.createCompound()
			.withIdentifier("type", BeamType.REGISTRY.getId(beam.type))
			.withUuid("uuid", beam.uuid)
			.withBlockPos("origin", beam.origin)
			;
		}
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buffer, ServerPlayerEntity recipient) {
		int count = this.beamsById.size();
		buffer.writeVarInt(count);
		for (PersistentBeam beam : this.beamsById.values()) {
			buffer.writeRegistryValue(BeamType.REGISTRY, beam.type);
			buffer.writeUuid(beam.uuid);
		}
	}

	@Override
	public void applySyncPacket(PacketByteBuf buffer) {
		this.beamsById.clear();
		int count = buffer.readVarInt();
		for (int index = 0; index < count; index++) {
			BeamType type = buffer.readRegistryValue(BeamType.REGISTRY);
			UUID uuid = buffer.readUuid();
			Beam beam = type.factory.create(this.world, uuid);
			if (beam instanceof PersistentBeam persistentBeam) {
				this.addBeamNoSync(persistentBeam);
			}
			else {
				BigTechMod.LOGGER.warn("Received non-persistent beam??? ${BeamType.REGISTRY.getId(type)}");
			}
		}
	}
}