package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.items.MaterialItems;

public class PhaseScramblerDataGenerator extends PhaseManipulatorDataGenerator {

	public PhaseScramblerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writePhaseAdjusterRecipe(context, Items.BLACK_STAINED_GLASS_PANE, MaterialItems.CRYSTAL_CLUSTERS.black);
	}
}