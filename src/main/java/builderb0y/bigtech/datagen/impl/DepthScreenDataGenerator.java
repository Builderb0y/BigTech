package builderb0y.bigtech.datagen.impl;

import net.minecraft.screen.ScreenHandlerType;

import builderb0y.bigtech.datagen.base.DataGenContext;

public class DepthScreenDataGenerator extends InventoryDataGenerator {

	public DepthScreenDataGenerator(ScreenHandlerType<?> screenHandlerType) {
		super(screenHandlerType);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put("container.bigtech.dislocator.depth", "Depth: %s");
	}
}