package builderb0y.bigtech.screenHandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class SorterBeltScreenHandler extends BigTechScreenHandler {

	public SorterBeltScreenHandler(ScreenHandlerType<?> type, int syncId, Inventory inventory, Inventory playerInventory) {
		super(type, syncId, inventory);
		this.slotGrid()
		.pos(8, 142).size(9, 1).inventory(playerInventory).add()
		.pos(8, 84).size(9, 3).add()
		.pos(26, 17).size(3, 3).inventory(inventory).slotFactory(TemplateSlot::new).add()
		.pos(98, 17).add()
		;
	}

	public SorterBeltScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.SORTER_BELT, syncId, new SimpleInventory(18), playerInventory);
	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		vanilla:
		if (slotIndex >= 0) {
			Slot slot = this.slots.get(slotIndex);
			if (slot instanceof TemplateSlot) {
				switch (actionType) {
					case PICKUP -> slot.setStack(this.cursorStack.copyWithCount(1));
					case QUICK_MOVE, THROW, PICKUP_ALL -> slot.setStack(ItemStack.EMPTY);
					case SWAP, QUICK_CRAFT -> {}
					case CLONE -> { break vanilla; }
				}
				return;
			}
		}
		super.onSlotClick(slotIndex, button, actionType, player);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = this.slots.get(slotIndex);
		if (slot.hasStack()) {
			if (slotIndex < 9) {
				this.insertItem(slot.getStack(), 9, 27 + 9, false);
				slot.onTakeItem(player, slot.getStack());
			}
			else if (slotIndex < 27 + 9) {
				this.insertItem(slot.getStack(), 0, 9, false);
				slot.onTakeItem(player, slot.getStack());
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}