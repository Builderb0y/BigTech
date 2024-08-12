package builderb0y.bigtech.screenHandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;

import builderb0y.bigtech.blockEntities.TransmuterBlockEntity;

public class TransmuterScreenHandler extends BigTechScreenHandler {

	public TransmuterScreenHandler(ScreenHandlerType<?> type, int syncId, Inventory inventory, Inventory playerInventory) {
		super(type, syncId, inventory);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar = grid.pos(8, 142).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos(8, 84).size(9, 3).add(),
			transmuter = grid.pos(44, 17).size(5, 3).inventory(inventory).add();

		this.shiftClickRules()
		.collect(playerStack((PlayerEntity player, ItemStack stack) -> TransmuterBlockEntity.isValidInput(player.getWorld().getRecipeManager(), stack)), transmuter.forward(), playerHotbar, playerStorage)
		.distribute(any(), transmuter, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward())
		;
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
}