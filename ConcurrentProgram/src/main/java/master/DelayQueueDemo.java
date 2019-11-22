package master;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author SuccessZhang
 * DelayQueue是一个并发的无界阻塞延迟队列，队列中每个元素都有过期时间，
 * 当从队列中获取元素时，只有过期元素才会出队，队列头是最快要过期的元素。
 * <p>
 * 内部采用PriorityQueue存放数据、采用ReentrantLock实现线程同步；
 * 队列中的元素需要实现{@link Delayed}接口。
 */
public class DelayQueueDemo {

    public static class Task implements Delayed {

        private final long delayTime;

        private final long expiredTime;

        private String name;

        public Task(long delayTime, String name) {
            this.delayTime = delayTime;
            this.expiredTime = System.currentTimeMillis() + delayTime;
            this.name = name;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        public void doTask() {
            System.out.println(this.name + ":" + this.delayTime + ":" + this.expiredTime);
        }
    }

    public static void main(String[] args) {
        DelayQueue<Task> queue = new DelayQueue<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            queue.offer(new Task(random.nextInt(10000), "task" + i));
        }
        while (!queue.isEmpty()) {
            try {
                queue.take().doTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
