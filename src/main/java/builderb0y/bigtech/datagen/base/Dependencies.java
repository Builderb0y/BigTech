package builderb0y.bigtech.datagen.base;

import java.lang.annotation.*;

/**
specifies that a DataGenerator depends on the
output from one or more other DataGenerator's.
for example, the dependency may emit a model that
several other data generators use as a parent model.
the {@link #value()} is expected to have a single
no-arg constructor (usually auto-generated by javac).
if multiple generators all depend on a shared generator,
the shared generator will only be
{@link DataGenerator#run(DataGenContext) ran} once.
*/
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependencies {

	public abstract Class<? extends DataGenerator>[] value();
}