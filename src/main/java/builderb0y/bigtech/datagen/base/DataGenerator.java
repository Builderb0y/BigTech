package builderb0y.bigtech.datagen.base;

import net.minecraft.util.Identifier;

public interface DataGenerator {

	public abstract Identifier getID();

	public abstract String getLangKey(DataGenContext context);

	public abstract String getLangValue(DataGenContext context);
}