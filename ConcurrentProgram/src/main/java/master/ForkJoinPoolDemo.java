package master;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @author SuccessZhang
 * ForkJoinPool核心思想类似归并排序，先把大任务拆分成多个小任务，
 * 利用现代CPU多核心的优势，计算小任务的结果，再把这些结果累加，
 * 得到最终结果。
 * 另一个核心则是WorkStealing算法，其理念在于让空闲线程从忙碌线程的
 * 双端队列中偷取任务。默认情况下，一个工作线程从它自己内部的双端
 * 队列的头部获取任务。但当线程自身的内部队列中没有任务时，它从其他
 * 繁忙线程的双端队列(或者全局的双端队列)中获取任务(一般是尾部，
 * 因为队列的尾部最有可能存在还未执行的任务)。这种方式减小了线程
 * 之间对任务竞争的可能性，它也使得线程以最大可能性去获取可执行的任务
 * ，因为它们总是在最有可能存在还未执行的任务的地方寻找任务。
 * <p>
 * 下面的demo中，计算1~3E的累加结果，采用ForkJoinPool，只需要1ms；
 * 传统的单核心计算for each则花费180ms。
 */
public class ForkJoinPoolDemo {

    private static ForkJoinPool pool = ForkJoinPool.commonPool();

    /**
     * 执行任务
     * RecursiveTask：有返回值；
     * RecursiveAction：无返回值。
     */
    public static class SumTask extends RecursiveTask<Long> {
        private long[] numbers;
        private int from;
        private int to;

        public SumTask(long[] numbers, int from, int to) {
            this.numbers = numbers;
            this.from = from;
            this.to = to;
        }

        //此方法为ForkJoin的核心方法：对任务进行拆分  拆分的好坏决定了效率的高低
        @Override
        protected Long compute() {
            //当需要计算的数字个数小于2^24时，直接采用for loop方式计算结果
            if (to - from < (1 << 25)) {
                long total = 0;
                for (int i = from; i <= to; i++) {
                    total += numbers[i];
                }
                return total;
            } else {
                //否则，把任务一分为二，递归拆分(注意此处有递归)到底拆分成多少分 需要根据具体情况而定
                int middle = (from + to) / 2;
                SumTask taskLeft = new SumTask(numbers, from, middle);
                SumTask taskRight = new SumTask(numbers, middle + 1, to);
                taskLeft.fork();
                taskRight.fork();
                return taskLeft.join() + taskRight.join();
            }
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long[] numbers = LongStream.rangeClosed(1, 300000000).toArray();
        Instant start = Instant.now();
        ForkJoinTask<Long> result = pool.submit(new SumTask(numbers, 0, numbers.length - 1));
        pool.shutdown();
        /*long result = 0;
        for (long number : numbers) {
            result = result + number;
        }*/
        Instant end = Instant.now();
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result.get());
    }

}
