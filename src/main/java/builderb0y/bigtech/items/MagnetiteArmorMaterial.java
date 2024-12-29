package builderb0y.bigtech.items;

import java.util.EnumMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.api.MagnetiteAttractableEntity;
import builderb0y.bigtech.blocks.MagnetiteBlock;

public class MagnetiteArmorMaterial {

	public static final RegistryKey<EquipmentAsset> MODEL = RegistryKey.of(
		EquipmentAssetKeys.REGISTRY_KEY,
		BigTechMod.modID("magnetite")
	);
	public static final ArmorMaterial INSTANCE;
	static {
		EnumMap<EquipmentType, Integer> defense = new EnumMap<>(EquipmentType.class);
		defense.put(EquipmentType.BOOTS,      2);
		defense.put(EquipmentType.LEGGINGS,   5);
		defense.put(EquipmentType.CHESTPLATE, 6);
		defense.put(EquipmentType.HELMET,     2);
		defense.put(EquipmentType.BODY,       5);
		INSTANCE = new ArmorMaterial(
			15,
			defense,
			9,
			SoundEvents.ITEM_ARMOR_EQUIP_IRON,
			0.0F,
			0.0F,
			BigTechItemTags.REPAIRS_MAGNETITE_ARMOR,
			MODEL
		);
	}

	public static void onEntityTick(LivingEntity living) {
		if (!living.getWorld().isClient) {
			int itemCount = 0;
			for (ItemStack stack : living.getArmorItems()) {
				if (stack.isIn(BigTechItemTags.MAGNETIC_ARMOR)) {
					itemCount++;
				}
			}
			if (itemCount > 0) {
				List<Entity> entities = (
					living
					.getWorld()
					.getEntitiesByClass(
						Entity.class,
						living.getBoundingBox().expand(itemCount << 1),
						(Entity entity) -> (
							entity instanceof MagnetiteAttractableEntity attractable &&
							!attractable.isImmuneToMagnetiteArmor(living) &&
							entity.isAlive() &&
							!entity.isSpectator()
						)
					)
				);
				if (!entities.isEmpty()) {
					for (Entity entity : entities) {
						MagnetiteBlock.attract(
							entity,
							living.getX(),
							living.getY() + living.getHeight() * 0.5D,
							living.getZ(),
							entity.<MagnetiteAttractableEntity>as().getMagnetiteAttractionForce(),
							itemCount << 1,
							true
						);
					}
				}
			}
		}
	}
}