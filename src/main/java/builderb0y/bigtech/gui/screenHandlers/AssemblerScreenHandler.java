package builderb0y.bigtech.gui.screenHandlers;

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
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blockEntities.AssemblerBlockEntity;
import builderb0y.bigtech.circuits.CircuitComponent;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;

public class AssemblerScreenHandler extends BigTechScreenHandler {

	public AssemblerBlockEntity assembler;
	public PropertyDelegate assemblerProperties;
	public Text outputName;

	public AssemblerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory assembler, PlayerInventory playerInventory, PropertyDelegate syncedProperties, Text outputName) {
		super(type, syncId, assembler, playerInventory);
		this.outputName = outputName;
		this.addProperties(this.assemblerProperties = syncedProperties);
		SlotGrid grid = this.slotGrid();
		SlotRange
			hotbar  = grid.pos(  8, 202).size(9, 1).inventory(playerInventory).add(),
			storage = grid.pos(  8, 144).size(9, 3).add(),
			inputs  = grid.pos( 14,  21).size(5, 5).inventory(assembler).slotFactory((Inventory inventory_, int index, int x, int y) -> new MaybeXSlot(inventory_, index, x, y, syncedProperties)).space(22, 22).add(),
			output  = grid.pos(146, 109).size(1, 1).slotFactory(OutputSlot::new).add();
		this.shiftClickRules().collect(stack((ItemStack stack) -> stack.contains(BigTechDataComponents.CIRCUIT)), inputs.forward(), hotbar, storage);
		this.shiftClickRules().distribute(any(), inputs, hotbar.forward(), storage.forward());
		this.shiftClickRules().distribute(any(), output, hotbar.forward(), storage.forward());
	}

	public AssemblerScreenHandler(int syncId, AssemblerBlockEntity assembler, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.ASSEMBLER, syncId, assembler.inventory, playerInventory, assembler.syncedProperties, assembler.outputName);
		this.assembler = assembler;
	}

	public AssemblerScreenHandler(int syncId, PlayerInventory playerInventory, Text initialName) {
		this(BigTechScreenHandlerTypes.ASSEMBLER, syncId, new SimpleInventory(26), playerInventory, new ArrayPropertyDelegate(AssemblerBlockEntity.SYNCED_PROPERTY_COUNT), initialName);
	}

	public void cycle(int slotIndex, boolean forward) {
		if (slotIndex >= 0 && slotIndex < 25) {
			ItemStack stack = this.inventory.getStack(slotIndex);
			CircuitComponent circuit = stack.get(BigTechDataComponents.CIRCUIT);
			if (circuit != null) {
				stack.set(BigTechDataComponents.CIRCUIT, circuit.cycle(forward));
			}
		}
	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (slotIndex >= 36 && slotIndex < 36 + 25 && this.inventory instanceof AssemblerBlockEntity.MainInventory inventory) {
			inventory.beginExport(slotIndex - 36);
			try {
				super.onSlotClick(slotIndex, button, actionType, player);
			}
			finally {
				inventory.endExport(slotIndex - 36);
			}
		}
		else {
			super.onSlotClick(slotIndex, button, actionType, player);
		}
	}

	public static class MaybeXSlot extends Slot {

		public static final Identifier X = BigTechMod.modID("assembler_x");

		public final PropertyDelegate properties;

		public MaybeXSlot(Inventory inventory, int index, int x, int y, PropertyDelegate properties) {
			super(inventory, index, x, y);
			this.properties = properties;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			if (!stack.contains(BigTechDataComponents.CIRCUIT)) return false;
			int x = this.getIndex() % 5;
			int y = this.getIndex() / 5;
			int startX = 5 - this.properties.get(AssemblerBlockEntity.PROPERTY_WIDTH);
			int startY = 5 - this.properties.get(AssemblerBlockEntity.PROPERTY_HEIGHT);
			return x >= startX && y >= startY;
		}

		@Override
		public @Nullable Identifier getBackgroundSprite() {
			int x = this.getIndex() % 5;
			int y = this.getIndex() / 5;
			int startX = 5 - this.properties.get(AssemblerBlockEntity.PROPERTY_WIDTH);
			int startY = 5 - this.properties.get(AssemblerBlockEntity.PROPERTY_HEIGHT);
			return x >= startX && y >= startY ? null : X;
		}
	}

	public static class OutputSlot extends Slot {

		public OutputSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return false;
		}
	}
}