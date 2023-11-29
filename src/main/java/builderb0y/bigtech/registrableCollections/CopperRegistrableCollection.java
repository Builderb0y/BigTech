package builderb0y.bigtech.registrableCollections;

import java.util.*;
import java.util.function.Consumer;

import net.minecraft.block.Oxidizable.OxidationLevel;

public abstract class CopperRegistrableCollection<T> implements RegistrableCollection<T> {

	public final T
		copper,
		exposed_copper,
		weathered_copper,
		oxidized_copper,
		waxed_copper,
		waxed_exposed_copper,
		waxed_weathered_copper,
		waxed_oxidized_copper;

	public CopperRegistrableCollection(
		String suffix,
		T copper,
		T exposed_copper,
		T weathered_copper,
		T oxidized_copper,
		T waxed_copper,
		T waxed_exposed_copper,
		T waxed_weathered_copper,
		T waxed_oxidized_copper
	) {
		this.                copper =                 copper;
		this.        exposed_copper =         exposed_copper;
		this.      weathered_copper =       weathered_copper;
		this.       oxidized_copper =        oxidized_copper;
		this.          waxed_copper =           waxed_copper;
		this.  waxed_exposed_copper =   waxed_exposed_copper;
		this.waxed_weathered_copper = waxed_weathered_copper;
		this. waxed_oxidized_copper =  waxed_oxidized_copper;

		if (suffix != null) this.register(suffix);
	}

	public CopperRegistrableCollection(String suffix, SeparateCopperRegistrableFactory<T> unwaxedFactory, SeparateCopperRegistrableFactory<T> waxedFactory) {
		this(
			suffix,
			unwaxedFactory.create(OxidationLevel.UNAFFECTED),
			unwaxedFactory.create(OxidationLevel.EXPOSED),
			unwaxedFactory.create(OxidationLevel.WEATHERED),
			unwaxedFactory.create(OxidationLevel.OXIDIZED),
			waxedFactory  .create(OxidationLevel.UNAFFECTED),
			waxedFactory  .create(OxidationLevel.EXPOSED),
			waxedFactory  .create(OxidationLevel.WEATHERED),
			waxedFactory  .create(OxidationLevel.OXIDIZED)
		);
	}

	public CopperRegistrableCollection(String suffix, MergedCopperRegistrableFactory<T> factory) {
		this(
			suffix,
			factory.create(Type.                COPPER),
			factory.create(Type.        EXPOSED_COPPER),
			factory.create(Type.      WEATHERED_COPPER),
			factory.create(Type.       OXIDIZED_COPPER),
			factory.create(Type.          WAXED_COPPER),
			factory.create(Type.  WAXED_EXPOSED_COPPER),
			factory.create(Type.WAXED_WEATHERED_COPPER),
			factory.create(Type. WAXED_OXIDIZED_COPPER)
		);
	}

	public abstract void register(String suffix);

	@FunctionalInterface
	public static interface MergedCopperRegistrableFactory<T> {

		public abstract T create(Type type);
	}

	@FunctionalInterface
	public static interface SeparateCopperRegistrableFactory<T> {

		public abstract T create(OxidationLevel level);
	}

	public T get(Type type) {
		return switch (type) {
			case                 COPPER -> this.                copper;
			case         EXPOSED_COPPER -> this.        exposed_copper;
			case       WEATHERED_COPPER -> this.      weathered_copper;
			case        OXIDIZED_COPPER -> this.       oxidized_copper;
			case           WAXED_COPPER -> this.          waxed_copper;
			case   WAXED_EXPOSED_COPPER -> this.  waxed_exposed_copper;
			case WAXED_WEATHERED_COPPER -> this.waxed_weathered_copper;
			case  WAXED_OXIDIZED_COPPER -> this. waxed_oxidized_copper;
		};
	}

