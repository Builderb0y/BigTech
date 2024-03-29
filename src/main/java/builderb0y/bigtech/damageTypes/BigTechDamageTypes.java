package builderb0y.bigtech.damageTypes;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.DamageTypeDataGenerator;

public class BigTechDamageTypes {

	@UseDataGen(DamageTypeDataGenerator.Shocking.class)
	public static final RegistryKey<DamageType> SHOCKING   = of("shocking");
	@UseDataGen(DamageTypeDataGenerator.TeslaCoil.class)
	public static final RegistryKey<DamageType> TESLA_COIL = of("tesla_coil");
	@UseDataGen(DamageTypeDataGenerator.Ignitor.class)
	public static final RegistryKey<DamageType> IGNITOR    = of("ignitor");

	public static RegistryKey<DamageType> of(String name) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BigTechMod.modID(name));
	}
}