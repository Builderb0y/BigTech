package builderb0y.bigtech.gui.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import builderb0y.bigtech.gui.HeldItemInventory.SimpleHeldItemInventory;
import builderb0y.bigtech.items.WorldInventory;
import builderb0y.bigtech.items.WorldSlot;

public class DislocatorScreenHandler extends BigTechScreenHandler {

	public DislocatorScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, PlayerInventory playerInventory) {
		super(type, syncId, inventory, playerInventory);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar  = grid.pos( 8, 182).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos( 8, 124).size(9, 3)                           .add(),
			worldInv      = grid.pos(62,  17).size(3, 3).inventory(inventory).slotFactory(WorldSlot::new).add();

		this
		.shiftClickRules()
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward())
		;
	}

	public DislocatorScreenHandler(int syncID, PlayerInventory playerInventory, byte usingSlot) {
		this(BigTechScreenHandlerTypes.DISLOCATOR, syncID, new SimpleHeldItemInventory(9, usingSlot), playerInventory);
	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (slotIndex >= 36 && slotIndex < 36 + 9) {
			if (player.getWorld().isClient) {
				return;
			}
			Slot slot = this.slots.get(slotIndex);
			switch (actionType) {
				case PICKUP -> {
					ItemStack oldStack = slot.getStack();
					ItemStack newStack = this.getCursorStack();
					if (!oldStack.isEmpty()) {
						if (!newStack.isEmpty()) {
							if (newStack.getItem() instanceof BlockItem && newStack.getCount() == 1) {
								this.setCursorStack(slot.takeStack(64));
								slot.setStack(newStack);
							}
							else {
								//do nothing.
							}
						}
						else {
							this.setCursorStack(slot.takeStack(64));
						}
					}
					else {
						if (!newStack.isEmpty()) {
							if (newStack.getItem() instanceof BlockItem) {
								slot.setStack(newStack);
							}
						}
						else {
							//do nothing.
						}
					}
				}
				case QUICK_MOVE -> {
					ItemStack stack = slot.takeStack(64);
					if (!stack.isEmpty()) {
						Destination destination = new Destination(new SlotRange(0, 36), false);
						this.insertStack(stack, destination, true);
						if (!stack.isEmpty()) {
							this.insertStack(stack, destination, false);
							if (!stack.isEmpty()) {
								player.dropItem(stack, true);
							}
						}
					}
				}
				case SWAP -> {
					ItemStack existing = this.playerInventory.getStack(button);
					if (slot.hasStack()) {
						if (!existing.isEmpty()) {
							if (existing.getItem() instanceof BlockItem) {
								ItemStack taken = slot.takeStack(64);
								slot.setStack(existing.split(1));
								this.playerInventory.setStack(button, taken);
								if (!taken.isEmpty()) {
									this.playerInventory.offerOrDrop(taken);
								}
							}
						}
						else {
							ItemStack taken = slot.takeStack(64);
							if (existing.isEmpty()) {
								this.playerInventory.setStack(button, taken);
							}
							else {
								this.playerInventory.offerOrDrop(taken);
							}
						}
					}
					else {
						if (!existing.isEmpty()) {
							if (existing.getItem() instanceof BlockItem) {
								slot.insertStack(existing.split(1));
							}
							else {
								//do nothing.
							}
						}
						else {
							//do nothing.
						}
					}
				}
				case CLONE -> {
					if (this.getCursorStack().isEmpty()) {
						ItemStack existing = slot.getStack();
						this.setCursorStack(existing.copyWithCount(existing.getMaxCount()));
					}
				}
				case THROW -> {
					((WorldInventory)(this.inventory)).drop(slot.getIndex());
				}
				case QUICK_CRAFT, PICKUP_ALL -> {}
			}
		}
		else {
			super.onSlotClick(slotIndex, button, actionType, player);
		}
	}
}