package builderb0y.bigtech.gui.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class SilverIodideCannonScreenHandler extends BigTechScreenHandler {

	public final BlockPos pos;
	public final Property selectedButton;

	public SilverIodideCannonScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, PlayerInventory playerInventory, BlockPos pos, Property selectedButton) {
		super(type, syncId, inventory, playerInventory);
		this.pos = pos;
		this.selectedButton = this.addProperty(selectedButton);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar  = grid.pos( 8, 124).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos( 8,  66).size(9, 3)                           .add(),
			fireworkSlot  = grid.pos(80,  17).size(1, 1).inventory(      inventory).add();

		this.shiftClickRules()
		.collect(itemIs(Items.FIREWORK_ROCKET), fireworkSlot.forward(), playerHotbar, playerStorage)
		.distribute(any(), fireworkSlot, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward())
		;
	}

	public SilverIodideCannonScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
		this(BigTechScreenHandlerTypes.SILVER_IODIDE_CANNON, syncId, new SimpleInventory(1), playerInventory, pos, Property.create());
	}
}