package builderb0y.bigtech.gui.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;

public class IgniterScreenHandler extends BigTechScreenHandler {

	public final Property totalBurnTime, remainingBurnTime;

	public IgniterScreenHandler(
		@Nullable ScreenHandlerType<?> type,
		int syncId,
		Inventory inventory,
		PlayerInventory playerInventory,
		Property totalBurnTime,
		Property remainingBurnTime
	) {
		super(type, syncId, inventory, playerInventory);
		this.totalBurnTime = this.addProperty(totalBurnTime);
		this.remainingBurnTime = this.addProperty(remainingBurnTime);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar  = grid.pos( 8, 124).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos( 8,  66).size(9, 3)                           .add(),
			igniterInv    = grid.pos(80,  35).size(1, 1).inventory(      inventory).add();

		this.shiftClickRules()
		.collect(playerStack((PlayerEntity player, ItemStack stack) -> player.getWorld() instanceof ServerWorld serverWorld && serverWorld.getFuelRegistry().isFuel(stack)), igniterInv.forward(), playerHotbar, playerStorage)
		.distribute(any(), igniterInv, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward());
	}

	public IgniterScreenHandler(int syncID, PlayerInventory playerInventory) {
		this(
			BigTechScreenHandlerTypes.IGNITER,
			syncID,
			new SimpleInventory(1),
			playerInventory,
			Property.create(),
			Property.create()
		);
	}
}