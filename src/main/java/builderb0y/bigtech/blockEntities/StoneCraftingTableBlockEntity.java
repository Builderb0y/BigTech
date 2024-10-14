package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.gui.screenHandlers.StoneCraftingTableScreenHandler;

public class StoneCraftingTableBlockEntity extends LootableBlockEntityThatActuallyHasAnInventory {

	public StoneCraftingTableBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState, 9);
	}

	public StoneCraftingTableBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.STONE_CRAFTING_TABLE, pos, state);
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.crafting");
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new StoneCraftingTableScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(this.world, this.pos));
	}
}