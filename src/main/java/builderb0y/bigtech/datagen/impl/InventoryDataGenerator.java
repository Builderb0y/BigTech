package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.LocalizedDataGenerator;

public class InventoryDataGenerator implements LocalizedDataGenerator {

	public final BlockEntityType<?> blockEntityType;

	public InventoryDataGenerator(BlockEntityType<?> blockEntityType) {
		this.blockEntityType = blockEntityType;
	}

	@Override
	public Identifier getId() {
		return Registries.BLOCK_ENTITY_TYPE.getId(this.blockEntityType);
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