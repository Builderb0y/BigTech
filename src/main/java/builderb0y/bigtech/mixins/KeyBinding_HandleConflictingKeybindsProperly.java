package builderb0y.bigtech.mixins;

import java.util.*;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

/**
I can't believe minecraft doesn't handle this correctly by default.
good thing I have incredibly simple mixins at my disposal.
*/
@Mixin(KeyBinding.class)
public class KeyBinding_HandleConflictingKeybindsProperly {

	@Unique
	private static final Map<InputUtil.Key, List<KeyBinding>> BIGTECH_KEY_BINDINGS = new HashMap<>();

	@Shadow @Final private static Map<String, KeyBinding> KEYS_BY_ID;

	@Shadow private int timesPressed;
	@Shadow private InputUtil.Key boundKey;

	@Overwrite
	public static void onKeyPressed(InputUtil.Key key) {
		for (KeyBinding keyBinding : BIGTECH_KEY_BINDINGS.getOrDefault(key, Collections.emptyList())) {
			keyBinding.<KeyBinding_HandleConflictingKeybindsProperly>as().timesPressed++;
		}
	}

	@Overwrite
	public static void setKeyPressed(InputUtil.Key key, boolean pressed) {
		for (KeyBinding keyBinding : BIGTECH_KEY_BINDINGS.getOrDefault(key, Collections.emptyList())) {
			keyBinding.setPressed(pressed);
		}
	}

	@Inject(method = "updateKeysByCode", at = @At("TAIL"))
	private static void bigtech_updateKeys(CallbackInfo callback) {
		BIGTECH_KEY_BINDINGS.clear();
		for (KeyBinding keyBinding : KEYS_BY_ID.values()) {
			BIGTECH_KEY_BINDINGS.computeIfAbsent(keyBinding.<KeyBinding_HandleConflictingKeybindsProperly>as().boundKey, $ -> new ArrayList<>(4)).add(keyBinding);
		}
	}

	@Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At("RETURN"))
	private void bigtech_storeInCorrectMap(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo callback) {
		BIGTECH_KEY_BINDINGS.computeIfAbsent(this.boundKey, $ -> new ArrayList<>(4)).add(this.as());
	}
}