package builderb0y.bigtech.damageTypes;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import builderb0y.bigtech.BigTechMod;

public class BigTechDamageTypes {

	public static final RegistryKey<DamageType>
		TESLA_COIL = of("tesla_coil");

	public static RegistryKey<DamageType> of(String name) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BigTechMod.modID(name));
	}
}