package builderb0y.bigtech.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.BigTechMod;

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
					Direction direction = pointer.state.get(Properties.FACING);
					BlockPos pos = pointer.pos.offset(direction);
					if (
						stack
						.useOnBlock(
							new ItemUsageContext(
								pointer.world,
								null,
								Hand.MAIN_HAND,
								stack,
								new BlockHitResult(
									pos.toCenterPos(),
									direction.opposite,
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
			(stack, tintIndex) -> {
				if (tintIndex == 1) {
					NbtCompound nbt = stack.getSubNbt(BlockItem.BLOCK_ENTITY_TAG_KEY);
					if (nbt != null) {
						float[] color = nbt.getFloatArray("color");
						if (color.length == 3) {
							return MathHelper.packRgb(color[0], color[1], color[2]) | 0xFF000000;
						}
					}
				}
				return -1;
			},
			FunctionalItems.BEAM_INTERCEPTOR
		);
	}

	public static BeltBlockItem registerBelt(Block block) {
		return register(Registries.BLOCK.getId(block), new BeltBlockItem(block, new Item.Settings()));
	}

	public static CatwalkStairsBlockItem registerCatwalkStairs(Block block) {
		return register(Registries.BLOCK.getId(block), new CatwalkStairsBlockItem(block, new Item.Settings()));
	}

	public static BlockItem registerPlacer(Block block) {
		return register(Registries.BLOCK.getId(block), new BlockItem(block, new Item.Settings()));
	}

	public static <I extends Item> I register(String name, I item) {
		return register(BigTechMod.modID(name), item);
	}

	public static <I extends Item> I register(Identifier id, I item) {
		return Registry.register(Registries.ITEM, id, item);
	}
}