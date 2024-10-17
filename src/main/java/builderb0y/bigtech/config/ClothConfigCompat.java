package builderb0y.bigtech.config;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;

public class ClothConfigCompat {

	public static Supplier<BigTechConfig> init() {
		try {
			return ClothCode.initCloth();
		}
		catch (LinkageError error) {
			ConfigLoader.LOGGER.info("Failed to register ConfigSerializer. Cloth Config is probably not installed.");
			return initFallback();
		}
	}

	public static Supplier<BigTechConfig> initFallback() {
		return Suppliers.ofInstance(ConfigLoader.loadAndSave());
	}

	public static class ClothCode {

		public static Supplier<BigTechConfig> initCloth() {
			AutoConfig.register(BigTechConfig.class.asSubclass(ConfigData.class), ClothCode::createSerializer);
			return AutoConfig.getConfigHolder(BigTechConfig.class.asSubclass(ConfigData.class)).as();
		}

		public static ConfigSerializer<ConfigData> createSerializer(Config annotation, Class<?> clazz) {
			return new ConfigSerializer<>() {

				@Override
				public void serialize(ConfigData config) throws SerializationException {
					try {
						ConfigLoader.save(config.as());
					}
					catch (Exception exception) {
						throw new SerializationException(exception);
					}
				}

				@Override
				public ConfigData deserialize() throws SerializationException {
					try {
						return ConfigLoader.load().as();
					}
					catch (Exception exception) {
						throw new SerializationException(exception);
					}
				}

				@Override
				public ConfigData createDefault() {
					return new BigTechConfig().as();
				}
			};
		}
	}
}