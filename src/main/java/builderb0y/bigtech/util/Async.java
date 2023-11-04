package builderb0y.bigtech.util;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Async implements AutoCloseable {

	public final LinkedList<CompletableFuture<Void>> waitingOn = new LinkedList<>();

	public void run(Runnable runnable) {
		this.waitingOn.add(CompletableFuture.runAsync(runnable));
	}

	@Override
	public void close() {
		CompletionException exception = null;
		for (CompletableFuture<Void> future; (future = this.waitingOn.pollFirst()) != null;) {
			try {
				future.join();
			}
			catch (Throwable throwable) {
				try {
					if (exception == null) {
						exception = new CompletionException("Some tasks failed to complete, see below.", null);
					}
					exception.addSuppressed(throwable);
				}
				catch (Throwable ignored) {
					//better to swallow the exception than to risk race conditions
					//caused by aborting the enclosing loop too early.
				}
			}
		}
		if (exception != null) throw exception;
	}
}