package builderb0y.bigtech.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

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

		this.slotGrid()
		.pos( 8, 124).size(9, 1).inventory(playerInventory).add()
		.pos( 8,  66).size(9, 3)                           .add()
		.pos(80,  35).size(1, 1).inventory(      inventory).add()
		;
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

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = this.slots.get(slotIndex);
		if (slot.hasStack()) {
			ItemStack stack = slot.getStack();
			if (slotIndex < 36) {
				if (AbstractFurnaceBlockEntity.canUseAsFuel(stack)) {
					this.insertItem(stack, 36, 37, false);
				}
			}
			else {
				this.insertItem(stack, 0, 36, false);
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