package app;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class CountingTimeThreadPoolExecutor implements Executor {

    private final ExecutorService executorService;
    private static final Logger LOGGER = Logger.getLogger(CountingTimeThreadPoolExecutor.class.getName());

    public CountingTimeThreadPoolExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void execute(Runnable command) {
        final long startTime = System.currentTimeMillis();
        executorService.execute(() -> {
            final long queueDuration = System.currentTimeMillis() - startTime;
            System.out.println("The task waited for " + queueDuration + " ms.");
            command.run();
        });
    }
}
