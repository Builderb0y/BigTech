package builderb0y.bigtech.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class SpawnerInterceptorScreenHandler extends BigTechScreenHandler {

	public SpawnerInterceptorScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, Inventory playerInventory) {
		super(type, syncId, inventory);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar   = grid.pos(  8, 109).size(9, 1).inventory(playerInventory).add(),
			playerStorage  = grid.pos(  8,  51).size(9, 3).add(),
			eggInput       = grid.pos( 44,  20).size(1, 1).inventory(inventory).add(),
			spawnEggOutput = grid.pos(116,  20).slotFactory(OutputSlot::new).add();

		this.shiftClickRules()
		.collect(itemIs(Items.EGG), eggInput.forward(), playerHotbar, playerStorage)
		.distribute(any(), eggInput, playerHotbar.forward(), playerStorage.forward())
		.distribute(any(), spawnEggOutput, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward())
		;
	}

	public SpawnerInterceptorScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.SPAWNER_INTERCEPTOR, syncId, new SimpleInventory(2), playerInventory);
	}

	public static class OutputSlot extends Slot {

		public OutputSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return false;
		}
	}
}