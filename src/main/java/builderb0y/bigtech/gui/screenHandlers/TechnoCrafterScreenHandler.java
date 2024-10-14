package builderb0y.bigtech.gui.screenHandlers;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import builderb0y.bigtech.gui.TechnoCrafterAccess;

public class TechnoCrafterScreenHandler extends BigTechScreenHandler {

	public final CraftingInventory leftGrid, rightGrid;
	public final CraftingResultInventory leftTopResult, rightBottomResult;

	public TechnoCrafterScreenHandler(ScreenHandlerType<?> screenHandlerType, int syncID, TechnoCrafterAccess crafter, PlayerInventory playerInventory) {
		super(screenHandlerType, syncID, crafter, playerInventory);
		class CraftingInventoryThatMarksOurCrafterDirtyProperly extends CraftingInventory {

			public CraftingInventoryThatMarksOurCrafterDirtyProperly(ScreenHandler handler, int width, int height, DefaultedList<ItemStack> stacks) {
				super(handler, width, height, stacks);
			}

			@Override
			public void markDirty() {
				super.markDirty();
				TechnoCrafterScreenHandler.this.access().markDirty();
			}
		}
		this.leftGrid          = new CraftingInventoryThatMarksOurCrafterDirtyProperly(this, 3, 3, crafter.getStacks(false));
		this.rightGrid         = new CraftingInventoryThatMarksOurCrafterDirtyProperly(this, 3, 3, crafter.getStacks(true));
		this.leftTopResult     = new CraftingResultInventory();
		this.rightBottomResult = new CraftingResultInventory();

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar      = grid.pos(  8, 142).size(9, 1).inventory(playerInventory).add(),
			playerStorage     = grid.pos(  8,  84).size(9, 3)                           .add(),
			leftGrid          = grid.pos(  8,  17).size(3, 3).inventory(this.leftGrid)  .add(),
			rightGrid         = grid.pos(116,  17)           .inventory(this.rightGrid) .add(),
			leftTopOutput     = grid.pos( 80,  17).size(1, 1).inventory(this.leftTopResult    ).slotFactory(this.resultSlotFactory(false)).add(),
			rightBottomOutput = grid.pos( 80,  53)           .inventory(this.rightBottomResult).slotFactory(this.resultSlotFactory(true )).add(),
			trash             = grid.pos(176, 142)           .inventory(TrashInventory.INSTANCE).slotFactory(Slot::new).add();
		this
		.shiftClickRules
		.collect(when(() -> !crafter.getInteractionSide()),  leftGrid.forward(), playerHotbar, playerStorage)
		.collect(when(() ->  crafter.getInteractionSide()), rightGrid.forward(), playerHotbar, playerStorage)
		.builder().from(leftGrid, rightGrid, leftTopOutput, rightBottomOutput).to(playerHotbar.forward(), playerStorage.forward()).add()
		;
	}

	public TechnoCrafterScreenHandler(int syncID, PlayerInventory playerInventory, byte selectedSlot) {
		this(BigTechScreenHandlerTypes.TECHNO_CRAFTER, syncID, TechnoCrafterAccess.create(selectedSlot), playerInventory);
	}

	public SlotFactory resultSlotFactory(boolean right) {
		return (Inventory inventory, int index, int x, int y) -> {
			return new CraftingResultSlot(
				this.playerInventory.player,
				right ? this.rightGrid : this.leftGrid,
				right ? this.rightBottomResult : this.leftTopResult,
				index,
				x,
				y
			);
		};
	}

	public TechnoCrafterAccess access() {
		return (TechnoCrafterAccess)(this.inventory);
	}

	public CraftingInventory getGrid(boolean side) {
		return side ? this.rightGrid : this.leftGrid;
	}

	public CraftingResultInventory getResult(boolean side) {
		return side ? this.rightBottomResult : this.leftTopResult;
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		if (slotIndex == 36 + 18 || slotIndex == 36 + 19) {
			Slot slot = this.getSlot(slotIndex);
			boolean loop = true;
			while (loop) {
				super.quickMove(player, slotIndex);
				if (!slot.getStack().isEmpty()) {
					player.dropItem(slot.getStack().copy(), true);
					loop = false;
				}
				this.updateResult(slotIndex == 36 + 19);
				if (slot.getStack().isEmpty()) {
					loop = false;
				}
			}
			return ItemStack.EMPTY;
		}
		else {
			return super.quickMove(player, slotIndex);
		}
	}

	@Override
	public void syncState() {
		this.updateResult(false);
		this.updateResult(true);
		super.syncState();
	}

	public void updateResult(boolean side) {
		updateResult(
			this.playerInventory.player.getWorld(),
			this.playerInventory.player,
			this.getGrid(side),
			this.getResult(side),
			this.getResult(side).getLastRecipe().as()
		);
	}

	//mostly a copy-paste of CraftingScreenHandler.updateResult(),
	//but with some minor changes which make it
	//actually work properly for techno crafters.
	public static void updateResult(
		World world,
		PlayerEntity player,
		RecipeInputInventory craftingInventory,
		CraftingResultInventory resultInventory,
		@Nullable RecipeEntry<CraftingRecipe> recipe
	) {
		if (!world.isClient) {
			CraftingRecipeInput craftingRecipeInput = craftingInventory.createRecipeInput();
			ServerPlayerEntity serverPlayerEntity = player.as();
			ItemStack itemStack = ItemStack.EMPTY;
			Optional<RecipeEntry<CraftingRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingRecipeInput, world, recipe);
			if (optional.isPresent()) {
				RecipeEntry<CraftingRecipe> recipeEntry = optional.get();
				CraftingRecipe craftingRecipe = recipeEntry.value();
				if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, recipeEntry)) {
					ItemStack itemStack2 = craftingRecipe.craft(craftingRecipeInput, world.getRegistryManager());
					if (itemStack2.isItemEnabled(world.getEnabledFeatures())) {
						itemStack = itemStack2;
					}
				}
			}

			resultInventory.setStack(0, itemStack);
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.access().canPlayerUse(player);
	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (
			this.access().isSlotBlocked(slotIndex) || (
				actionType == SlotActionType.SWAP &&
				this.access().isSlotBlocked(button)
			)
		) {
			return;
		}

		if ((slotIndex >= 36 && slotIndex < 36 + 9) || slotIndex == 36 + 18) {
			this.access().setInteractionSide(false);
		}
		else if ((slotIndex >= 36 + 9 && slotIndex < 36 + 18) || slotIndex == 36 + 19) {
			this.access().setInteractionSide(true);
		}

		super.onSlotClick(slotIndex, button, actionType, player);

		//the CORRECT way to do this would be to listen to changes
		//to our left and right grids, via onContentsChanged(),
		//but there are apparently some action types that don't call that.
		this.updateResult(false);
		this.updateResult(true);
	}
}