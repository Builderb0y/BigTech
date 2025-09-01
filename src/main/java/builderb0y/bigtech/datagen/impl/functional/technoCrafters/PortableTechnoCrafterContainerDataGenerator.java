package builderb0y.bigtech.datagen.impl.functional.technoCrafters;

import net.minecraft.screen.ScreenHandlerType;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.impl.InventoryDataGenerator;

public class PortableTechnoCrafterContainerDataGenerator extends InventoryDataGenerator {

	public PortableTechnoCrafterContainerDataGenerator(ScreenHandlerType<?> screenHandlerType) {
		super(screenHandlerType);
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return "Portable Techno Crafter";
	}
}