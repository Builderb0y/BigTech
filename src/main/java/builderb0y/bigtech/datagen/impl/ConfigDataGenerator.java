package builderb0y.bigtech.datagen.impl;

import java.lang.reflect.Field;

import me.shedaniel.autoconfig.annotation.Config;

import builderb0y.autocodec.annotations.UseName;
import builderb0y.bigtech.config.BigTechConfig;
import builderb0y.bigtech.config.BigTechConfig.Tooltips;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class ConfigDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		runRecursive(context, BigTechConfig.class, "text.autoconfig." + BigTechConfig.class.getDeclaredAnnotation(Config.class).name() + ".option");
	}

	public static void runRecursive(DataGenContext context, Class<?> configClass, String prefix) {
		for (Field field : configClass.getDeclaredFields()) {
			Tooltips tooltips = field.getAnnotation(Tooltips.class);
			UseName useName = field.getAnnotatedType().getAnnotation(UseName.class);
			if (tooltips != null || useName != null) {
				if (tooltips != null && useName != null) {
					context.lang.put(prefix + '.' + field.getName(), useName.value());
					String[] lines = tooltips.value();
					for (int index = 0, length = lines.length; index < length; index++) {
						context.lang.put(prefix + '.' + field.getName() + ".@Tooltip[" + index + ']', lines[index]);
					}
					if (!field.getType().isPrimitive()) {
						runRecursive(context, field.getType(), prefix + '.' + field.getName());
					}
				}
				else {
					context.error("Missing annotation on " + field);
				}
			}
		}
	}
}