package builderb0y.bigtech.gui.screenHandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.StoneCraftingTableBlockEntity;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.mixins.AbstractCraftingScreenHandler_Setters;
import builderb0y.bigtech.mixins.CraftingResultSlot_InputSetter;
import builderb0y.bigtech.mixins.CraftingScreenHandler_Accessors;
import builderb0y.bigtech.mixins.Slot_MutableInventory;
import builderb0y.bigtech.util.WorldHelper;

public class StoneCraftingTableScreenHandler extends CraftingScreenHandler {

	public StoneCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(syncId, playerInventory, context);
		RecipeInputInventory oldInput = this.craftingInventory;
		CraftingInventory newInput = new CraftingInventory(this, 3, 3, context.get((World world, BlockPos pos) -> WorldHelper.getBlockEntity(world, pos, StoneCraftingTableBlockEntity.class)).orElseThrow().heldStacks);
		this.abstractAccess().bigtech_setInput(newInput);
		for (Slot slot : this.slots) {
			if (slot.inventory == oldInput) {
				slot.<Slot_MutableInventory>as().bigtech_setInventory(newInput);
			}
			if (slot instanceof CraftingResultSlot_InputSetter setter) {
				setter.bigtech_setInput(newInput);
			}
		}
	}

	public StoneCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(syncId, playerInventory);
	}

	public final AbstractCraftingScreenHandler_Setters abstractAccess() {
		return this.as();
	}

	public final CraftingScreenHandler_Accessors craftingAccess() {
		return this.as();
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
		if (inventory == this.craftingInventory) {
			this.craftingAccess().bigtech_getContext().run(World::markDirty);
		}
	}

	@Override
	public void dropInventory(PlayerEntity player, Inventory inventory) {
		//no-op'ing this method is not the best way to prevent dropping items
		//when the GUI is closed, I would've preferred to override onClosed()
		//instead, but java doesn't allow me to call super.super.onClosed().
		//so, I have this override instead.
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return canUse(this.craftingAccess().bigtech_getContext(), player, FunctionalBlocks.STONE_CRAFTING_TABLE);
	}
}