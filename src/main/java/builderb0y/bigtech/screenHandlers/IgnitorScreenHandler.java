package builderb0y.bigtech.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerType;

public class IgnitorScreenHandler extends BigTechScreenHandler {

	public final Property totalBurnTime, remainingBurnTime;

	public IgnitorScreenHandler(
		@Nullable ScreenHandlerType<?> type,
		int syncId,
		Inventory inventory,
		Inventory playerInventory,
		Property totalBurnTime,
		Property remainingBurnTime
	) {
		super(type, syncId, inventory);
		this.totalBurnTime = this.addProperty(totalBurnTime);
		this.remainingBurnTime = this.addProperty(remainingBurnTime);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar  = grid.pos( 8, 124).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos( 8,  66).size(9, 3)                           .add(),
			ignitorInv    = grid.pos(80,  35).size(1, 1).inventory(      inventory).add();

		this.shiftClickRules()
		.collect(stack(AbstractFurnaceBlockEntity::canUseAsFuel), ignitorInv.forward(), playerHotbar, playerStorage)
		.distribute(any(), ignitorInv, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward());
	}

	public IgnitorScreenHandler(int syncID, Inventory playerInventory) {
		this(
			BigTechScreenHandlerTypes.IGNITOR,
			syncID,
			new SimpleInventory(1),
			playerInventory,
			Property.create(),
			Property.create()
		);
	}
}