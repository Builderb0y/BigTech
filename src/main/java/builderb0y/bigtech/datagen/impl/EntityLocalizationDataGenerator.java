package builderb0y.bigtech.datagen.impl;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.LocalizedDataGenerator;

public class EntityLocalizationDataGenerator implements LocalizedDataGenerator {

	public final EntityType<?> type;

	public EntityLocalizationDataGenerator(EntityType<?> type) {
		this.type = type;
	}

	@Override
	public Identifier getId() {
		return Registries.ENTITY_TYPE.getId(this.type);
	}

	@Override
	public String getLangKey(DataGenContext context) {
		return Util.createTranslationKey("entity", this.getId());
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return context.underscoresToCapitals(this.getId().getPath());
	}
}