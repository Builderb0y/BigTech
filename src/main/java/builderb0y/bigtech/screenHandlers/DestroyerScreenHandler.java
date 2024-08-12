package builderb0y.bigtech.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;

public class DestroyerScreenHandler extends BigTechScreenHandler {

	public DestroyerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, Inventory playerInventory) {
		super(type, syncId, inventory);
		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar  = grid.pos( 8, 106).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos( 8,  48).size(9, 3)                           .add(),
			destroyerInv  = grid.pos(80,  17).size(1, 1).inventory(      inventory).add();
		this.shiftClickRules()
		.collect(stack((ItemStack stack) -> stack.get(DataComponentTypes.TOOL) != null), destroyerInv.forward(), playerHotbar, playerStorage)
		.distribute(any(), destroyerInv, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward());
	}

	public DestroyerScreenHandler(int syncID, Inventory playerInventory) {
		this(
			BigTechScreenHandlerTypes.DESTROYER,
			syncID,
			new SimpleInventory(1),
			playerInventory
		);
	}
}