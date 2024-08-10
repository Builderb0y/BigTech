package builderb0y.bigtech.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class SilverIodideCannonScreenHandler extends BigTechScreenHandler {

	public final BlockPos pos;

	public SilverIodideCannonScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, Inventory playerInventory, BlockPos pos) {
		super(type, syncId, inventory);
		this.pos = pos;

		this.slotGrid()
		.pos( 8, 124).size(9, 1).inventory(playerInventory).add()
		.pos( 8,  66).size(9, 3)                           .add()
		.pos(80,  17).size(1, 1).inventory(      inventory).add()
		;
	}

	public SilverIodideCannonScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
		this(BigTechScreenHandlerTypes.SILVER_IODIDE_CANNON, syncId, new SimpleInventory(1), playerInventory, pos);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = this.slots.get(slotIndex);
		if (slot.hasStack()) {
			ItemStack stack = slot.getStack();
			if (slotIndex < 36) {
				if (stack.isOf(Items.FIREWORK_ROCKET)) {
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