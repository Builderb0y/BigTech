package builderb0y.bigtech.datagen.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import builderb0y.bigtech.items.BigTechItems;

/**
specifies a DataGenerator to use for the annotated element.
the {@link #value()} is expected to have a single constructor
taking an instance of the annotated element.
see {@link BigTechItems} for a good example of how this works.

if {@link #value()} returns {@code void.class}, this indicates that
the annotated element should not have an associated data generator.
this is mostly intended for blocks whose data generator is
actually specified by the BlockItem instead of the Block.

specifying void.class instead of just leaving the field
un-annotated is an intentional safeguard to ensure
that I didn't simply forget to annotate the field.
*/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseDataGen {

	public abstract Class<?> value();
}