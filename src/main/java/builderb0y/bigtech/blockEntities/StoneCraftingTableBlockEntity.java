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

import builderb0y.bigtech.screenHandlers.StoneCraftingTableScreenHandler;

public class StoneCraftingTableBlockEntity extends LootableContainerBlockEntity {

	public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

	public StoneCraftingTableBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public StoneCraftingTableBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.STONE_CRAFTING_TABLE, pos, state);
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.inventory.clear();
		if (!this.readLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory, registryLookup);
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		if (!this.writeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory, registryLookup);
		}
	}

	@Override
	public DefaultedList<ItemStack> getHeldStacks() {
		return this.inventory;
	}

	@Override
	public void setHeldStacks(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.crafting");
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new StoneCraftingTableScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(this.world, this.pos));
	}

	@Override
	public int size() {
		return 9;
	}
}