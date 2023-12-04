package builderb0y.bigtech.screenHandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

import builderb0y.bigtech.blockEntities.TransmuterBlockEntity;

public class TransmuterScreenHandler extends BigTechScreenHandler {

	public TransmuterScreenHandler(ScreenHandlerType<?> type, int syncId, Inventory inventory, Inventory playerInventory) {
		super(type, syncId, inventory);
		this.slotGrid()
		.pos(8, 142).size(9, 1).inventory(playerInventory).add()
		.pos(8, 84).size(9, 3).add()
		.pos(44, 17).size(5, 3).inventory(inventory).add();
	}

	public TransmuterScreenHandler(int syncId, Inventory playerInventory) {
		this(
			BigTechScreenHandlerTypes.TRANSMUTER,
			syncId,
			new SimpleInventory(15) {

				@Override
				public int getMaxCountPerStack() {
					return 1;
				}
			},
			playerInventory
		);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = this.slots.get(slotIndex);
		if (slot.hasStack()) {
			ItemStack stack = slot.getStack();
			if (slotIndex < 36) {
				if (TransmuterBlockEntity.isValidInput(player.world.recipeManager, stack)) {
					for (int slotIndex2 = 36; slotIndex2 < 36 + 15; slotIndex2++) {
						Slot slot2 = this.slots.get(slotIndex2);
						if (!slot2.hasStack()) {
							slot2.setStack(stack.split(1));
							if (stack.isEmpty) break;
						}
					}
					slot.onTakeItem(player, stack);
				}
			}
			else {
				this.insertItem(stack, 0, 36, false);
				slot.onTakeItem(player, stack);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}