package builderb0y.bigtech.beams.storage.world;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.util.AsyncRunner;

public abstract class CommonWorldBeamStorage implements AutoSyncedComponent, ClientTickingComponent {

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
	public void writeToNbt(NbtCompound tag) {
		NbtList beamsTag = tag.createSubList("beams");
		for (PersistentBeam beam : this.beamsById.values()) {
			beamsTag
			.createCompound()
			.withIdentifier("type", BeamType.REGISTRY.getId(beam.type))
			.withUuid("uuid", beam.uuid)
			.withBlockPos("origin", beam.origin)
			.withLongArray("callbacks", beam.callbacks.stream().mapToLong(BlockPos::asLong).toArray())
			.withSubList("segment_sections", list -> {
				try (AsyncRunner async = new AsyncRunner()) {
					ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> sectionIterator = beam.seen.long2ObjectEntrySet().fastIterator();
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
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.clear();
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
				NbtList sectionsNbt = beamTag.getList("segment_sections", NbtElement.COMPOUND_TYPE);
				try (AsyncRunner async = new AsyncRunner()) {
					for (NbtCompound sectionNbt : sectionsNbt.<Iterable<NbtCompound>>as()) {
						long sectionPos = sectionNbt.getLong("pos");
						BasicSectionBeamStorage section = persistentBeam.seen.getSegments(sectionPos);
						async.run(() -> {
							section.readFromNbt(sectionNbt, persistentBeam);
						});
					}
				}
				Arrays.stream(beamTag.getLongArray("callbacks")).mapToObj(BlockPos::fromLong).forEach(persistentBeam.callbacks::add);
				this.addBeamNoSync(persistentBeam);
			}
			else {
				BigTechMod.LOGGER.warn("Skipping non-persistent beam ${typeID}");
			}
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
		this.clear();
		int count = buffer.readVarInt();
		for (int index = 0; index < count; index++) {
			BeamType type = buffer.readRegistryValue(BeamType.REGISTRY);
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