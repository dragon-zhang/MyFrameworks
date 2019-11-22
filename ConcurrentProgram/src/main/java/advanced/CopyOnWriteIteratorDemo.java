package advanced;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author SuccessZhang
 * 实际上是COWIterator，没有CopyOnWriteIterator。
 * COWIterator是一种采用写时复制策略的iterator，具有弱一致性的特点
 * (返回迭代器后，其他线程对list的增删改对迭代器不可见)。
 */
public class CopyOnWriteIteratorDemo {

    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("My");
        list.add("stage name");
        list.add("is");
        list.add("SuccessZhang");

        Thread thread = new Thread(() -> {
            list.add("I");
            list.add("am");
            list.add("The Iron Man");
        });

        //先获取迭代器再启动线程
        Iterator<String> iterator = list.iterator();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

}