	@Override
	public List<T> asList() {
		return Arrays.asList(
			this.                copper,
			this.        exposed_copper,
			this.      weathered_copper,
			this.       oxidized_copper,
			this.          waxed_copper,
			this.  waxed_exposed_copper,
			this.waxed_weathered_copper,
			this. waxed_oxidized_copper
		);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		action.accept(this.                copper);
		action.accept(this.        exposed_copper);
		action.accept(this.      weathered_copper);
		action.accept(this.       oxidized_copper);
		action.accept(this.          waxed_copper);
		action.accept(this.  waxed_exposed_copper);
		action.accept(this.waxed_weathered_copper);
		action.accept(this. waxed_oxidized_copper);
	}

	@Override
	public Spliterator<T> spliterator() {
		return Spliterators.spliterator(
			new Object[] {
				this.                copper,
				this.        exposed_copper,
				this.      weathered_copper,
				this.       oxidized_copper,
				this.          waxed_copper,
				this.  waxed_exposed_copper,
				this.waxed_weathered_copper,
				this. waxed_oxidized_copper
			},
			Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL
		);
	}

	@Override
	public boolean contains(T object) {
		return (
			this.                copper == object ||
			this.        exposed_copper == object ||
			this.      weathered_copper == object ||
			this.       oxidized_copper == object ||
			this.          waxed_copper == object ||
			this.  waxed_exposed_copper == object ||
			this.waxed_weathered_copper == object ||
			this. waxed_oxidized_copper == object
		);
	}

	@Override
	@SuppressWarnings("unchecked") //generic array creation.
	public RegistrableVariant<T>[] getRegistrableVariants() {
		return new RegistrableVariant[] {
			new RegistrableVariant<>(this.                copper, Type.                COPPER),
			new RegistrableVariant<>(this.        exposed_copper, Type.        EXPOSED_COPPER),
			new RegistrableVariant<>(this.      weathered_copper, Type.      WEATHERED_COPPER),
			new RegistrableVariant<>(this.       oxidized_copper, Type.       OXIDIZED_COPPER),
			new RegistrableVariant<>(this.          waxed_copper, Type.          WAXED_COPPER),
			new RegistrableVariant<>(this.  waxed_exposed_copper, Type.  WAXED_EXPOSED_COPPER),
			new RegistrableVariant<>(this.waxed_weathered_copper, Type.WAXED_WEATHERED_COPPER),
			new RegistrableVariant<>(this. waxed_oxidized_copper, Type. WAXED_OXIDIZED_COPPER),
		};
	}

	public static enum Type {
		COPPER                (OxidationLevel.UNAFFECTED, false),
		EXPOSED_COPPER        (OxidationLevel.EXPOSED,    false),
		WEATHERED_COPPER      (OxidationLevel.WEATHERED,  false),
		OXIDIZED_COPPER       (OxidationLevel.OXIDIZED,   false),
		WAXED_COPPER          (OxidationLevel.UNAFFECTED, true),
		WAXED_EXPOSED_COPPER  (OxidationLevel.EXPOSED,    true),
		WAXED_WEATHERED_COPPER(OxidationLevel.WEATHERED,  true),
		WAXED_OXIDIZED_COPPER (OxidationLevel.OXIDIZED,   true);

		public static final Type[] VALUES = values();

		public final OxidationLevel level;
		public final boolean waxed;
		public final String lowerCaseName;
		public final String prefix;

		Type(OxidationLevel level, boolean waxed) {
			this.level = level;
			this.waxed = waxed;
			this.lowerCaseName = this.name().toLowerCase(Locale.ROOT);
			this.prefix = this.lowerCaseName + '_';
		}

		public static Type get(OxidationLevel level, boolean waxed) {
			return VALUES[level.ordinal() | (waxed ? 4 : 0)];
		}

		public Type waxed() {
			return VALUES[this.ordinal() | 4];
		}

		public Type notWaxed() {
			return VALUES[this.ordinal() & ~4];
		}

		public Type withLevel(OxidationLevel level) {
			return VALUES[level.ordinal() | (this.ordinal() & 4)];
		}
	}
}