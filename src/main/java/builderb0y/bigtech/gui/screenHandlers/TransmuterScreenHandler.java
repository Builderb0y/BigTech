package builderb0y.bigtech.gui.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;

import builderb0y.bigtech.blockEntities.TransmuterBlockEntity;

public class TransmuterScreenHandler extends BigTechScreenHandler {

	public TransmuterScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, PlayerInventory playerInventory) {
		super(type, syncId, inventory, playerInventory);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar = grid.pos(8, 142).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos(8, 84).size(9, 3).add(),
			transmuter = grid.pos(44, 17).size(5, 3).inventory(inventory).add();

		this.shiftClickRules()
		.collect(playerStack((PlayerEntity player, ItemStack stack) -> player.getWorld() instanceof ServerWorld serverWorld && TransmuterBlockEntity.isValidInput(serverWorld.getRecipeManager(), stack)), transmuter.forward(), playerHotbar, playerStorage)
		.distribute(any(), transmuter, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward())
		;
	}

	public TransmuterScreenHandler(int syncId, PlayerInventory playerInventory) {
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