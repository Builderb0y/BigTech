package builderb0y.bigtech.registrableCollections;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface RegistrableCollection<T> extends Iterable<T> {

	public abstract List<T> asList();

	public default Stream<T> stream() {
		return this.asList().stream();
	}

	public default Iterator<T> iterator() {
		return this.asList().iterator();
	}

	@Override
	public abstract void forEach(Consumer<? super T> action);

	@Override
	public abstract Spliterator<T> spliterator();

	public abstract boolean contains(T object);

	public abstract RegistrableVariant<T>[] getRegistrableVariants();

	public static record RegistrableVariant<T>(T object, Object variant) {}
}