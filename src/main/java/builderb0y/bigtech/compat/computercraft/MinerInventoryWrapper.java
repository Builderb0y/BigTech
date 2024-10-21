package builderb0y.bigtech.compat.computercraft;

import java.util.Map;
import java.util.Optional;

import dan200.computercraft.api.detail.VanillaDetailRegistries;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;

import builderb0y.bigtech.entities.MinerEntity;

public final class MinerInventoryWrapper {

	public final MinerWrapper wrapper;

	public MinerInventoryWrapper(MinerWrapper wrapper) {
		this.wrapper = wrapper;
	}

	@LuaFunction(mainThread = true)
	public int size() throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		return MinerEntity.STORAGE_SIZE;
	}

	public @Nullable Map<String, ?> implGetStack(int slot, boolean advanced) {
		ItemStack stack = this.wrapper.miner.getStack(slot);
		if (stack.isEmpty()) return null;
		if (advanced) {
			return VanillaDetailRegistries.ITEM_STACK.getDetails(stack);
		}
		else {
			return VanillaDetailRegistries.ITEM_STACK.getBasicDetails(stack);
		}
	}

	@LuaFunction(mainThread = true)
	public @Nullable Map<String, ?> getItemDetail(int slot, Optional<Boolean> advanced) throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		if (slot <= 0 || slot > MinerEntity.STORAGE_SIZE) return null;
		return this.implGetStack(slot - 1, advanced.isPresent() && advanced.get());
	}

	@LuaFunction(mainThread = true)
	public LuaArrayTable<Map<String, ?>> list(Optional<Boolean> advanced) throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		boolean isAdvanced = advanced.isPresent() && advanced.get();
		@SuppressWarnings("unchecked")
		Map<String, ?>[] maps = new Map[MinerEntity.STORAGE_SIZE];
		for (int slot = 0; slot < MinerEntity.STORAGE_SIZE; slot++) {
			maps[slot] = this.implGetStack(slot, isAdvanced);
		}
		return new LuaArrayTable<>(maps);
	}

	@LuaFunction(mainThread = true)
	public int getItemLimit(int slot) throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		return 64;
	}

	public int implTransfer(int fromSlot, int toSlot, Optional<Integer> amount) {
		if (amount.isPresent() && amount.get() <= 0) return 0;
		ItemStack fromStack = this.wrapper.miner.getStack(fromSlot);
		if (fromStack.isEmpty()) return 0;
		if (fromSlot == toSlot) return fromStack.getCount();
		ItemStack toStack = this.wrapper.miner.getStack(toSlot);
		if (toStack.isEmpty()) {
			if (amount.isPresent()) {
				int toTransfer = Math.min(fromStack.getCount(), amount.get());
				this.wrapper.miner.setStack(toSlot, fromStack.split(toTransfer));
				return toTransfer;
			}
			else {
				this.wrapper.miner.setStack(fromSlot, ItemStack.EMPTY);
				this.wrapper.miner.setStack(toSlot, fromStack);
				return fromStack.getCount();
			}
		}
		else if (ItemStack.areItemsAndComponentsEqual(fromStack, toStack)) {
			int toTransfer = Math.min(fromStack.getCount(), toStack.getMaxCount() - toStack.getCount());
			if (toTransfer <= 0) return 0;
			if (amount.isPresent()) toTransfer = Math.min(toTransfer, amount.get());
			assert toTransfer > 0;
			fromStack.decrement(toTransfer);
			toStack.increment(toTransfer);
			return toTransfer;
		}
		else {
			return 0;
		}
	}

	@LuaFunction(mainThread = true)
	public int transfer(int fromSlot, int toSlot, Optional<Integer> amount) throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		if (fromSlot <= 0 || fromSlot > MinerEntity.STORAGE_SIZE) {
			throw new LuaException("fromSlot out of range: must be between 1 and " + MinerEntity.STORAGE_SIZE + ", but it was " + fromSlot);
		}
		if (toSlot <= 0 || toSlot > MinerEntity.STORAGE_SIZE) {
			throw new LuaException("toSlot out of range: must be between 1 and " + MinerEntity.STORAGE_SIZE + ", but it was " + toSlot);
		}
		return this.implTransfer(fromSlot - 1, toSlot - 1, amount);
	}

	@LuaFunction(mainThread = true)
	public int transferToFuel(int fromSlot, Optional<Integer> amount) throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		if (fromSlot <= 0 || fromSlot > MinerEntity.STORAGE_SIZE) {
			throw new LuaException("fromSlot out of range: must be between 1 and " + MinerEntity.STORAGE_SIZE + ", but it was " + fromSlot);
		}
		return this.implTransfer(fromSlot - 1, MinerEntity.FUEL_START, amount);
	}

	@LuaFunction(mainThread = true)
	public int transferFromFuel(int toSlot, Optional<Integer> amount) throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		if (toSlot <= 0 || toSlot > MinerEntity.STORAGE_SIZE) {
			throw new LuaException("toSlot out of range: must be between 1 and " + MinerEntity.STORAGE_SIZE + ", but it was " + toSlot);
		}
		return this.implTransfer(MinerEntity.FUEL_START, toSlot - 1, amount);
	}

	@LuaFunction(mainThread = true)
	public int transferToSmelting(int fromSlot, Optional<Integer> amount) throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		if (fromSlot <= 0 || fromSlot > MinerEntity.STORAGE_SIZE) {
			throw new LuaException("fromSlot out of range: must be between 1 and " + MinerEntity.STORAGE_SIZE + ", but it was " + fromSlot);
		}
		return this.implTransfer(fromSlot - 1, MinerEntity.SMELTING_START, amount);
	}

	@LuaFunction(mainThread = true)
	public int transferFromSmelting(int toSlot, Optional<Integer> amount) throws LuaException {
		this.wrapper.checkRangeExceptRunning();
		if (toSlot <= 0 || toSlot > MinerEntity.STORAGE_SIZE) {
			throw new LuaException("toSlot out of range: must be between 1 and " + MinerEntity.STORAGE_SIZE + ", but it was " + toSlot);
		}
		return this.implTransfer(MinerEntity.SMELTING_START, toSlot - 1, amount);
	}
}