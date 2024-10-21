package builderb0y.bigtech.compat.computercraft;

import java.util.*;

import dan200.computercraft.api.lua.LuaTable;
import org.jetbrains.annotations.NotNull;

public class LuaArrayTable<V> extends AbstractMap<Object, V> implements LuaTable<Object, V> {

	public Object[] array;

	@SafeVarargs
	public LuaArrayTable(V... array) {
		this.array = array;
	}

	@Override
	public int length() {
		return this.array.length;
	}

	@Override
	public V get(Object key) {
		if (key instanceof Number number) {
			int index = number.intValue() - 1;
			if (index >= 0 && index < this.array.length) {
				return this.array[index].as();
			}
		}
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		if (key instanceof Number number) {
			int index = number.intValue() - 1;
			if (index >= 0 && index < this.array.length) {
				return true;
			}
		}
		return false;
	}

	@Override
	public @NotNull Set<Entry<Object, V>> entrySet() {
		return this.new EntrySet();
	}

	public class EntrySet extends AbstractSet<Entry<Object, V>> {

		@Override
		public Iterator<Entry<Object, V>> iterator() {
			return LuaArrayTable.this.new EntryIterator();
		}

		@Override
		public int size() {
			return LuaArrayTable.this.size();
		}

		@Override
		public boolean contains(Object o) {
			return o instanceof Entry<?, ?> entry && Objects.equals(LuaArrayTable.this.get(entry.getKey()), entry.getValue());
		}
	}

	public class EntryIterator implements Iterator<Entry<Object, V>> {

		public int index;

		@Override
		public boolean hasNext() {
			return this.index < LuaArrayTable.this.array.length;
		}

		@Override
		public Entry<Object, V> next() {
			if (this.index >= LuaArrayTable.this.array.length) throw new NoSuchElementException();
			int index = this.index++;
			return new TableEntry<>(index + 1, LuaArrayTable.this.array[index].as());
		}
	}

	public static record TableEntry<V>(Object getKey, V getValue) implements Entry<Object, V> {

		@Override
		public boolean equals(Object obj) {
			return (
				obj instanceof Map.Entry<?, ?> that &&
				Objects.equals(this.getKey(), that.getKey()) &&
				Objects.equals(this.getValue(), that.getValue())
			);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(this.getKey()) ^ Objects.hashCode(this.getValue());
		}

		@Override
		public String toString() {
			return this.getKey() + " -> " + this.getValue();
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}
	}
}