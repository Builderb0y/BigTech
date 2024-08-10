package builderb0y.bigtech.armorMaterials;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import builderb0y.bigtech.BigTechMod;

public class ArmorMaterialTags {

	public static final TagKey<ArmorMaterial> SHOCK_PROTECTION = of("shock_protection");

	public static TagKey<ArmorMaterial> of(String name) {
		return TagKey.of(RegistryKeys.ARMOR_MATERIAL, BigTechMod.modID(name));
	}
}