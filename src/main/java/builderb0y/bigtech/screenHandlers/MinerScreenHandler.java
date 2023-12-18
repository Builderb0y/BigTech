package builderb0y.bigtech.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

import builderb0y.bigtech.entities.MinerEntity;

public class MinerScreenHandler extends BigTechScreenHandler {

	public static final int
		PLAYER_START   = 0,
		PLAYER_SIZE    = 9 * 4,
		PLAYER_END     = PLAYER_START + PLAYER_SIZE,
		STORAGE_START  = PLAYER_END,
		STORAGE_SIZE   = MinerEntity.STORAGE_SIZE,
		STORAGE_END    = STORAGE_START + STORAGE_SIZE,
		FUEL_START     = STORAGE_END,
		FUEL_SIZE      = MinerEntity.FUEL_SIZE,
		FUEL_END       = FUEL_START + FUEL_SIZE,
		SMELTING_START = FUEL_END,
		SMELTING_SIZE  = MinerEntity.SMELTING_SIZE,
		SMELTING_END   = SMELTING_START + SMELTING_SIZE,
		TRASH_START    = SMELTING_END,
		TRASH_SIZE     = 1,
		TRASH_END      = TRASH_START + TRASH_SIZE,
		TOTAl_SIZE     = TRASH_END;

	public final Property fuelTicks;

	public MinerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, Inventory playerInventory, Property fuelTicks) {
		super(type, syncId, inventory);
		this.slotGrid()
		.pos(  8, 198).size(9, 1).inventory(playerInventory).add()
		.pos(  8, 140).size(9, 3).add()
		.pos(  8,  18).size(9, 6).inventory(inventory).add()
		.pos(176, 176).size(1, 1).add()
		.pos(176, 140).size(1, 1).add()
		.pos(176, 198).inventory(TrashInventory.INSTANCE).add();
		this.fuelTicks = this.addProperty(fuelTicks);
	}

	public MinerScreenHandler(int syncID, Inventory playerInventory) {
		this(BigTechScreenHandlerTypes.MINER, syncID, new SimpleInventory(MinerEntity.TOTAL_SIZE), playerInventory, Property.create());
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = this.slots.get(slotIndex);
		if (slot.hasStack()) {
			ItemStack stack = slot.getStack();
			if (slotIndex >= PLAYER_START && slotIndex < PLAYER_END) {
				if (AbstractFurnaceBlockEntity.canUseAsFuel(stack)) {
					this.insertItem(stack, FUEL_START, FUEL_END, false);
				}
				this.insertItem(stack, STORAGE_START, STORAGE_END, false);
			}
			else {
				this.insertItem(stack, PLAYER_START, PLAYER_END, false);
			}
			slot.onTakeItem(player, stack);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}