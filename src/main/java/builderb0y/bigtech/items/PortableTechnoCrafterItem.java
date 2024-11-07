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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import builderb0y.bigtech.gui.HeldItemInventory;
import builderb0y.bigtech.gui.TechnoCrafterAccess;
import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.PortableTechnoCrafterScreenHandler;

public class PortableTechnoCrafterItem extends Item {

	public PortableTechnoCrafterItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return !oldStack.isOf(this) || !newStack.isOf(this);
	}

	@Override
	public ActionResult use(World world, PlayerEntity player, Hand hand) {
		ItemStack heldStack = player.getStackInHand(hand);
		if (!world.isClient) {
			int heldSlot = switch (hand) {
				case MAIN_HAND -> player.getInventory().selectedSlot;
				case OFF_HAND -> 40;
			};
			player.openHandledScreen(new ExtendedScreenHandlerFactory<Byte>() {

				@Override
				public Text getDisplayName() {
					Text customName = heldStack.get(DataComponentTypes.CUSTOM_NAME);
					return customName != null ? customName : Text.translatable("container.bigtech.portable_techno_crafter");
				}

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new PortableTechnoCrafterScreenHandler(BigTechScreenHandlerTypes.PORTABLE_TECHNO_CRAFTER, syncId, new PortableTechnoCrafterAccess(heldStack, heldSlot), playerInventory);
				}

				@Override
				public Byte getScreenOpeningData(ServerPlayerEntity player) {
					return (byte)(heldSlot);
				}
			});
		}
		return ActionResult.CONSUME;
	}

	public static class PortableTechnoCrafterAccess implements TechnoCrafterAccess, HeldItemInventory {

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
			this.inventory = SplitStackList.createPortable(stacks);
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
			return player.getInventory().getStack(this.heldSlot).getItem() == FunctionalItems.PORTABLE_TECHNO_CRAFTER;
		}

		@Override
		public int getHeldSlot() {
			return this.heldSlot;
		}
	}
}