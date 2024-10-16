package builderb0y.bigtech.gui.screenHandlers;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import builderb0y.bigtech.gui.HeldItemInventory;

public abstract class BigTechScreenHandler extends ScreenHandler implements IBigTechScreenHandler {

	public final Inventory inventory;
	public final PlayerInventory playerInventory;
	public final ShiftClickRules shiftClickRules;

	public BigTechScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, PlayerInventory playerInventory) {
		super(type, syncId);
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		this.shiftClickRules = new ShiftClickRules();
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public PlayerInventory getPlayerInventory() {
		return this.playerInventory;
	}

	@Override
	public List<Slot> getSlots() {
		return this.slots;
	}

	@Override
	public Slot addSlot(Slot slot) {
		return super.addSlot(slot);
	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (this.inventory instanceof HeldItemInventory held) {
			switch (actionType) {
				case PICKUP, QUICK_MOVE, THROW -> {
					if (this.isSlotProtected(held, slotIndex)) return;
				}
				case SWAP -> {
					if (this.isSlotProtected(held, slotIndex) || button == held.getHeldSlot()) return;
				}
				case CLONE, QUICK_CRAFT, PICKUP_ALL -> {}
			}
		}
		super.onSlotClick(slotIndex, button, actionType, player);
	}

	public boolean isSlotProtected(HeldItemInventory inventory, int slotIndex) {
		if (slotIndex >= 0 && slotIndex < this.slots.size()) {
			Slot slot = this.slots.get(slotIndex);
			return slot.inventory == this.playerInventory && slot.getIndex() == inventory.getHeldSlot();
		}
		return false;
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return IBigTechScreenHandler.super.quickMove(player, slot);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return IBigTechScreenHandler.super.canUse(player);
	}

	@Override
	public ShiftClickRules shiftClickRules() {
		return this.shiftClickRules;
	}

	public static BiPredicate<PlayerEntity, Slot> any() {
		return (PlayerEntity player, Slot slot) -> true;
	}

	public static BiPredicate<PlayerEntity, Slot> when(BooleanSupplier supplier) {
		return (PlayerEntity player, Slot slot) -> supplier.getAsBoolean();
	}

	public static BiPredicate<PlayerEntity, Slot> slot(Predicate<Slot> slotPredicate) {
		return (PlayerEntity player, Slot slot) -> slotPredicate.test(slot);
	}

	public static BiPredicate<PlayerEntity, Slot> stack(Predicate<ItemStack> stackPredicate) {
		return (PlayerEntity player, Slot slot) -> stackPredicate.test(slot.getStack());
	}

	public static BiPredicate<PlayerEntity, Slot> item(Predicate<Item> itemPredicate) {
		return (PlayerEntity player, Slot slot) -> itemPredicate.test(slot.getStack().getItem());
	}

	public static BiPredicate<PlayerEntity, Slot> itemIs(Item item) {
		return (PlayerEntity player, Slot slot) -> slot.getStack().isOf(item);
	}

	public static BiPredicate<PlayerEntity, Slot> itemIn(TagKey<Item> tag) {
		return (PlayerEntity player, Slot slot) -> slot.getStack().isIn(tag);
	}

	public static BiPredicate<PlayerEntity, Slot> player(Predicate<PlayerEntity> playerPredicate) {
		return (PlayerEntity player, Slot slot) -> playerPredicate.test(player);
	}

	public static BiPredicate<PlayerEntity, Slot> playerSlot(BiPredicate<PlayerEntity, Slot> playerSlotPredicate) {
		return playerSlotPredicate;
	}

	public static BiPredicate<PlayerEntity, Slot> playerStack(BiPredicate<PlayerEntity, ItemStack> playerStackPredicate) {
		return (PlayerEntity player, Slot slot) -> playerStackPredicate.test(player, slot.getStack());
	}

	public static BiPredicate<PlayerEntity, Slot> playerItem(BiPredicate<PlayerEntity, Item> playerItemPredicate) {
		return (PlayerEntity player, Slot slot) -> playerItemPredicate.test(player, slot.getStack().getItem());
	}
}