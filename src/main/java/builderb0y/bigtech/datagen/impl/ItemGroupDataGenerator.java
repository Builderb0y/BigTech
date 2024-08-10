package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.LocalizedDataGenerator;

public class ItemGroupDataGenerator implements LocalizedDataGenerator {

	public final ItemGroup itemGroup;

	public ItemGroupDataGenerator(ItemGroup itemGroup) {
		this.itemGroup = itemGroup;
	}

	@Override
	public Identifier getId() {
		return Registries.ITEM_GROUP.getId(this.itemGroup);
	}

	@Override
	public String getLangKey(DataGenContext context) {
		Identifier id = this.getId();
		return "itemGroup.${id.getNamespace()}.${id.getPath()}";
	}

	@Override
	public String getLangValue(DataGenContext context) {
		Identifier id = this.getId();
		return "${BigTechMod.MODNAME} (${context.underscoresToCapitals(id.getPath())})";
	}
}