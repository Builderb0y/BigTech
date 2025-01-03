package builderb0y.bigtech.gui.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;

import builderb0y.bigtech.entities.MinerEntity;

public class MinerScreenHandler extends BigTechScreenHandler {

	public final Property fuelTicks;

	public MinerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, PlayerInventory playerInventory, Property fuelTicks) {
		super(type, syncId, inventory, playerInventory);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar  = grid.pos(  8, 198).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos(  8, 140).size(9, 3)                           .add(),
			mainInventory = grid.pos(  8,  18).size(9, 6).inventory(inventory)      .add(),
			fuel          = grid.pos(176, 176).size(1, 1)                           .add(),
			smeltingInput = grid.pos(176, 140)                                      .add(),
			trash         = grid.pos(176, 198).inventory(TrashInventory.INSTANCE)   .add();

		this
		.shiftClickRules()
		.collect(playerStack((PlayerEntity player, ItemStack stack) -> player.getWorld() instanceof ServerWorld serverWorld && serverWorld.getFuelRegistry().isFuel(stack)), fuel.forward(), playerHotbar, playerStorage)
		.distribute(any(), playerHotbar,  mainInventory.forward(), playerStorage.forward())
		.distribute(any(), playerStorage, mainInventory.forward(), playerHotbar.forward())
		.distribute(any(), mainInventory, playerStorage.forward(), playerHotbar.forward())
		.distribute(any(), fuel,          playerStorage.forward(), playerHotbar.forward(), mainInventory.forward())
		.distribute(any(), smeltingInput, playerStorage.forward(), playerHotbar.forward(), mainInventory.forward())
		;

		this.fuelTicks = this.addProperty(fuelTicks);
	}

	public MinerScreenHandler(int syncID, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.MINER, syncID, new SimpleInventory(MinerEntity.TOTAL_SIZE), playerInventory, Property.create());
	}
}