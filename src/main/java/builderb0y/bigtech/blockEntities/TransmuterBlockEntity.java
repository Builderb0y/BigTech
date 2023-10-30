package builderb0y.bigtech.blockEntities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.recipes.BigTechRecipeTypes;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.screenHandlers.TransmuterScreenHandler;

public class TransmuterBlockEntity extends LootableContainerBlockEntity implements SidedInventory {

	public static final int[] SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

	public DefaultedList<ItemStack> items;

	public TransmuterBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		this.items = DefaultedList.ofSize(15, ItemStack.EMPTY);
	}

	public TransmuterBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.TRANSMUTER, pos, state);
	}

	public static boolean isValidInput(RecipeManager recipeManager, ItemStack stack) {
		return recipeManager.listAllOfType(BigTechRecipeTypes.TRANSMUTE).stream().anyMatch(entry -> entry.value().input.test(stack));
	}

	public static boolean isValidOutput(RecipeManager recipeManager, ItemStack stack) {
		return recipeManager.listAllOfType(BigTechRecipeTypes.TRANSMUTE).stream().flatMap(entry -> entry.value().output.stream()).anyMatch(output -> ItemStack.canCombine(output.toStack(), stack));
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return this.getStack(slot).isEmpty && isValidInput(this.world.recipeManager, stack);
	}

	@Override
	public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
		return isValidOutput(this.world.recipeManager, stack);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return this.getStack(slot).isEmpty && isValidInput(this.world.recipeManager, stack);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return isValidOutput(this.world.recipeManager, stack);
	}

	@Override
	public DefaultedList<ItemStack> getInvStackList() {
		return this.items;
	}

	@Override
	public void setInvStackList(DefaultedList<ItemStack> list) {
		this.items = list;
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.transmuter");
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new TransmuterScreenHandler(BigTechScreenHandlerTypes.TRANSMUTER, syncId, this, playerInventory);
	}

	@Override
	public int size() {
		return 15;
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.items);
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.items.clear();
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.items);
		}
	}
}