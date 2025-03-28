package builderb0y.bigtech.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.gui.HeldItemInventory;
import builderb0y.bigtech.util.WorldHelper;
import builderb0y.bigtech.util.WorldHelper.SpawnHandler;

public class WorldInventory implements HeldItemInventory {

	public PlayerEntity player;
	public int heldSlot;
	public BlockPos origin;
	public Direction forward, right, down;
	public int depth;

	public WorldInventory(
		PlayerEntity player,
		int heldSlot,
		BlockPos origin,
		Direction forward,
		Direction right,
		Direction down
	) {
		this.player   = player;
		this.heldSlot = heldSlot;
		this.origin   = origin;
		this.forward  = forward;
		this.right    = right;
		this.down     = down;
	}

	@Override
	public int getHeldSlot() {
		return this.heldSlot;
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public boolean isEmpty() {
		for (int slot = 0; slot < 9; slot++) {
			if (!this.getStack(slot).isEmpty()) return false;
		}
		return true;
	}

	public BlockPos getPos(int slot) {
		int rightAmount = (slot % 3) - 1;
		int  downAmount = (slot / 3) - 1;
		return new BlockPos(
			this.origin.getX() + this.depth * this.forward.getOffsetX() + rightAmount * this.right.getOffsetX() + downAmount * this.down.getOffsetX(),
			this.origin.getY() + this.depth * this.forward.getOffsetY() + rightAmount * this.right.getOffsetY() + downAmount * this.down.getOffsetY(),
			this.origin.getZ() + this.depth * this.forward.getOffsetZ() + rightAmount * this.right.getOffsetZ() + downAmount * this.down.getOffsetZ()
		);
	}

	@Override
	public ItemStack getStack(int slot) {
		BlockPos pos = this.getPos(slot);
		BlockState state = this.player.getWorld().getBlockState(pos);
		return new ItemStack(state.getBlock().asItem());
	}

	public void doBreakBlock(BlockPos pos) {
		ItemStack stack = this.player.getInventory().getStack(this.heldSlot);
		if (stack.getItem() == FunctionalItems.DISLOCATOR) {
			WorldHelper.breakBlockWithTool(this.player.getWorld().as(), pos, this.player.getWorld().getBlockState(pos), this.player, stack);
			stack.damage(1, this.player, this.heldSlot == 40 ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND);
		}
	}

	public void transferToPlayer(int slot) {
		BlockPos pos = this.getPos(slot);
		if (!this.player.getWorld().canEntityModifyAt(this.player, pos)) {
			return;
		}
		BlockState state = this.player.getWorld().getBlockState(pos);
		if (state.isAir() || state.getHardness(this.player.getWorld(), pos) < 0.0F) {
			return;
		}
		WorldHelper.runEntitySpawnAction(
			() -> this.doBreakBlock(pos),
			(Entity entity) -> {
				if (entity instanceof ItemEntity item) {
					WorldHelper.runEntitySpawnAction(
						() -> this.player.getInventory().offerOrDrop(item.getStack()),
						null
					);
					return false;
				}
				else if (entity instanceof ExperienceOrbEntity) {
					entity.setPosition(this.player.getPos());
				}
				return true;
			}
		);
	}

	public void drop(int slot) {
		BlockPos pos = this.getPos(slot);
		if (!this.player.getWorld().canEntityModifyAt(this.player, pos)) {
			return;
		}
		BlockState state = this.player.getWorld().getBlockState(pos);
		if (state.isAir() || state.getHardness(this.player.getWorld(), pos) < 0.0F) {
			return;
		}
		WorldHelper.runEntitySpawnAction(
			() -> this.doBreakBlock(pos),
			(Entity entity) -> {
				if (entity instanceof ItemEntity item) {
					WorldHelper.runEntitySpawnAction(
						() -> this.player.dropItem(item.getStack(), true),
						null
					);
					return false;
				}
				else if (entity instanceof ExperienceOrbEntity) {
					entity.setPosition(this.player.getPos());
				}
				return true;
			}
		);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return this.removeStack(slot); //you get all or nothing.
	}

	@Override
	public ItemStack removeStack(int slot) {
		class Handler implements SpawnHandler {

			public ItemStack first = ItemStack.EMPTY;

			@Override
			public boolean handleSpawn(Entity entity) {
				if (entity instanceof ItemEntity item) {
					ItemStack oldStack = this.first;
					ItemStack newStack = item.getStack();
					if (oldStack.isEmpty()) {
						this.first = newStack;
						newStack = ItemStack.EMPTY;
					}
					else if (ItemStack.areItemsAndComponentsEqual(oldStack, newStack)) {
						int toTransfer = Math.min(newStack.getCount(), oldStack.getMaxCount() - oldStack.getCount());
						oldStack.increment(toTransfer);
						newStack.decrement(toTransfer);
					}
					if (!newStack.isEmpty()) {
						ItemStack newStack_ = newStack;
						WorldHelper.runEntitySpawnAction(
							() -> WorldInventory.this.player.getInventory().offerOrDrop(newStack_),
							null
						);
					}
					return false;
				}
				else if (entity instanceof ExperienceOrbEntity) {
					entity.setPosition(WorldInventory.this.player.getPos());
				}
				return true;
			}
		}
		BlockPos pos = this.getPos(slot);
		if (!this.player.getWorld().canEntityModifyAt(this.player, pos)) {
			return ItemStack.EMPTY;
		}
		BlockState state = this.player.getWorld().getBlockState(pos);
		if (state.isAir() || state.getHardness(this.player.getWorld(), pos) < 0.0F) {
			return ItemStack.EMPTY;
		}
		Handler handler = new Handler();
		WorldHelper.runEntitySpawnAction(() -> this.doBreakBlock(pos), handler);
		return handler.first;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		BlockPos pos = this.getPos(slot);
		if (!this.player.getWorld().canEntityModifyAt(this.player, pos)) {
			return;
		}
		this.player.getWorld().removeBlock(pos, false);
		if (!stack.isEmpty()) {
			((BlockItem)(stack.getItem())).place(
				new AutomaticItemPlacementContext(
					this.player.getWorld(),
					pos,
					this.forward,
					stack,
					this.forward.getOpposite()
				)
			);
		}
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return player.getInventory().getStack(this.heldSlot).getItem() == FunctionalItems.DISLOCATOR;
	}

	@Override
	public void clear() {}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public int getMaxCount(ItemStack stack) {
		return 1;
	}
}