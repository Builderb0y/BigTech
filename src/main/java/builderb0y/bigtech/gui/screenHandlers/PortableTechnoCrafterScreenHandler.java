package builderb0y.bigtech.gui.screenHandlers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

import builderb0y.bigtech.gui.TechnoCrafterAccess;
import builderb0y.bigtech.gui.TechnoCrafterAccess.SplitStackList;

public class PortableTechnoCrafterScreenHandler extends TechnoCrafterScreenHandler {

	public PortableTechnoCrafterScreenHandler(ScreenHandlerType<?> screenHandlerType, int syncID, TechnoCrafterAccess crafter, PlayerInventory playerInventory) {
		super(screenHandlerType, syncID, crafter, playerInventory);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar      = grid.pos(  8, 142).size(9, 1).inventory(playerInventory).add(),
			playerStorage     = grid.pos(  8,  84).size(9, 3)                           .add(),
			leftGrid          = grid.pos(  8,  17).size(3, 3).inventory(this.leftGrid)  .add(),
			rightGrid         = grid.pos(116,  17)           .inventory(this.rightGrid) .add(),
			leftTopOutput     = grid.pos( 80,  17).size(1, 1).inventory(this.leftTopResult    ).slotFactory(this.resultSlotFactory(false)).add(),
			rightBottomOutput = grid.pos( 80,  53)           .inventory(this.rightBottomResult).slotFactory(this.resultSlotFactory(true )).add(),
			trash             = grid.pos(176, 142)           .inventory(TrashInventory.INSTANCE).slotFactory(Slot::new).add();
		this
			.shiftClickRules
			.collect(when(() -> !crafter.getInteractionSide()),  leftGrid.forward(), playerHotbar, playerStorage)
			.collect(when(() ->  crafter.getInteractionSide()), rightGrid.forward(), playerHotbar, playerStorage)
			.builder().from(leftGrid, rightGrid, leftTopOutput, rightBottomOutput).to(playerHotbar.forward(), playerStorage.forward()).add()
		;
	}

	public PortableTechnoCrafterScreenHandler(int syncID, PlayerInventory playerInventory, byte heldSlot) {
		this(BigTechScreenHandlerTypes.PORTABLE_TECHNO_CRAFTER, syncID, new TechnoCrafterAccess.HeldImpl(SplitStackList.createPortable(), heldSlot), playerInventory);
	}
}