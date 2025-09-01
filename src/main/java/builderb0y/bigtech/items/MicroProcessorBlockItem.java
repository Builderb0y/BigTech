package builderb0y.bigtech.items;

import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;

import builderb0y.bigtech.circuits.MicroProcessorCircuitComponent;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.gui.screenHandlers.CircuitDebuggerScreenHandler;

public class MicroProcessorBlockItem extends BlockItem implements InventoryVariants {

	public MicroProcessorBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType == ClickType.RIGHT && otherStack.isOf(FunctionalItems.MAGNIFYING_GLASS) && stack.get(BigTechDataComponents.CIRCUIT) instanceof MicroProcessorCircuitComponent circuit) {
			if (!player.getWorld().isClient) {
				if (player.currentScreenHandler instanceof CircuitDebuggerScreenHandler handler) {
					handler.enter(slot.getIndex());
				}
				else {
					player.openHandledScreen(
						new NamedScreenHandlerFactory() {

							@Override
							public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
								return new CircuitDebuggerScreenHandler(syncId, () -> (MicroProcessorCircuitComponent)(stack.get(BigTechDataComponents.CIRCUIT)), playerInventory);
							}

							@Override
							public Text getDisplayName() {
								return stack.getName();
							}
						}
					);
				}
			}
			return true;
		}
		return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
	}

	@Override
	public Stream<ItemStack> getInventoryStacks() {
		//there is no sane default for what a creative-spawned microprocessor should do when placed.
		return Stream.empty();
	}
}