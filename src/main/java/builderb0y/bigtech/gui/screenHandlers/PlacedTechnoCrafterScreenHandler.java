package builderb0y.bigtech.gui.screenHandlers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

import builderb0y.bigtech.gui.TechnoCrafterAccess;
import builderb0y.bigtech.gui.TechnoCrafterAccess.SplitStackList;

public class PlacedTechnoCrafterScreenHandler extends TechnoCrafterScreenHandler {

	public PlacedTechnoCrafterScreenHandler(ScreenHandlerType<?> screenHandlerType, int syncID, TechnoCrafterAccess crafter, PlayerInventory playerInventory) {
		super(screenHandlerType, syncID, crafter, playerInventory);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar      = grid.pos(  8, 209).size(9, 1).inventory(playerInventory).add(),
			playerStorage     = grid.pos(  8, 151).size(9, 3)                           .add(),
			crafterStorage    = grid.pos(  8,  84)           .inventory(crafter)        .add(),
			leftGrid          = grid.pos(  8,  17).size(3, 3).inventory(this.leftGrid)  .add(),
			rightGrid         = grid.pos(116,  17)           .inventory(this.rightGrid) .add(),
			leftTopOutput     = grid.pos( 80,  17).size(1, 1).inventory(this.leftTopResult    ).slotFactory(this.resultSlotFactory(false)).add(),
			rightBottomOutput = grid.pos( 80,  53)           .inventory(this.rightBottomResult).slotFactory(this.resultSlotFactory(true )).add(),
			trash             = grid.pos(176, 209)           .inventory(TrashInventory.INSTANCE).slotFactory(Slot::new).add();
		this
		.shiftClickRules
		.collect(when(() -> !crafter.getInteractionSide()),  leftGrid.forward(), playerHotbar, playerStorage, crafterStorage)
		.collect(when(() ->  crafter.getInteractionSide()), rightGrid.forward(), playerHotbar, playerStorage, crafterStorage)
		.builder().from(leftGrid, rightGrid).to(crafterStorage.forward(), playerStorage.forward(), playerHotbar.forward()).add()
		.builder().from(leftTopOutput, rightBottomOutput).to(playerHotbar.forward(), playerStorage.forward(), crafterStorage.forward()).add()
		;
	}

	public PlacedTechnoCrafterScreenHandler(int syncID, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.PLACED_TECHNO_CRAFTER, syncID, new TechnoCrafterAccess.Impl(SplitStackList.createPlaced()), playerInventory);
	}
}