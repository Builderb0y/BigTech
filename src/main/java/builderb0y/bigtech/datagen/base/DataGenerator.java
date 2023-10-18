package builderb0y.bigtech.datagen.base;

import net.minecraft.util.Identifier;

public interface DataGenerator {

	public abstract Identifier getId();

	public abstract String getLangKey(DataGenContext context);

	public abstract String getLangValue(DataGenContext context);

	public default void setupLang(DataGenContext context) {
		context.lang.put(this.getLangKey(context), this.getLangValue(context));
	}

	public default void run(DataGenContext context) {
		this.setupLang(context);
	}
}