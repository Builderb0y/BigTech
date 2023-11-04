package builderb0y.bigtech.beams;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.HashCommon;

import net.minecraft.util.math.MathHelper;

/**
UNUSED, and only left here because it was a cool concept.
I ended up needing the keys to be of type short,
so I went with a Short2ObjectMap<LinkedList<T>> instead.

MultiMap-like class which stores its values in a single-linked list.
this class does not actually implement MultiMap because there are a
*lot* of methods in MultiMap and I don't need all of them, nor do I
want to spend the time implementing all of them.

anyway, the backing array structure looks somewhat like this:
ab .. cd ef //the table itself
gh .. .. ei //the {@link Entry#next} entries for the table.
aj .. .. .. //the next next entries.
for some objects a through j.
the first character in a pair represents a key, the 2nd represents a value.
notice that in this example, a and g have the same hash code mod 4,
so they occupy the same bin in the table.
the collection of values for a, and the collection you will get if you call
{@link #iterator(Object)}(a), consists of b and j.
likewise, the collection for e consists of f and i.

this implementation is intended for cases where it is known
in advance that most keys will map to very few values.
*/
public class LinkedMultiMap<K, V> {

	public static final int MIN_SIZE = 16;

	public Entry<K, V>[] table;
	public int binsInUse, minFill, maxFill;

	@SuppressWarnings("unchecked")
	public LinkedMultiMap(int capacity) {
		capacity = MathHelper.smallestEncompassingPowerOfTwo(capacity);
		this.table = new Entry[capacity + 1];
		this.minFill = capacity >>> 2;
		this.maxFill = this.minFill | (capacity >>> 1); //capacity * 3 / 4
	}

	public int getTableSize() {
		return this.table.length - 1;
	}

	public int getMask() {
		return this.table.length - 2;
	}

	public Iterator<V> iterator(K key) {
		int hash, index;
		if (key == null) {
			hash = 0;
			index = this.tableSize;
		}
		else {
			hash = HashCommon.mix(key.hashCode());
			index = hash & this.mask;
		}
		return new IteratorImpl<>(this.table[index], key, hash);
	}

	public Iterable<V> iterable(K key) {
		return () -> this.iterator(key);
	}

	public Stream<V> stream(K key) {
		int hash, index;
		if (key == null) {
			hash = 0;
			index = this.tableSize;
		}
		else {
			hash = HashCommon.mix(key.hashCode());
			index = hash & this.mask;
		}
		return (
			Stream
			.iterate(this.table[index], Objects::nonNull, entry -> entry.next)
			.filter(entry -> entry.keyHash == hash && Objects.equals(entry.key, key))
			.map(entry -> entry.value)
		);
	}

	public void add(K key, V value) {
		int hash, index;
		if (key == null) {
			hash = 0;
			index = this.tableSize;
		}
		else {
			hash = HashCommon.mix(key.hashCode());
			index = hash & this.mask;
		}
		Entry<K, V> entry = new Entry<>(key, hash, value);
		entry.next = this.table[index];
		this.table[index] = entry;
		if (entry.next == null) {
			this.binsInUse++;
			this.checkSize();
		}
	}

	public boolean remove(K key, Object value) {
		return this.remove(key, value != null ? value::equals : Objects::isNull, true);
	}

	public boolean remove(K key, Predicate<? super V> predicate, boolean stopAfterFirst) {
		if (this.removeImpl(key, predicate, stopAfterFirst)) {
			this.checkSize();
			return true;
		}
		return false;
	}

	public boolean removeImpl(K key, Predicate<? super V> predicate, boolean stopAfterFirst) {
		int hash, index;
		if (key == null) {
			hash = 0;
			index = this.tableSize;
		}
		else {
			hash = HashCommon.mix(key.hashCode());
			index = hash & this.mask;
		}
		Entry<K, V> entry = this.table[index];
		if (entry == null) return false;
		boolean changed = false;
		while (entry.keyHash == hash && Objects.equals(entry.key, key) && predicate.test(entry.value)) {
			Entry<K, V> next = entry.next;
			entry.next = null;
			this.table[index] = next;
			if (next == null) {
				this.binsInUse--;
				return true;
			}
			if (stopAfterFirst) return true;
			entry = next;
			changed = true;
		}
		while (true) {
			Entry<K, V> next = entry.next;
			if (next == null) return changed;
			if (entry.keyHash == hash && Objects.equals(next.key, key) && predicate.test(next.value)) {
				entry.next = next.next;
				next.next = null;
				if (stopAfterFirst) return true;
			}
			entry = next;
		}
	}

