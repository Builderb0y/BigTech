package builderb0y.bigtech.items;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.DislocatorScreenHandler;

public class DislocatorItem extends Item {

	public DislocatorItem(Settings settings) {
		super(settings);
	}

	@Override
	public int getEnchantability() {
		return 15;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			PlayerEntity player = context.getPlayer();
			context.getStack().damage(
				1,
				context.getWorld() instanceof ServerWorld serverWorld ? serverWorld : null,
				player instanceof ServerPlayerEntity serverPlayer ? serverPlayer : null,
				(Item item) -> {
					if (player != null) player.sendEquipmentBreakStatus(
						item,
						switch (context.getHand()) {
							case MAIN_HAND -> EquipmentSlot.MAINHAND;
							case  OFF_HAND -> EquipmentSlot. OFFHAND;
						}
					);
				}
			);
			if (player != null) player.openHandledScreen(new ExtendedScreenHandlerFactory<Byte>() {

				@Override
				public Text getDisplayName() {
					return context.getStack().getName();
				}

				@Override
				public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					Direction facing = context.getSide().getOpposite();
					Direction horizontal = facing.getHorizontal() >= 0 ? facing : player.getHorizontalFacing();
					return new DislocatorScreenHandler(
						BigTechScreenHandlerTypes.DISLOCATOR,
						syncId,
						new WorldInventory(
							player,
							switch (context.getHand()) {
								case MAIN_HAND -> player.getInventory().selectedSlot;
								case  OFF_HAND -> 40;
							},
							context.getBlockPos(),
							facing,
							horizontal.rotateYClockwise(),
							facing == Direction.UP ? horizontal : facing == Direction.DOWN ? horizontal.getOpposite() : Direction.DOWN
						),
						playerInventory
					);
				}

				@Override
				public Byte getScreenOpeningData(ServerPlayerEntity player) {
					return switch (context.getHand()) {
						case MAIN_HAND -> (byte)(player.getInventory().selectedSlot);
						case OFF_HAND -> 40;
					};
				}
			});
		}
		return ActionResult.SUCCESS;
	}
}