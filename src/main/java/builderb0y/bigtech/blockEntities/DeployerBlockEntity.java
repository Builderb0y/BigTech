package builderb0y.bigtech.blockEntities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import builderb0y.bigtech.util.Inventories2.SlotStack;

public class DeployerBlockEntity extends LootableBlockEntityThatActuallyHasAnInventory {

	public DeployerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState, 9);
	}

	public DeployerBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.SHORT_RANGE_DEPLOYER, pos, state);
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.short_range_deployer");
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new Generic3x3ContainerScreenHandler(syncId, playerInventory, this);
	}

	public @Nullable SlotStack getRandomSlotStack(Random random) {
		ItemStack chosenStack = null;
		int chosenSlot = 0, chance = 0;
		for (int slot = 0; slot < 9; slot++) {
			ItemStack stack = this.getStack(slot);
			if (!stack.isEmpty() && (chance++ == 0 || random.nextInt(chance) == 0)) {
				chosenSlot = slot;
				chosenStack = stack;
			}
		}
		return chosenStack != null ? new SlotStack(chosenSlot, chosenStack) : null;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return stack.getItem() instanceof BlockItem;
	}
}