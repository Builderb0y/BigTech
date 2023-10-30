package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;

public class CrystalDebrisDataGenerator extends BasicItemDataGenerator {

	public CrystalDebrisDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		//no-op.
	}
}