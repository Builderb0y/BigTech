package builderb0y.bigtech.compatibility;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import net.minecraft.client.texture.Sprite;

import builderb0y.autocodec.util.AutoCodecUtil;
import builderb0y.bigtech.BigTechMod;

public class SodiumCompatibility {

	public static final MethodHandle markSpriteActive;
	static {
		MethodHandle handle;
		gotHandle: {
			try {
				Class<?> spriteUtil = Class.forName("me.jellysquid.mods.sodium.client.render.texture.SpriteUtil");
				handle = MethodHandles.lookup().findStatic(spriteUtil, "markSpriteActive", MethodType.methodType(void.class, Sprite.class));
				BigTechMod.LOGGER.info("Enabling old sodium laser beam compatibility code.");
				break gotHandle;
			}
			catch (Exception ignored) {}

			try {
				Class<?> spriteUtil = Class.forName("net.caffeinemc.mods.sodium.client.render.texture.SpriteUtil");
				handle = MethodHandles.lookup().findStatic(spriteUtil, "markSpriteActive", MethodType.methodType(void.class, Sprite.class));
				BigTechMod.LOGGER.info("Enabling new sodium laser beam compatibility code.");
				break gotHandle;
			}
			catch (Exception ignored) {}

			handle = null;
			BigTechMod.LOGGER.info("Not enabling sodium laser beam compatibility code, as sodium was not found.");
		}
		markSpriteActive = handle;
	}

	public static void markSpriteActive(Sprite sprite) {
		if (markSpriteActive != null) try {
			markSpriteActive.invokeExact(sprite);
		}
		catch (Throwable throwable) {
			throw AutoCodecUtil.rethrow(throwable);
		}
	}
}