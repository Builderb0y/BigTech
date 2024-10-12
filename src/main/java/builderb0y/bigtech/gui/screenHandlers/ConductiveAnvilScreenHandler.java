package builderb0y.bigtech.gui.screenHandlers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerType;

import builderb0y.bigtech.blockEntities.ConductiveAnvilBlockEntity;

public class ConductiveAnvilScreenHandler extends BigTechScreenHandler {

	public ConductiveAnvilScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory, PlayerInventory playerInventory) {
		super(type, syncId, inventory, playerInventory);

		SlotGrid grid = this.slotGrid();
		SlotRange
			playerHotbar = grid.pos(8, 106).size(9, 1).inventory(playerInventory).add(),
			playerStorage = grid.pos(8, 48).size(9, 3).add(),
			fromSlot = grid.pos(44, 17).size(1, 1).inventory(inventory).add(),
			toSlot = grid.pos(116, 17).add();

		this.shiftClickRules()
		.collect(stack(EnchantmentHelper::hasEnchantments), fromSlot.forward(), playerHotbar, playerStorage)
		.collect(stack(ConductiveAnvilBlockEntity::isEnchantable), toSlot.forward(), playerHotbar, playerStorage)
		.distribute(any(), fromSlot, playerHotbar.forward(), playerStorage.forward())
		.distribute(any(), toSlot, playerHotbar.forward(), playerStorage.forward())
		.viseVersa(any(), playerHotbar.forward(), playerStorage.forward())
		;
	}

	public ConductiveAnvilScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(BigTechScreenHandlerTypes.CONDUCTIVE_ANVIL, syncId, new SimpleInventory(2), playerInventory);
	}
}