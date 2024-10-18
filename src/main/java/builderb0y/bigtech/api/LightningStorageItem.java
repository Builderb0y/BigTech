package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.dataComponents.BigTechDataComponents;

/**
an {@link Item} which can store lightning energy.
unlike most of my API interfaces, this one
must be implemented directly on the Item.
it does not use an {@link ItemApiLookup}.
the reason for this is that the item needs to call
{@link #defaultUseOnBlock(ItemUsageContext)}
from {@link Item#useOnBlock(ItemUsageContext)},
and that requires a custom Item class anyway.
*/
public interface LightningStorageItem {

	public default int getCharge(ItemStack stack) {
		return stack.getOrDefault(BigTechDataComponents.LIGHTNING_ENERGY, 0);
	}

	public default int getMaxCharge(ItemStack stack) {
		return stack.getOrDefault(BigTechDataComponents.LIGHTNING_CAPACITY, 1);
	}

	/**
	sets the charge on the given stack.
	this method *mutates* the stack,
	usually by modifying its NBT data.
	*/
	public default void setCharge(ItemStack stack, int charge) {
		stack.set(BigTechDataComponents.LIGHTNING_ENERGY, charge);
	}

	/**
	this method should be called from {@link Item#useOnBlock(ItemUsageContext)}.
	by default, it allows the block to decide what to
	do when right clicked by a LightningStorageItem.
	the default implementation for blocks is to summon a LightningPulse.
	see {@link LightningPulseInteractor#interactWithBattery(World, BlockPos, BlockState, PlayerEntity, ItemStack, LightningStorageItem)}.
	blocks can override this behavior to do other things,
	such as transferring energy between the block and the item.
	*/
	public default ActionResult defaultUseOnBlock(ItemUsageContext context) {
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		LightningPulseInteractor interactor = LightningPulseInteractor.LOOKUP.find(context.getWorld(), context.getBlockPos(), state, null, null);
		if (interactor != null) {
			return interactor.interactWithBattery(context.getWorld(), context.getBlockPos(), state, context.getPlayer(), context.getStack(), this);
		}
		return ActionResult.PASS;
	}

	/**
	by default, right clicking on a LightningPulseInteractor
	block will summon a lightning pulse.
	some blocks may override this behavior,
	but for those that don't, this method can be used
	to fine-tune the number of steps in the pulse.
	tweak this based on how powerful your lightning storage item is.
	*/
	public abstract int getDefaultSpreadEvents(ItemStack stack);
}