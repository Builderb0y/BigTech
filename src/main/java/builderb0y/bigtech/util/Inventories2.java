package builderb0y.bigtech.util;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;

public class Inventories2 {

	public static Stream<SlotStack> readItems(NbtList list, RegistryWrapper.WrapperLookup registryLookup) {
		if (list.getHeldType() == NbtElement.COMPOUND_TYPE) {
			return (
				list
				.stream()
				.map(NbtCompound.class::cast)
				.map((NbtCompound compound) -> new SlotStack(
					Byte.toUnsignedInt(compound.getByte("Slot")),
					ItemStack.fromNbtOrEmpty(registryLookup, compound)
				))
			);
		}
		else {
			return Stream.empty();
		}
	}

	public static NbtList writeItems(Stream<SlotStack> items, RegistryWrapper.WrapperLookup registryLookup) {
		return (
			items
			.map((SlotStack slotStack) ->
				slotStack
				.stack
				.encodeAllowEmpty(registryLookup)
				.<NbtCompound>as()
				.withInt("Slot", slotStack.slot)
			)
			.collect(
				Collectors.toCollection(
					NbtList::new
				)
			)
		);
	}

	public static record SlotStack(int slot, ItemStack stack) {}

	public static Stream<SlotStack> stream(Inventory inventory, boolean nonEmptyOnly) {
		Stream<SlotStack> stream = (
			IntStream
			.range(0, inventory.size())
			.mapToObj((int slot) -> new SlotStack(slot, inventory.getStack(slot)))
		);
		if (nonEmptyOnly) stream = stream.filter((SlotStack slotStack) -> !slotStack.stack().isEmpty());
		return stream;
	}

	public static Consumer<SlotStack> setter(Inventory inventory) {
		return (SlotStack slotStack) -> {
			if (slotStack.slot() >= 0 && slotStack.slot() < inventory.size()) {
				inventory.setStack(slotStack.slot(), slotStack.stack());
			}
		};
	}
}