package builderb0y.bigtech.blockEntities;

import java.util.stream.IntStream;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.TransmuterScreenHandler;
import builderb0y.bigtech.mixins.ServerRecipeManager_PreparedRecipesAccess;
import builderb0y.bigtech.recipes.BigTechRecipeTypes;
import builderb0y.bigtech.recipes.TransmuteRecipe;
import builderb0y.bigtech.recipes.TransmuteRecipe.Output;

public class TransmuterBlockEntity extends LootableBlockEntityThatActuallyHasAnInventory implements SidedInventory {

	public static final int[] SLOTS = IntStream.range(0, 15).toArray();


	public TransmuterBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState, 15);
	}

	public TransmuterBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.TRANSMUTER, pos, state);
	}

	public static boolean isValidInput(ServerRecipeManager recipeManager, ItemStack stack) {
		return (
			recipeManager
			.<ServerRecipeManager_PreparedRecipesAccess>as()
			.bigtech_getPreparedRecipes()
			.getAll(BigTechRecipeTypes.TRANSMUTE)
			.stream()
			.anyMatch((RecipeEntry<TransmuteRecipe> entry) ->
				entry.value().input.test(stack)
			)
		);
	}

	public static boolean isValidOutput(ServerRecipeManager recipeManager, ItemStack stack) {
		return (
			recipeManager
			.<ServerRecipeManager_PreparedRecipesAccess>as()
			.bigtech_getPreparedRecipes()
			.getAll(BigTechRecipeTypes.TRANSMUTE)
			.stream()
			.flatMap((RecipeEntry<TransmuteRecipe> entry) ->
				entry.value().output.stream()
			)
			.anyMatch((Output output) ->
				ItemStack.areItemsAndComponentsEqual(output.toStack(), stack)
			)
		);
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return this.world instanceof ServerWorld serverWorld && this.getStack(slot).isEmpty() && isValidInput(serverWorld.getRecipeManager(), stack);
	}

	@Override
	public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
		return this.world instanceof ServerWorld serverWorld && isValidOutput(serverWorld.getRecipeManager(), stack);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return this.world instanceof ServerWorld serverWorld && this.getStack(slot).isEmpty() && isValidInput(serverWorld.getRecipeManager(), stack);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return this.world instanceof ServerWorld serverWorld && isValidOutput(serverWorld.getRecipeManager(), stack);
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.transmuter");
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new TransmuterScreenHandler(BigTechScreenHandlerTypes.TRANSMUTER, syncId, this, playerInventory);
	}
}