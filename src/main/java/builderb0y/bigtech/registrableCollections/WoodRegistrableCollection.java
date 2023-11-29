package builderb0y.bigtech.registrableCollections;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public abstract class WoodRegistrableCollection<T> implements RegistrableCollection<T> {

	public final T
		oak,
		spruce,
		birch,
		jungle,
		acacia,
		dark_oak,
		mangrove,
		cherry,
		crimson,
		warped;

	public WoodRegistrableCollection(
		String suffix,
		T oak,
		T spruce,
		T birch,
		T jungle,
		T acacia,
		T dark_oak,
		T mangrove,
		T cherry,
		T crimson,
		T warped
	) {
		this.oak      = oak;
		this.spruce   = spruce;
		this.birch    = birch;
		this.jungle   = jungle;
		this.acacia   = acacia;
		this.dark_oak = dark_oak;
		this.mangrove = mangrove;
		this.cherry   = cherry;
		this.crimson  = crimson;
		this.warped   = warped;

		if (suffix != null) this.register(suffix);
	}

	public WoodRegistrableCollection(String suffix, WoodRegistrableFactory<T> factory) {
		this(
			suffix,
			factory.create(Type.OAK),
			factory.create(Type.SPRUCE),
			factory.create(Type.BIRCH),
			factory.create(Type.JUNGLE),
			factory.create(Type.ACACIA),
			factory.create(Type.DARK_OAK),
			factory.create(Type.MANGROVE),
			factory.create(Type.CHERRY),
			factory.create(Type.CRIMSON),
			factory.create(Type.WARPED)
		);
	}

	public abstract void register(String suffix);

	@FunctionalInterface
	public static interface WoodRegistrableFactory<T> {

		public abstract T create(Type type);
	}

	public T get(Type type) {
		return switch (type) {
			case OAK      -> this.oak;
			case SPRUCE   -> this.spruce;
			case BIRCH    -> this.birch;
			case JUNGLE   -> this.jungle;
			case ACACIA   -> this.acacia;
			case DARK_OAK -> this.dark_oak;
			case MANGROVE -> this.mangrove;
			case CHERRY   -> this.cherry;
			case CRIMSON  -> this.crimson;
			case WARPED   -> this.warped;
		};
	}

	@Override
	public List<T> asList() {
		return Arrays.asList(
			this.oak,
			this.spruce,
			this.birch,
			this.jungle,
			this.acacia,
			this.dark_oak,
			this.mangrove,
			this.cherry,
			this.crimson,
			this.warped
		);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		action.accept(this.oak);
		action.accept(this.spruce);
		action.accept(this.birch);
		action.accept(this.jungle);
		action.accept(this.acacia);
		action.accept(this.dark_oak);
		action.accept(this.mangrove);
		action.accept(this.cherry);
		action.accept(this.crimson);
		action.accept(this.warped);
	}

	@Override
	public Spliterator<T> spliterator() {
		return Spliterators.spliterator(
			new Object[] {
				this.oak,
				this.spruce,
				this.birch,
				this.jungle,
				this.acacia,
				this.dark_oak,
				this.mangrove,
				this.cherry,
				this.crimson,
				this.warped
			},
			Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL
		);
	}

	@Override
	public boolean contains(T object) {
		return (
			this.oak      == object ||
			this.spruce   == object ||
			this.birch    == object ||
			this.jungle   == object ||
			this.acacia   == object ||
			this.dark_oak == object ||
			this.mangrove == object ||
			this.cherry   == object ||
			this.crimson  == object ||
			this.warped   == object
		);
	}

	@Override
	@SuppressWarnings("unchecked") //generic array.
	public RegistrableVariant<T>[] getRegistrableVariants() {
		return new RegistrableVariant[] {
			new RegistrableVariant<>(this.oak, Type.OAK),
			new RegistrableVariant<>(this.spruce, Type.SPRUCE),
			new RegistrableVariant<>(this.birch, Type.BIRCH),
			new RegistrableVariant<>(this.jungle, Type.JUNGLE),
			new RegistrableVariant<>(this.acacia, Type.ACACIA),
			new RegistrableVariant<>(this.dark_oak, Type.DARK_OAK),
			new RegistrableVariant<>(this.mangrove, Type.MANGROVE),
			new RegistrableVariant<>(this.cherry, Type.CHERRY),
			new RegistrableVariant<>(this.crimson, Type.CRIMSON),
			new RegistrableVariant<>(this.warped, Type.WARPED)
		};
	}

	public static enum Type {
		OAK,
		SPRUCE,
		BIRCH,
		JUNGLE,
		ACACIA,
		DARK_OAK,
		MANGROVE,
		CHERRY,
		CRIMSON,
		WARPED;

		public static final Type[] VALUES = values();
	}
}