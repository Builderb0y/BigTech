package builderb0y.bigtech.beams.storage.chunk;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.util.Async;

public abstract class CommonChunkBeamStorage<T_Section extends CommonSectionBeamStorage> extends Int2ObjectOpenHashMap<T_Section> implements CommonTickingComponent, CopyableComponent<CommonChunkBeamStorage<T_Section>> {

	public static final ComponentKey<CommonChunkBeamStorage<?>> KEY = ComponentRegistry.getOrCreate(BigTechMod.modID("chunk_beam_storage"), CommonChunkBeamStorage.class.as());

	public final WorldChunk chunk;

	public CommonChunkBeamStorage(WorldChunk chunk) {
		super(4);
		this.chunk = chunk;
	}

	public abstract T_Section newSection(int sectionCoordY);

	public T_Section getSection(int sectionCoordY) {
		return this.computeIfAbsent(sectionCoordY, this::newSection);
	}

	@Override
	public void tick() {
		this.values().forEach(T_Section::tick);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.clear();
		NbtList sectionsNbt = tag.getList("sections", NbtElement.COMPOUND_TYPE);
		if (!sectionsNbt.isEmpty) try (Async async = new Async()) {
			for (NbtCompound sectionNbt : sectionsNbt.<Iterable<NbtCompound>>as()) {
				NbtElement coord = sectionNbt.get("coord");
				if (!(coord instanceof AbstractNbtNumber)) {
					BigTechMod.LOGGER.warn("Skipping beam section storage with unknown coord");
					continue;
				}
				int actualCoord = coord.<AbstractNbtNumber>as().intValue();
				T_Section section = this.newSection(actualCoord);
				T_Section old = this.putIfAbsent(actualCoord, section);
				if (old != null) {
					async.run(() -> section.readFromNbt(sectionNbt));
				}
				else {
					BigTechMod.LOGGER.warn("Skipping beam section storage with duplicate coord: ${actualCoord}");
				}
			}
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		if (!this.isEmpty) {
			NbtList sectionsNbt = new NbtList();
			try (Async async = new Async()) {
				ObjectIterator<Int2ObjectMap.Entry<T_Section>> iterator = this.int2ObjectEntrySet().fastIterator();
				while (iterator.hasNext()) {
					Int2ObjectMap.Entry<T_Section> entry = iterator.next();
					if (!entry.value.isEmpty) {
						NbtCompound compound = new NbtCompound().withInt("coord", entry.intKey);
						sectionsNbt.add(compound);
						async.run(() -> entry.value.writeToNbt(compound));
					}
				}
			}
			tag.put("sections", sectionsNbt);
		}
	}

	/**
	will be called automatically for ProtoChunk -> Chunk conversion.
	while ProtoChunk's will never have beams in them,
	it is possible that other places could call this too,
	and I want this to work faster than the default implementation,
	which simply serializes and deserializes from NBT.
	*/
	@Override
	public void copyFrom(CommonChunkBeamStorage<T_Section> other) {
		this.clear();
		if (!other.isEmpty && !(other instanceof DummyChunkBeamStorage)) {
			try (Async async = new Async()) {
				ObjectIterator<Entry<T_Section>> iterator = other.int2ObjectEntrySet().fastIterator();
				while (iterator.hasNext()) {
					Entry<T_Section> entry = iterator.next();
					T_Section thisSection = this.getSection(entry.intKey);
					T_Section thatSection = entry.value;
					async.run(() -> thisSection.addAll(thatSection, false));
				}
			}
		}
	}
}