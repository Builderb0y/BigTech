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
	public default Identifier getId() {
		return Registries.ITEM.getId(this.getBlockItem());
	}

	@Override
	public default Item getItem() {
		return this.getBlockItem();
	}

	@Override
	public default void run(DataGenContext context) {
		this.setupLang(context);

		this.writeBlockstateJson(context);
		this.writeBlockModels(context);
		this.writeLootTableJson(context);
		this.setupMiningToolTags(context);
		this.setupMiningLevelTags(context);
		this.setupOtherBlockTags(context);

		this.writeItemModels(context);
		this.writeRecipes(context);
		this.setupOtherItemTags(context);
	}
}