package builderb0y.bigtech.items;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.api.MagnetiteAttractableEntity;
import builderb0y.bigtech.blocks.MagnetiteBlock;

public class MagnetiteArmorMaterial {

	public static final RegistryEntry<ArmorMaterial> INSTANCE;
	static {
		EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
		defense.put(ArmorItem.Type.BOOTS,      2);
		defense.put(ArmorItem.Type.LEGGINGS,   5);
		defense.put(ArmorItem.Type.CHESTPLATE, 6);
		defense.put(ArmorItem.Type.HELMET,     2);
		defense.put(ArmorItem.Type.BODY,       5);
		INSTANCE = Registry.registerReference(
			Registries.ARMOR_MATERIAL,
			BigTechMod.modID("magnetite"),
			new ArmorMaterial(
				defense,
				9,
				SoundEvents.ITEM_ARMOR_EQUIP_IRON,
				() -> Ingredient.ofItems(FunctionalItems.MAGNETITE_INGOT),
				Collections.singletonList(new ArmorMaterial.Layer(BigTechMod.modID("magnetite"))),
				0.0F,
				0.0F
			)
		);
	}

	public static void onEntityTick(LivingEntity living) {
		if (!living.getWorld().isClient) {
			int itemCount = 0;
			for (ItemStack stack : living.getArmorItems()) {
				if (stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == MagnetiteArmorMaterial.INSTANCE) {
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