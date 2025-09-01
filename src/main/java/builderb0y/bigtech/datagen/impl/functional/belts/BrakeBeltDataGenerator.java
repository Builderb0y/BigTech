package builderb0y.bigtech.datagen.impl.functional.belts;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;

public class BrakeBeltDataGenerator extends DirectionalBeltDataGenerator {

	public BrakeBeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeBeltRecipes(context, Items.SLIME_BALL);
	}
}