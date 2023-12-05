package builderb0y.bigtech.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

import builderb0y.bigtech.blockEntities.LongRangeDestroyerBlockEntity;

public class DestroyerScreenHandler extends BigTechScreenHandler {

	public DestroyerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, Inventory playerInventory) {
		super(type, syncId, inventory);
		this.slotGrid()
		.pos( 8, 106).size(9, 1).inventory(playerInventory).add()
		.pos( 8,  48).size(9, 3)                           .add()
		.pos(80,  17).size(1, 1).inventory(      inventory).add()
		;
	}

	public DestroyerScreenHandler(int syncID, Inventory playerInventory) {
		this(
			BigTechScreenHandlerTypes.DESTROYER,
			syncID,
			new SimpleInventory(1),
			playerInventory
		);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = this.slots.get(slotIndex);
		if (slot.hasStack()) {
			ItemStack stack = slot.getStack();
			if (slotIndex < 36) {
				if (LongRangeDestroyerBlockEntity.OVERRIDES_IS_SUITABLE_FOR.get(stack.item.getClass())) {
					this.insertItem(stack, 36, 37, false);
				}
			}
			else {
				this.insertItem(stack, 0, 36, false);
			}
			slot.onTakeItem(player, stack);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}