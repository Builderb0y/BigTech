package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public abstract class LootableBlockEntityThatActuallyHasAnInventory extends LootableBlockEntityThatReadsAndWritesToNbtProperly {

	public DefaultedList<ItemStack> heldStacks;

	public LootableBlockEntityThatActuallyHasAnInventory(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, DefaultedList<ItemStack> heldStacks) {
		super(blockEntityType, blockPos, blockState);
		this.heldStacks = heldStacks;
	}

	public LootableBlockEntityThatActuallyHasAnInventory(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int size) {
		this(blockEntityType, pos, state, DefaultedList.ofSize(size, ItemStack.EMPTY));
	}

	@Override
	public DefaultedList<ItemStack> getHeldStacks() {
		return this.heldStacks;
	}

	@Override
	public void setHeldStacks(DefaultedList<ItemStack> inventory) {
		this.heldStacks = inventory;
	}
}