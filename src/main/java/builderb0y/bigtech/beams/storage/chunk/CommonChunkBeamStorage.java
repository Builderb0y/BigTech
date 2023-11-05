package builderb0y.bigtech.beams.storage.chunk;

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

public abstract class CommonChunkBeamStorage extends Int2ObjectOpenHashMap<CommonSectionBeamStorage> {

	public final WorldChunk chunk;

	public CommonChunkBeamStorage(WorldChunk chunk) {
		super(4);
		this.chunk = chunk;
	}

	public abstract CommonSectionBeamStorage newSection(int sectionCoordY);

	public CommonSectionBeamStorage getSection(int sectionCoordY) {
		return this.computeIfAbsent(sectionCoordY, this::newSection);
	}

	public void tick() {
		if (!this.isEmpty) this.values().forEach(CommonSectionBeamStorage::tick);
	}

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
				CommonSectionBeamStorage section = this.newSection(actualCoord);
				CommonSectionBeamStorage old = this.putIfAbsent(actualCoord, section);
				if (old != null) {
					async.run(() -> section.readFromNbt(sectionNbt));
				}
				else {
					BigTechMod.LOGGER.warn("Skipping beam section storage with duplicate coord: ${actualCoord}");
				}
			}
		}
	}

	public void writeToNbt(NbtCompound tag) {
		if (!this.isEmpty) {
			NbtList sectionsNbt = new NbtList();
			try (Async async = new Async()) {
				ObjectIterator<Int2ObjectMap.Entry<CommonSectionBeamStorage>> iterator = this.int2ObjectEntrySet().fastIterator();
				while (iterator.hasNext()) {
					Int2ObjectMap.Entry<CommonSectionBeamStorage> entry = iterator.next();
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
	public void copyFrom(CommonChunkBeamStorage other) {
		this.clear();
		if (!other.isEmpty) {
			try (Async async = new Async()) {
				ObjectIterator<Entry<CommonSectionBeamStorage>> iterator = other.int2ObjectEntrySet().fastIterator();
				while (iterator.hasNext()) {
					Entry<CommonSectionBeamStorage> entry = iterator.next();
					CommonSectionBeamStorage thisSection = this.getSection(entry.intKey);
					CommonSectionBeamStorage thatSection = entry.value;
					async.run(() -> thisSection.addAll(thatSection, false));
				}
			}
		}
	}
}