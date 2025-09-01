package builderb0y.bigtech.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Lockable<T> {

	public final T value;
	public final ReentrantReadWriteLock lock;

	public Lockable(T value) {
		this.value = value;
		this.lock = new ReentrantReadWriteLock();
	}

	public Locked<T> read() {
		return new Locked<>(this.value, this.lock.readLock());
	}

	public Locked<T> write() {
		return new Locked<>(this.value, this.lock.writeLock());
	}
}