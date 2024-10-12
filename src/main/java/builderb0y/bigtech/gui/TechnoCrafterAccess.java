package builderb0y.bigtech.gui;

import java.util.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface TechnoCrafterAccess extends Inventory {

	public static TechnoCrafterAccess create() {
		return new TechnoCrafterAccess() {

			public boolean interactionSide;
			public final SplitStackList stacks = SplitStackList.create();

			@Override
			public boolean getInteractionSide() {
				return this.interactionSide;
			}

			@Override
			public void setInteractionSide(boolean interactionSide) {
				this.interactionSide = interactionSide;
			}

			@Override
			public DefaultedList<ItemStack> getHeldStacks() {
				return this.stacks.heldStacks();
			}

			@Override
			public DefaultedList<ItemStack> getStacks(boolean interactionSide) {
				return this.stacks.getStacks(interactionSide);
			}

			@Override
			public void markDirty() {

			}

			@Override
			public boolean canPlayerUse(PlayerEntity player) {
				return true;
			}
		};
	}

	/**
	false - interacting with the left side.
	true - interacting with the right side.
	*/
	public abstract boolean getInteractionSide();

	public abstract void setInteractionSide(boolean interactionSide);

	public DefaultedList<ItemStack> getHeldStacks();

	public DefaultedList<ItemStack> getStacks(boolean interactionSide);

	@Override
	public default int size() {
		return 18;
	}

	@Override
	public default boolean isEmpty() {
		return this.getHeldStacks().stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public default ItemStack getStack(int slot) {
		return this.getHeldStacks().get(slot);
	}

	@Override
	public default ItemStack removeStack(int slot, int amount) {
		ItemStack result = this.getHeldStacks().get(slot).split(amount);
		this.markDirty();
		return result;
	}

	@Override
	public default ItemStack removeStack(int slot) {
		ItemStack result = this.getHeldStacks().set(slot, ItemStack.EMPTY);
		this.markDirty();
		return result;
	}

	@Override
	public default void setStack(int slot, ItemStack stack) {
		this.getHeldStacks().set(slot, stack);
		this.markDirty();
	}

	@Override
	public default void clear() {
		Collections.fill(this.getHeldStacks(), ItemStack.EMPTY);
		this.markDirty();
	}

	public static record SplitStackList(
		DefaultedList<ItemStack> heldStacks,
		DefaultedList<ItemStack> leftStacks,
		DefaultedList<ItemStack> rightStacks
	) {

		public static SplitStackList create() {
			List<ItemStack> stacks = new ArrayView(18);
			return new SplitStackList(
				new DefaultedList<>(stacks, ItemStack.EMPTY),
				new DefaultedList<>(stacks.subList(0, 9), ItemStack.EMPTY),
				new DefaultedList<>(stacks.subList(9, 18), ItemStack.EMPTY)
			);
		}

		public static SplitStackList create(DefaultedList<ItemStack> stacks) {
			return new SplitStackList(
				stacks,
				new DefaultedList<>(stacks.delegate.subList(0, 9), ItemStack.EMPTY),
				new DefaultedList<>(stacks.delegate.subList(9, 18), ItemStack.EMPTY)
			);
		}

		public DefaultedList<ItemStack> getStacks(boolean interactionSide) {
			return interactionSide ? this.rightStacks : this.leftStacks;
		}
	}

	public static class ArrayView extends AbstractList<ItemStack> implements RandomAccess {

		public final ItemStack[] stacks;
		public final int offset, length;

		public ArrayView(ItemStack[] stacks, int offset, int length) {
			this.stacks = stacks;
			this.offset = offset;
			this.length = length;
		}

		public ArrayView(int length) {
			Arrays.fill(this.stacks = new ItemStack[this.length = length], ItemStack.EMPTY);
			this.offset = 0;
		}

		@Override
		public ItemStack get(int index) {
			return this.stacks[Objects.checkIndex(index, this.length) + this.offset];
		}

		@Override
		public int size() {
			return this.length;
		}

		@Override
		public ItemStack set(int index, ItemStack element) {
			ItemStack old = this.get(index);
			this.stacks[index + this.offset] = element;
			return old;
		}

		@Override
		public List<ItemStack> subList(int fromIndex, int toIndex) {
			return new ArrayView(this.stacks, Objects.checkFromToIndex(fromIndex, toIndex, this.length) + this.offset, toIndex - fromIndex);
		}
	}
}