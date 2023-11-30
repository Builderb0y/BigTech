package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.FunctionalItems;

public class PhaseAlignerDataGenerator extends PhaseManipulatorDataGenerator {

	public PhaseAlignerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writePhaseAdjusterRecipe(context, Items.WHITE_STAINED_GLASS_PANE, FunctionalItems.CRYSTAL_CLUSTERS.white);
	}
}