package builderb0y.bigtech.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.mojang.serialization.DynamicOps;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;

public class Inventories2 {

	public static Stream<SlotStack> readItems(NbtList list, RegistryWrapper.WrapperLookup registryLookup) {
		return (
			list
			.stream()
			.map((NbtElement element) -> {
				if (element instanceof NbtCompound compound) {
					Byte slot = compound.getByte("Slot").orElse(null);
					ItemStack stack = ItemStack.fromNbt(registryLookup, compound).orElse(null);
					if (slot != null && stack != null) {
						return new SlotStack(slot, stack);
					}
				}
				return null;
			})
			.filter(Objects::nonNull)
		);
	}

	public static NbtList writeItems(Stream<SlotStack> items, RegistryWrapper.WrapperLookup registryLookup) {
		return (
			items
			.filter((SlotStack slotStack) -> !slotStack.stack.isEmpty())
			.map((SlotStack slotStack) ->
				slotStack
				.stack
				.toNbt(registryLookup)
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

	public static <T_Encoded> SlotStack readItem(T_Encoded input, DynamicOps<T_Encoded> ops) {
		return new SlotStack(
			ops.getNumberValue(ops.get(input, "Slot").getOrThrow()).getOrThrow().intValue(),
			ItemStack.CODEC.parse(ops, input).getOrThrow()
		);
	}

	public static <T_Encoded> Stream<SlotStack> readItems(T_Encoded input, DynamicOps<T_Encoded> ops) {
		return ops.getStream(input).getOrThrow().map((T_Encoded element) -> readItem(element, ops));
	}

	public static <T_Encoded> T_Encoded writeItem(SlotStack slotStack, DynamicOps<T_Encoded> ops) {
		return ops.mergeToMap(
			ItemStack.CODEC.encodeStart(ops, slotStack.stack()).getOrThrow(),
			ops.createString("Slot"),
			ops.createInt(slotStack.slot())
		)
		.getOrThrow();
	}

	public static <T_Encoded> T_Encoded writeItems(Stream<SlotStack> stacks, DynamicOps<T_Encoded> ops) {
		return ops.createList(stacks.map((SlotStack slotStack) -> writeItem(slotStack, ops)));
	}

	public static record SlotStack(int slot, ItemStack stack) {}

	public static Stream<SlotStack> stream(DefaultedList<ItemStack> stacks, boolean nonEmptyOnly) {
		Stream<SlotStack> stream = (
			IntStream
			.range(0, stacks.size())
			.mapToObj((int slot) -> new SlotStack(slot, stacks.get(slot)))
		);
		if (nonEmptyOnly) stream = stream.filter((SlotStack slotStack) -> !slotStack.stack().isEmpty());
		return stream;
	}

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