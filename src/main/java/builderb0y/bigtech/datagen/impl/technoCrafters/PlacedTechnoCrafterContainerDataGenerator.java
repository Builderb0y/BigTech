package builderb0y.bigtech.datagen.impl.technoCrafters;

import net.minecraft.screen.ScreenHandlerType;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.impl.InventoryDataGenerator;

public class PlacedTechnoCrafterContainerDataGenerator extends InventoryDataGenerator {

	public PlacedTechnoCrafterContainerDataGenerator(ScreenHandlerType<?> screenHandlerType) {
		super(screenHandlerType);
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return "Techno Crafter";
	}
}