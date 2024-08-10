package builderb0y.bigtech.datagen.impl;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Util;

import builderb0y.bigtech.datagen.base.DataGenContext;

public class MinerDataGenerator extends InventoryDataGenerator {

	public MinerDataGenerator(ScreenHandlerType<?> screenHandlerType) {
		super(screenHandlerType);
	}

	@Override
	public String getLangKey(DataGenContext context) {
		return Util.createTranslationKey("entity", this.getId());
	}
}