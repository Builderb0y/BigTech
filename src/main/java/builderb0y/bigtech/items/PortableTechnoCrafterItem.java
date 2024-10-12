package builderb0y.bigtech.items;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import builderb0y.bigtech.gui.TechnoCrafterAccess;
import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.TechnoCrafterScreenHandler;

public class PortableTechnoCrafterItem extends Item {

	public PortableTechnoCrafterItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack heldStack = player.getStackInHand(hand);
		if (!world.isClient) {
			int heldSlot = switch (hand) {
				case MAIN_HAND -> player.getInventory().selectedSlot;
				case OFF_HAND -> -1;
			};
			player.openHandledScreen(new ExtendedScreenHandlerFactory<Byte>() {

				@Override
				public Text getDisplayName() {
					return heldStack.getName();
				}

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new TechnoCrafterScreenHandler(BigTechScreenHandlerTypes.TECHNO_CRAFTER, syncId, new PortableTechnoCrafterAccess(heldStack, heldSlot), playerInventory);
				}

				@Override
				public Byte getScreenOpeningData(ServerPlayerEntity player) {
					return (byte)(heldSlot);
				}
			});
		}
		return TypedActionResult.success(heldStack, false);
	}

	public static class PortableTechnoCrafterAccess implements TechnoCrafterAccess {

		public final ItemStack stack;
		public final int heldSlot;
		public final SplitStackList inventory;
		public boolean interactionSide;

		public PortableTechnoCrafterAccess(ItemStack stack, int slot) {
			this.stack = stack;
			this.heldSlot = slot;
			ContainerComponent component = stack.get(DataComponentTypes.CONTAINER);
			ArrayView array = new ArrayView(18);
			DefaultedList<ItemStack> stacks = new DefaultedList<>(array, ItemStack.EMPTY);
			if (component != null) component.copyTo(stacks);
			this.inventory = SplitStackList.create(stacks);
		}

		@Override
		public boolean getInteractionSide() {
			return this.interactionSide;
		}

		@Override
		public void setInteractionSide(boolean interactionSide) {
			this.interactionSide = interactionSide;
		}

		@Override
		public DefaultedList<ItemStack> getHeldStacks() {
			return this.inventory.heldStacks();
		}

		@Override
		public DefaultedList<ItemStack> getStacks(boolean interactionSide) {
			return this.inventory.getStacks(interactionSide);
		}

		@Override
		public void markDirty() {
			this.stack.set(
				DataComponentTypes.CONTAINER,
				ContainerComponent.fromStacks(this.inventory.heldStacks())
			);
		}

		@Override
		public boolean canPlayerUse(PlayerEntity player) {
			if (!this.stack.isOf(FunctionalItems.PORTABLE_TECHNO_CRAFTER)) {
				return false;
			}
			if (this.heldSlot >= 0) {
				return player.getInventory().selectedSlot == this.heldSlot && player.getInventory().getMainHandStack() == this.stack;
			}
			else {
				return player.getInventory().offHand.get(0) == this.stack;
			}
		}

		@Override
		public boolean isSlotBlocked(int index) {
			return index == this.heldSlot;
		}
	}
}