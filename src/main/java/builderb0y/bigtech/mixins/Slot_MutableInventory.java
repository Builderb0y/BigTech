package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

@Mixin(Slot.class)
public interface Slot_MutableInventory {

	@Mutable
	@Accessor("inventory")
	public void bigtech_setInventory(Inventory inventory);
}