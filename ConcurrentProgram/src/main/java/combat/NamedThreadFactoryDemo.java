package combat;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author SuccessZhang
 * @date 2019/11/21
 * 创建有名字的线程池，方便日后定位问题。
 */
public class NamedThreadFactoryDemo {

    public static class NamedThreadFactory implements ThreadFactory {

        private static final AtomicInteger POOL_ID = new AtomicInteger();

        private final ThreadGroup threadGroup;

        private final AtomicInteger nextId = new AtomicInteger();

        private final String prefix;

        public NamedThreadFactory() {
            this(null);
        }

        public NamedThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            threadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            if (name == null || name.isEmpty()) {
                name = "pool";
            }
            prefix = name + "-" + POOL_ID.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(threadGroup, r, prefix + nextId, 0);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(
                6,
                12,
                5,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(10),
                new NamedThreadFactory("name"),
                new ThreadPoolExecutor.AbortPolicy());
        executor.execute(() -> {
            throw new RuntimeException("just for test");
        });
        executor.shutdown();
    }

}
