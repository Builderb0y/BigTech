package builderb0y.bigtech.items;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.registrableCollections.RegistrableCollection;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.ItemGroupDataGenerator;

public class BigTechItemGroups {

	public static final List<ItemStack> ITEMS = (
		Arrays
		.stream(BigTechItems.class.getDeclaredFields())
		.filter((Field field) -> (field.getModifiers() & (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL))
		.flatMap((Field field) -> {
			try {
				if (Item.class.isAssignableFrom(field.type)) {
					Item item = field.get(null).as();
					if (item instanceof InventoryVariants variants) {
						return variants.getInventoryStacks();
					}
					return Stream.of(item.defaultStack);
				}
				else if (RegistrableCollection.class.isAssignableFrom(field.type)) {
					return Arrays.stream(field.get(null).<RegistrableCollection<?>>as().getRegistrableVariants()).map(variant -> variant.object.<Item>as().defaultStack);
				}
				else {
					return Stream.empty();
				}
			}
			catch (ReflectiveOperationException exception) {
				throw new AssertionError("Should not fail to get public field", exception);
			}
		})
		.toList()
	);

	@UseDataGen(ItemGroupDataGenerator.class)
	public static final ItemGroup INSTANCE = Registry.register(
		Registries.ITEM_GROUP,
		BigTechMod.modID(BigTechMod.MODID),
		FabricItemGroup
		.builder()
		.displayName(Text.translatable("itemGroup.${BigTechMod.MODID}"))
		.icon(() -> BigTechItems.BELT.defaultStack)
		.entries((displayContext, entries) -> entries.addAll(ITEMS))
		.build()
	);

	public static void init() {}
}