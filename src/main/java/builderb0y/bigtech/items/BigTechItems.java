package builderb0y.bigtech.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.util.ColorF;

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

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		ColorProviderRegistry.ITEM.register(
			(ItemStack stack, int tintIndex) -> {
				if (tintIndex == 1) {
					NbtCompound nbt = stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).getNbt();
					if (nbt != null) {
						int[] color = nbt.getIntArray("color");
						if (color.length == 3) {
							return ColorF.toInt(color[0], color[1], color[2]);
						}
					}
				}
				return -1;
			},
			FunctionalItems.BEAM_INTERCEPTOR
		);
	}

	public static RegistryKey<Item> key(String name) {
		return key(BigTechMod.modID(name));
	}

	public static RegistryKey<Item> key(Identifier id) {
		return RegistryKey.of(RegistryKeys.ITEM, id);
	}

	public static Item.Settings settings(String name) {
		return new Item.Settings().registryKey(key(name));
	}

	public static Item.Settings settings(Identifier id) {
		return new Item.Settings().registryKey(key(id));
	}

	public static BlockItem registerPlacer(Block block) {
		Identifier id = Registries.BLOCK.getId(block);
		return register(id, new BlockItem(block, settings(id)));
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