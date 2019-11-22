package MyJedis;

public class MyJedisTestDemo {

    public static void main(String[] args) {
        MyJedis.init("127.0.0.1", 6379);
        System.out.println(MyJedis.set("test", "test"));
        MyJedis.expire("test", 600);
        MyJedis.expireAt("test", 1550827254);
        System.out.println(MyJedis.exists("test"));
    }

}
