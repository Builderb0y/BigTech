package builderb0y.bigtech.datagen.base;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface BlockItemDataGenerator extends BlockDataGenerator, ItemDataGenerator {

	public abstract BlockItem getBlockItem();

	@Override
	public default Block getBlock() {
		return this.getBlockItem().getBlock();
	}

	@Override
	public default Identifier getID() {
		return Registries.ITEM.getId(this.getBlockItem());
	}

	@Override
	public default Item getItem() {
		return this.getBlockItem();
	}
}