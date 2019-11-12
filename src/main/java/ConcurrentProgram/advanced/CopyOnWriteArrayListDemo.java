package ConcurrentProgram.advanced;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author SuccessZhang
 * CopyOnWriteArrayList是一个并发安全的ArrayList，其实现原理是：
 * 在一个复制的快照数组上进行修改操作，并且使用了写时复制策略。
 * 相对应的，JUC也提供了CopyOnWriteArraySet。
 */
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        /*
         * add()实现逻辑：
         * 1.获取独占锁；
         * 2.读取原数组；
         * 3.复制原数组到新数组，将元素添加到新数组；
         * 4.新数组替换原数组；
         * 5.释放锁。
         * */
        list.add(10);
        /*
         * set()实现逻辑：
         * 1.获取独占锁；
         * 2.读取原数组；
         * 3.访问指定位置的元素；
         * 4.如果新旧元素值不一样，复制原数组到新数组，
         * 将元素添加到新数组，用新数组替换原数组；
         * 如果新旧元素值一样，直接设置为原数组；
         * 5.释放锁。
         *
         * 并发时可能导致x线程明明已经删除了原数组1下标的元素，
         * 但是y线程仍会读取到原数组1下标的元素。
         * */
        list.set(0, 11);
        /*
         * get()实现逻辑：
         * 1.获取数组；
         * 2.访问指定位置的元素。
         *
         * 并发时可能导致x线程明明已经删除了原数组1下标的元素，
         * 但是y线程仍会读取到原数组1下标的元素。
         * */
        list.get(0);
        /*
         * remove()大体实现逻辑与其他update操作类似，细节略微不同：
         * 1.如果删除的是最后一个元素，直接复制数组大小减少1的原数组；
         * 2.如果删除的是中间的元素，分两次分别复制该元素前面和后面的
         * 所有元素，再用新数组替换旧数组。
         * */
        list.remove(0);
        System.out.println(list);
    }

}
