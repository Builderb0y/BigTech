package builderb0y.bigtech.datagen.impl.belts;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;

public class InjectorBeltDataGenerator extends DirectionalBeltDataGenerator {

	public InjectorBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeBeltRecipes(context, Items.HOPPER);
	}
}