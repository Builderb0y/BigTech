package builderb0y.bigtech.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;

import net.minecraft.client.gui.screen.Screen;

import builderb0y.bigtech.config.BigTechConfig;

public class ModMenuCompat implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return (Screen parent) -> {
			try {
				return ClothCode.getConfigScreen(parent);
			}
			catch (LinkageError ignored) {
				return null;
			}
		};
	}

	public static class ClothCode {

		public static Screen getConfigScreen(Screen parent) {
			return AutoConfig.getConfigScreen(BigTechConfig.class.asSubclass(ConfigData.class), parent).get();
		}
	}
}