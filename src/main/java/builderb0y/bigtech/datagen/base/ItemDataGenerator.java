package builderb0y.bigtech.datagen.base;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface ItemDataGenerator extends LocalizedDataGenerator {

	public abstract Item getItem();

	@Override
	public default Identifier getId() {
		return Registries.ITEM.getId(this.getItem());
	}

	@Override
	public default void run(DataGenContext context) {
		LocalizedDataGenerator.super.run(context);

		this.writeItemModels(context);
		this.writeRecipes(context);
		this.setupOtherItemTags(context);
	}

	public abstract void writeItemModels(DataGenContext context);

	public abstract void writeRecipes(DataGenContext context);

	public abstract void setupOtherItemTags(DataGenContext context);
}