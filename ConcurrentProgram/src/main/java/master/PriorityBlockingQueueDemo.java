package master;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author SuccessZhang
 * 对于PriorityBlockingQueue来说，
 * 要么队列元素实现{@link Comparable}接口，
 * 要么构造PriorityBlockingQueue时传入
 * 初始队列容量 和 {@link Comparator}；
 * <p>
 * 内部使用平衡二叉树堆实现，直接遍历队列元素不保证有序；
 * 默认优先级值越小，优先级越高(当然这个可以自定义)。
 */
public class PriorityBlockingQueueDemo {

    public static class Task {

        private int priority;

        private String name;

        public int getPriority() {
            return priority;
        }

        public Task(int priority, String name) {
            this.priority = priority;
            this.name = name;
        }

        public void doTask() {
            System.out.println(this.name + ":" + this.priority);
        }
    }

    public static void main(String[] args) {
        PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(
                11, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Integer.compare(o1.getPriority(), o2.getPriority());
            }
        });
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            queue.offer(new Task(random.nextInt(10), "name" + i));
        }
        while (!queue.isEmpty()) {
            queue.poll().doTask();
        }
    }

}