	public void checkSize() {
		int newSize;
		if (this.binsInUse >= this.maxFill) {
			newSize = this.tableSize << 1;
			this.rehash(newSize);
		}
		else if (this.binsInUse < this.minFill) {
			newSize = this.tableSize >>> 1;
			if (newSize >= MIN_SIZE) {
				this.rehash(newSize);
			}
		}
	}

	public void rehash(int newSize) {
		assert (newSize & (newSize - 1)) == 0;
		Entry<K, V>[] oldArray = this.table;
		@SuppressWarnings("unchecked")
		Entry<K, V>[] newArray = new Entry[newSize + 1];
		int oldArraySize = this.tableSize;
		int newArrayMask = newSize - 1;
		int newBinsInUse = 0;
		for (int index = 0; index < oldArraySize; index++) {
			Entry<K, V> entry = oldArray[index];
			while (entry != null) {
				Entry<K, V> next = entry.next;
				int newIndex = entry.keyHash & newArrayMask;
				entry.next = newArray[newIndex];
				if (entry.next == null) newBinsInUse++;
				newArray[newIndex] = entry;
				entry = next;
			}
		}
		newArray[newSize] = oldArray[oldArraySize];
		this.table = newArray;
		this.binsInUse = newBinsInUse;
		this.minFill = newSize >>> 2;
		this.maxFill = this.minFill | (newSize >>> 1);
	}

	public void transferNodesFrom(LinkedMultiMap<K, V> that) {
		int newCapacity = MathHelper.smallestEncompassingPowerOfTwo(this.binsInUse + that.binsInUse);
		if (this.tableSize < newCapacity) this.rehash(newCapacity);
		int thisTableSize = this.tableSize;
		int thisMask = this.mask;
		int thatTableSize = that.tableSize;
		for (int thatIndex = 0; thatIndex < thatTableSize; thatIndex++) {
			Entry<K, V> entry = that.table[thatIndex];
			that.table[thatIndex] = null;
			while (entry != null) {
				Entry<K, V> next = entry.next;
				int thisIndex = entry.keyHash & thisMask;
				entry.next = this.table[thisIndex];
				this.table[thisIndex] = entry;
				if (entry.next == null) this.binsInUse++;
				entry = next;
			}
		}
		Entry<K, V> nullEntry = that.table[thatTableSize];
		that.table[thatTableSize] = null;
		while (nullEntry != null) {
			Entry<K, V> next = nullEntry.next;
			nullEntry.next = this.table[thisTableSize];
			this.table[thisTableSize] = nullEntry;
			if (nullEntry.next == null) this.binsInUse++;
			nullEntry = next;
		}
	}

	public static class IteratorImpl<K, V> implements Iterator<V> {

		public final K key;
		public final int keyHash;
		public Entry<K, V> current;

		public IteratorImpl(Entry<K, V> firstEntry, K key, int keyHash) {
			this.key = key;
			this.keyHash = keyHash;
			this.current = firstEntry;
			while (this.current != null && !(this.current.keyHash == keyHash && Objects.equals(this.current.key, key))) {
				this.current = this.current.next;
			}
		}

		@Override
		public boolean hasNext() {
			return this.current != null;
		}

		@Override
		public V next() {
			Entry<K, V> current = this.current;
			if (current == null) throw new NoSuchElementException();
			do this.current = this.current.next;
			while (this.current != null && !(this.current.keyHash == this.keyHash && Objects.equals(this.current.key, this.key)));
			return current.value;
		}
	}

	public static class Entry<K, V> implements Map.Entry<K, V> {

		public final K key;
		public final int keyHash;
		public V value;
		public Entry<K, V> next;

		public Entry(K key, int keyHash, V value) {
			this.key = key;
			this.keyHash = keyHash;
			this.value = value;
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.value;
		}

		@Override
		public V setValue(V newValue) {
			V oldValue = this.value;
			this.value = newValue;
			return oldValue;
		}
	}
}