package builderb0y.bigtech.compatibility;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.texture.Sprite;

public class SodiumCompatibility {

	public static final boolean ENABLED = FabricLoader.getInstance().isModLoaded("sodium");

	public static void markSpriteActive(Sprite sprite) {
		if (ENABLED) SodiumCode.markSpriteActive(sprite);
	}

	public static class SodiumCode {

		public static void markSpriteActive(Sprite sprite) {
			SpriteUtil.markSpriteActive(sprite);
		}
	}
}