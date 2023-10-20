package builderb0y.bigtech.datagen.base;

public interface LocalizedDataGenerator extends DataGenerator {

	public abstract String getLangKey(DataGenContext context);

	public abstract String getLangValue(DataGenContext context);

	public default void setupLang(DataGenContext context) {
		context.lang.put(this.getLangKey(context), this.getLangValue(context));
	}

	@Override
	public default void run(DataGenContext context) {
		this.setupLang(context);
	}
}