package builderb0y.bigtech.items;

import java.util.stream.Stream;

import net.minecraft.item.ItemStack;

public interface InventoryVariants {

	public abstract Stream<ItemStack> getInventoryStacks();
}