package builderb0y.bigtech.datagen.impl;

import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.LocalizedDataGenerator;

public class InventoryDataGenerator implements LocalizedDataGenerator {

	public final ScreenHandlerType<?> screenHandlerType;

	public InventoryDataGenerator(ScreenHandlerType<?> screenHandlerType) {
		this.screenHandlerType = screenHandlerType;
	}

	@Override
	public Identifier getId() {
		return Registries.SCREEN_HANDLER.getId(this.screenHandlerType);
	}

	@Override
	public String getLangKey(DataGenContext context) {
		return Util.createTranslationKey("container", this.id);
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return context.underscoresToCapitals(this.id.path);
	}
}