package builderb0y.bigtech.util;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
an AsyncRunner runs tasks asynchronously and concurrently,
then waits for them to finish.
this allows expensive computations to be done in parallel,
while still enforcing something similar to flow control for task completion.
tasks can be submitted at any time,
and tasks will be finished when {@link #close()} is called.
any tasks which are not finished when {@link #close()} is called
will be waited for before the calling thread resumes execution.
example usage: {@code
	try (AsyncRunner async = new AsyncRunner()) {
		async.submit(() -> expensiveOperation(1));
		async.submit(() -> expensiveOperation(2));
		async.submit(() -> expensiveOperation(3));
		//all 3 tasks are worked on in parallel.
	}
	//closing waits for all 3 tasks to finish running.
}
note: while this class assists with parallel computation,
it is not itself thread safe. do not submit new tasks concurrently!

see also: {@link AsyncConsumer}.
*/
public class AsyncRunner implements AutoCloseable {

	public final LinkedList<CompletableFuture<Void>> waitingOn = new LinkedList<>();

	public void submit(Runnable runnable) {
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