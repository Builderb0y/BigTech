package builderb0y.bigtech.util;

import java.util.concurrent.locks.Lock;

public class Locked<T> implements AutoCloseable {

	public final T value;
	public final Lock lock;

	public Locked(T value, Lock lock) {
		this.value = value;
		this.lock = lock;
		lock.lock();
	}

	@Override
	public void close() {
		this.lock.unlock();
	}
}