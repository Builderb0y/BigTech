package builderb0y.bigtech.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class Inventories2 {

	public static Stream<SlotStack> readItems(NbtList list) {
		if (list.heldType == NbtElement.COMPOUND_TYPE) {
			return (
				list
				.stream()
				.map(NbtCompound.class::cast)
				.map(compound -> new SlotStack(
					Byte.toUnsignedInt(compound.getByte("Slot")),
					ItemStack.fromNbt(compound)
				))
			);
		}
		else {
			return Stream.empty();
		}
	}

	public static NbtList writeItems(Stream<SlotStack> items) {
		return (
			items
			.map(slotStack ->
				slotStack
				.stack
				.writeNbt(new NbtCompound())
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
}