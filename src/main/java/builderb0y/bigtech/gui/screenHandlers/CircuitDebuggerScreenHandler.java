package builderb0y.bigtech.gui.screenHandlers;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.circuits.MicroProcessorCircuitComponent;
import builderb0y.bigtech.circuits.MicroProcessorCircuitComponent.CircuitStack;

public class CircuitDebuggerScreenHandler extends BigTechScreenHandler {

	public static final int
		PROPERTY_WIDTH  = 0,
		PROPERTY_HEIGHT = 1;

	public PropertyDelegate size = new ArrayPropertyDelegate(2);

	public CircuitDebuggerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, PlayerInventory playerInventory) {
		super(type, syncId, inventory, playerInventory);
		this.addProperties(this.size);
		SlotGrid grid = this.slotGrid();
		SlotRange
			hotbar  = grid.pos(  8, 202).size(9, 1).inventory(playerInventory).add(),
			storage = grid.pos(  8, 144).size(9, 3).add(),
			circuit = grid.pos( 36,  21).size(5, 5).inventory(inventory).slotFactory((Inventory inventory_, int index, int x, int y) -> new ImmutableSlot(inventory_, index, x, y, this.size)).space(22, 22).add();
		this.shiftClickRules().viseVersa(any(), hotbar.forward(), storage.forward());
	}

	public CircuitDebuggerScreenHandler(int syncID, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.CIRCUIT_DEBUGGER, syncID, new SimpleInventory(25), playerInventory);
	}

	public CircuitDebuggerScreenHandler(int syncID, Supplier<MicroProcessorCircuitComponent> circuit, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.CIRCUIT_DEBUGGER, syncID, new ProcessorViewInventory(circuit), playerInventory);
	}

	@Override
	public void sendContentUpdates() {
		if (this.inventory instanceof ProcessorViewInventory inventory) {
			MicroProcessorCircuitComponent processor = inventory.processor.get();
			this.size.set(PROPERTY_WIDTH, processor.width());
			this.size.set(PROPERTY_HEIGHT, processor.height());
		}
		super.sendContentUpdates();
	}

	public void enter(int slot) {
		if (this.inventory instanceof ProcessorViewInventory processorViewInventory) {
			MicroProcessorCircuitComponent processor = processorViewInventory.processor.get();
			int x = slot % 5 + 5 - processor.width();
			int y = slot / 5 + 5 - processor.height();
			if (processor.getComponent(x, y) instanceof MicroProcessorCircuitComponent) {
				processorViewInventory.processor = new RecursiveProcessorLocation(processorViewInventory.processor, x, y);
			}
		}
	}

	public void exit() {
		if (this.inventory instanceof ProcessorViewInventory processorViewInventory && this.playerInventory.player instanceof ServerPlayerEntity player) {
			if (processorViewInventory.processor instanceof RecursiveProcessorLocation recursive) {
				processorViewInventory.processor = recursive.container;
			}
			else {
				player.closeHandledScreen();
			}
		}
	}

	public static record RecursiveProcessorLocation(
		Supplier<MicroProcessorCircuitComponent> container,
		int x,
		int y
	)
	implements Supplier<MicroProcessorCircuitComponent> {

		@Override
		public MicroProcessorCircuitComponent get() {
			return (MicroProcessorCircuitComponent)(this.container.get().getComponent(this.x, this.y));
		}
	}

	public static class ImmutableSlot extends Slot {

		public static final Identifier X = BigTechMod.modID("assembler_x");

		public final PropertyDelegate size;

		public ImmutableSlot(Inventory inventory, int index, int x, int y, PropertyDelegate size) {
			super(inventory, index, x, y);
			this.size = size;
		}

		@Override
		public @Nullable Identifier getBackgroundSprite() {
			int x = this.getIndex() % 5;
			int y = this.getIndex() / 5;
			int startX = 5 - this.size.get(PROPERTY_WIDTH);
			int startY = 5 - this.size.get(PROPERTY_HEIGHT);
			return x >= startX && y >= startY ? null : X;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return false;
		}

		@Override
		public ItemStack takeStack(int amount) {
			return ItemStack.EMPTY;
		}

		@Override
		public boolean canTakeItems(PlayerEntity playerEntity) {
			return false;
		}
	}

	public static class ProcessorViewInventory implements Inventory {

		public Supplier<MicroProcessorCircuitComponent> processor;

		public ProcessorViewInventory(Supplier<MicroProcessorCircuitComponent> processor) {
			this.processor = processor;
		}

		@Override
		public int size() {
			return 25;
		}

		@Override
		public boolean isEmpty() {
			for (CircuitStack stack : this.processor.get().stacks) {
				if (stack.getStack().isEmpty()) return false;
			}
			return true;
		}

		@Override
		public ItemStack getStack(int slot) {
			MicroProcessorCircuitComponent processor = this.processor.get();
			int x = (slot % 5) - (5 - processor.width());
			int y = (slot / 5) - (5 - processor.height());
			return x >= 0 && y >= 0 ? processor.getStack(x, y) : ItemStack.EMPTY;
		}

		@Override
		public ItemStack removeStack(int slot, int amount) {
			throw new UnsupportedOperationException("Inventory not modifiable.");
		}

		@Override
		public ItemStack removeStack(int slot) {
			throw new UnsupportedOperationException("Inventory not modifiable.");
		}

		@Override
		public void setStack(int slot, ItemStack stack) {
			throw new UnsupportedOperationException("Inventory not modifiable.");
		}

		@Override
		public void markDirty() {

		}

		@Override
		public boolean canPlayerUse(PlayerEntity player) {
			return true;
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException("Inventory not modifiable.");
		}
	}
}