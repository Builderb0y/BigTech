package builderb0y.bigtech.tweaks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class PlacementSpeed {

	public static final KeyBinding
		CYCLE_STATE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.bigtech.cycle_state", GLFW.GLFW_KEY_UNKNOWN, KeyBinding.GAMEPLAY_CATEGORY)),
		PLACE_DELAY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.bigtech.place_delay", GLFW.GLFW_KEY_UNKNOWN, KeyBinding.GAMEPLAY_CATEGORY)),
		BREAK_DELAY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.bigtech.break_delay", GLFW.GLFW_KEY_UNKNOWN, KeyBinding.GAMEPLAY_CATEGORY));

	public static int
		creativePlaceDelay = 4,
		survivalPlaceDelay = 4,
		creativeBreakDelay = 5,
		survivalBreakDelay = 5;

	public static void init() {}

	public static int getPlaceDelay() {
		return getDisplayedPlaceDelay();
	}

	public static int getBreakDelay() {
		return getDisplayedBreakDelay() - 1;
	}

	public static int getDisplayedPlaceDelay() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			return player.isInCreativeMode() ? creativePlaceDelay : survivalPlaceDelay;
		}
		else {
			return 0;
		}
	}

	public static int getDisplayedBreakDelay() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			return player.isInCreativeMode() ? creativeBreakDelay : survivalBreakDelay;
		}
		else {
			return 0;
		}
	}

	public static void changePlaceDelay(boolean forward) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			if (player.isInCreativeMode()) {
				if (forward) {
					creativePlaceDelay++;
					MinecraftClient.getInstance().options.write();
				}
				else if (creativePlaceDelay > 1) {
					creativePlaceDelay--;
					MinecraftClient.getInstance().options.write();
				}
			}
			else {
				if (forward) {
					survivalPlaceDelay++;
					MinecraftClient.getInstance().options.write();
				}
				else if (survivalPlaceDelay > 1) {
					survivalPlaceDelay--;
					MinecraftClient.getInstance().options.write();
				}
			}
			player.sendMessage(Text.translatable("overlay.bigtech.place_delay", getDisplayedPlaceDelay()), true);
		}
	}

	public static void changeBreakDelay(boolean forward) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			if (player.isInCreativeMode()) {
				if (forward) {
					creativeBreakDelay++;
					MinecraftClient.getInstance().options.write();
				}
				else if (creativeBreakDelay > 1) {
					creativeBreakDelay--;
					MinecraftClient.getInstance().options.write();
				}
			}
			else {
				if (forward) {
					survivalBreakDelay++;
					MinecraftClient.getInstance().options.write();
				}
				else if (survivalBreakDelay > 1) {
					survivalBreakDelay--;
					MinecraftClient.getInstance().options.write();
				}
			}
			player.sendMessage(Text.translatable("overlay.bigtech.break_delay", getDisplayedBreakDelay()), true);
		}
	}
}