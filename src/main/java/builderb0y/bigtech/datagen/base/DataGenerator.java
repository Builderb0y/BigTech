package builderb0y.bigtech.datagen.base;

import net.minecraft.util.Identifier;

public interface DataGenerator {

	public abstract Identifier getId();

	public abstract void run(DataGenContext context);
}