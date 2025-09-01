package builderb0y.bigtech.gui.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerType;

public class LongRangeDeployerScreenHandler extends BigTechScreenHandler {

	public Property everywhere;

	public LongRangeDeployerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, PlayerInventory playerInventory, Property everywhere) {
		super(type, syncId, inventory, playerInventory);
		this.addProperty(this.everywhere = everywhere);
		SlotGrid slotGrid = this.slotGrid();
		SlotRange
			playerHotbar  = slotGrid.pos(8, 142).size(9, 1).inventory(playerInventory).add(),
			playerStorage = slotGrid.pos(8,  84).size(9, 3).add(),
			deployer      = slotGrid.pos(8,  17).size(3, 3).inventory(inventory).add();
		this
		.shiftClickRules()
		.collect(any(), deployer.forward(), playerHotbar, playerStorage)
		.distribute(any(), deployer, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward());
	}

	public LongRangeDeployerScreenHandler(int syncID, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.LONG_RANGE_DEPLOYER, syncID, new SimpleInventory(9), playerInventory, Property.create());
	}
}