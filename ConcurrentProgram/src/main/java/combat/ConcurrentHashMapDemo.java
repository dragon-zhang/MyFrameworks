package combat;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SuccessZhang
 * 在高并发的场景下，调用put(K key, V value)会丢失部分数据，
 * 可以改为调用putIfAbsent(K key, V value)。如果key已经存在，
 * 该方法会返回原先的value，不存在则相当于直接put，当然具体
 * 还得看业务场景，如果确实需要替换的话，直接用put就行。
 */
public class ConcurrentHashMapDemo {

    public static void main(String[] args) {
        ConcurrentHashMap<String, List<String>> map = new ConcurrentHashMap<>();
        Thread thread1 = new Thread(() -> {
            List<String> list = new ArrayList<>();
            list.add("test1");
            list.add("test2");
            List<String> old = map.putIfAbsent("list1", list);
            if (old != null) {
                old.addAll(list);
            }
            System.out.println(JSON.toJSONString(map));
        });
        Thread thread2 = new Thread(() -> {
            List<String> list = new ArrayList<>();
            list.add("test3");
            list.add("test4");
            List<String> old = map.putIfAbsent("list1", list);
            if (old != null) {
                old.addAll(list);
            }
            System.out.println(JSON.toJSONString(map));
        });
        Thread thread3 = new Thread(() -> {
            List<String> list = new ArrayList<>();
            list.add("test5");
            list.add("test6");
            List<String> old = map.putIfAbsent("list2", list);
            if (old != null) {
                old.addAll(list);
            }
            System.out.println(JSON.toJSONString(map));
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }

}
