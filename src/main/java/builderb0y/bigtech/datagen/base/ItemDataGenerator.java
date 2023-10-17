package builderb0y.bigtech.datagen.base;

import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public interface ItemDataGenerator extends DataGenerator {

	public abstract Item getItem();

	@Override
	public default Identifier getID() {
		return Registries.ITEM.getId(this.getItem());
	}

	public abstract void writeItemModels(DataGenContext context);

	public abstract void writeRecipes(DataGenContext context);

	public abstract Collection<TagKey<Item>> getItemTags(DataGenContext context);
}