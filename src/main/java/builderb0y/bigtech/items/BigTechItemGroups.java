package builderb0y.bigtech.items;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
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

	public static final List<ItemStack>
		MATERIAL_ITEMS   = collectItems(MaterialItems  .class),
		DECO_ITEMS       = collectItems(DecoItems      .class),
		FUNCTIONAL_ITEMS = collectItems(FunctionalItems.class);

	@UseDataGen(ItemGroupDataGenerator.class)
	public static final ItemGroup
		MATERIAL = register("material", MATERIAL_ITEMS, () -> MaterialItems.CRYSTAL_CLUSTERS.red.getDefaultStack()),
		DECO = register("deco", DECO_ITEMS, () -> DecoItems.IRON_FRAME.getDefaultStack()),
		FUNCTIONAL = register("functional", FUNCTIONAL_ITEMS, () -> FunctionalItems.PORTABLE_TECHNO_CRAFTER.getDefaultStack());

	public static void init() {}

	public static ItemGroup register(String name, List<ItemStack> stacks, Supplier<ItemStack> icon) {
		return Registry.register(
			Registries.ITEM_GROUP,
			BigTechMod.modID(name),
			FabricItemGroup
			.builder()
			.displayName(Text.translatable("itemGroup.${BigTechMod.MODID}.${name}"))
			.icon(icon)
			.entries((displayContext, entries) -> entries.addAll(stacks))
			.build()
		);
	}

	public static List<ItemStack> collectItems(Class<?> clazz) {
		return (
			Arrays
			.stream(clazz.getDeclaredFields())
			.filter((Field field) -> (field.getModifiers() & (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL))
			.flatMap((Field field) -> {
				try {
					if (Item.class.isAssignableFrom(field.getType())) {
						Item item = field.get(null).as();
						if (item instanceof InventoryVariants variants) {
							return variants.getInventoryStacks();
						}
						return Stream.of(item.getDefaultStack());
					}
					else if (RegistrableCollection.class.isAssignableFrom(field.getType())) {
						return Arrays.stream(field.get(null).<RegistrableCollection<?>>as().getRegistrableVariants()).map(variant -> variant.object().<Item>as().getDefaultStack());
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
	}
}