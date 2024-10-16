package builderb0y.bigtech.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class WorldSlot extends Slot {

	public WorldSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return this.getStack().isEmpty() && stack.getItem() instanceof BlockItem;
	}

	@Override
	public void onTakeItem(PlayerEntity player, ItemStack stack) {
		super.onTakeItem(player, stack);
		ItemStack removed = this.inventory.removeStack(this.getIndex());
		if (!removed.isEmpty()) {
			if (player.currentScreenHandler != null) {
				player.currentScreenHandler.setCursorStack(removed);
			}
			else {
				player.getInventory().offerOrDrop(removed);
			}
		}
	}
}