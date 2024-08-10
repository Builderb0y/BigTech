package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.ArmorMaterials;
import net.minecraft.registry.tag.BlockTags;

import builderb0y.bigtech.armorMaterials.ArmorMaterialTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class VanillaTweaksDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(MiningToolTags.AXE).add(BlockTags.LEAVES);
		context.getTags(ArmorMaterialTags.SHOCK_PROTECTION).addAll(ArmorMaterials.IRON, ArmorMaterials.GOLD, ArmorMaterials.CHAIN);
	}
}