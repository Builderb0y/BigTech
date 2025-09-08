package builderb0y.bigtech.util;

import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.ReadView.TypedListReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.collection.DefaultedList;

public class Inventories2 {

	public static Stream<StackWithSlot> readItems(ReadView view, String key) {
		return (
			view
			.getOptionalTypedListView(key, StackWithSlot.CODEC)
			.map(TypedListReadView<StackWithSlot>::stream)
			.orElseGet(Stream::empty)
		);
	}

	public static void writeItems(WriteView view, String key, Stream<StackWithSlot> stream) {
		stream.forEach(view.getListAppender(key, StackWithSlot.CODEC)::add);
	}

	public static Stream<StackWithSlot> stream(DefaultedList<ItemStack> stacks, boolean nonEmptyOnly) {
		Stream<StackWithSlot> stream = (
			IntStream
			.range(0, stacks.size())
			.mapToObj((int slot) -> new StackWithSlot(slot, stacks.get(slot)))
		);
		if (nonEmptyOnly) stream = stream.filter((StackWithSlot slotStack) -> !slotStack.stack().isEmpty());
		return stream;
	}

	public static Stream<StackWithSlot> stream(Inventory inventory, boolean nonEmptyOnly) {
		Stream<StackWithSlot> stream = (
			IntStream
			.range(0, inventory.size())
			.mapToObj((int slot) -> new StackWithSlot(slot, inventory.getStack(slot)))
		);
		if (nonEmptyOnly) stream = stream.filter((StackWithSlot slotStack) -> !slotStack.stack().isEmpty());
		return stream;
	}

	public static Consumer<StackWithSlot> setter(Inventory inventory) {
		return (StackWithSlot slotStack) -> {
			if (slotStack.slot() >= 0 && slotStack.slot() < inventory.size()) {
				inventory.setStack(slotStack.slot(), slotStack.stack());
			}
		};
	}

	public static Consumer<StackWithSlot> setter(DefaultedList<ItemStack> stacks) {
		return (StackWithSlot stackWithSlot) -> {
			if (stackWithSlot.slot() >= 0 && stackWithSlot.slot() < stacks.size()) {
				stacks.set(stackWithSlot.slot(), stackWithSlot.stack());
			}
		};
	}
}