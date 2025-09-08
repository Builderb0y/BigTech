package builderb0y.bigtech.blockEntities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class AbstractDeployerBlockEntity extends LootableBlockEntityThatActuallyHasAnInventory {

	public AbstractDeployerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState, 9);
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new Generic3x3ContainerScreenHandler(syncId, playerInventory, this);
	}

	public @Nullable StackWithSlot getRandomStackWithSlot() {
		ItemStack chosenStack = null;
		int chosenSlot = 0, chance = 0;
		for (int slot = 0; slot < 9; slot++) {
			ItemStack stack = this.getStack(slot);
			if (stack.getItem() instanceof BlockItem && (chance++ == 0 || this.world.random.nextInt(chance) == 0)) {
				chosenSlot = slot;
				chosenStack = stack;
			}
		}
		return chosenStack != null ? new StackWithSlot(chosenSlot, chosenStack) : null;
	}

	public boolean deploy(BlockPos placementPos, Direction facing) {
		StackWithSlot slotStack;
		if (!this.world.isClient && (slotStack = this.getRandomStackWithSlot()) != null) {
			((BlockItem)(slotStack.stack().getItem())).place(
				new AutomaticItemPlacementContext(
					this.world,
					placementPos,
					facing,
					slotStack.stack(),
					facing.getOpposite()
				)
			);
			return true;
		}
		return false;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return stack.getItem() instanceof BlockItem;
	}
}