package builderb0y.bigtech.items;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.BigTechMod;

/**
welcome! the actual item declarations are in {@link FunctionalItems} and {@link DecoItems}.
if I ever add a 3rd creative tab, it'll get a new class too.
*/
public class BigTechItems {

	public static void init() {
		FunctionalItems.init();
		DecoItems.init();
		DispenserBlock.registerBehavior(
			FunctionalItems.LIGHTNING_BATTERY,
			new ItemDispenserBehavior() {

				public static final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

				@Override
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
					Direction direction = pointer.state().get(Properties.FACING);
					BlockPos pos = pointer.pos().offset(direction);
					if (
						stack
						.useOnBlock(
							new ItemUsageContext(
								pointer.world(),
								null,
								Hand.MAIN_HAND,
								stack,
								new BlockHitResult(
									pos.toCenterPos(),
									direction.getOpposite(),
									pos,
									false
								)
							)
						)
						!= ActionResult.PASS
					) {
						return stack;
					}
					else {
						return fallbackBehavior.dispense(pointer, stack);
					}
				}
			}
		);
	}

	public static RegistryKey<Item> key(String name) {
		return key(BigTechMod.modID(name));
	}

	public static RegistryKey<Item> key(Identifier id) {
		return RegistryKey.of(RegistryKeys.ITEM, id);
	}

	public static Item.Settings settings(String name, boolean block) {
		Item.Settings settings = new Item.Settings().registryKey(key(name));
		if (block) settings = settings.useBlockPrefixedTranslationKey();
		return settings;
	}

	public static Item.Settings settings(Identifier name, boolean block) {
		Item.Settings settings = new Item.Settings().registryKey(key(name));
		if (block) settings = settings.useBlockPrefixedTranslationKey();
		return settings;
	}

	public static BlockItem registerPlacer(Block block) {
		Identifier id = Registries.BLOCK.getId(block);
		return register(id, new BlockItem(block, settings(id, true)));
	}

	public static <I extends Item> I register(I item) {
		Identifier id = BigTechMod.modID(
			item.getTranslationKey().substring(
				item.getTranslationKey().lastIndexOf('.') + 1
			)
		);
		return Registry.register(Registries.ITEM, id, item);
	}

	public static <I extends Item> I register(Identifier id, I item) {
		return Registry.register(Registries.ITEM, id, item);
	}
}