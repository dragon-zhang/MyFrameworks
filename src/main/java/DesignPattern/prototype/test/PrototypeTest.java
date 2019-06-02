package DesignPattern.prototype.test;

import DesignPattern.prototype.deep.ManDeep;
import DesignPattern.prototype.shallow.ManShallow;

import java.util.Arrays;

/**
 * @author SuccessZhang
 * 浅克隆：复制的不是值，而是引用的地址，引用对象仍然指向原来的对象
 */
public class PrototypeTest {
    public static void main(String[] args) {
        //浅克隆
        ManShallow man1 = new ManShallow();
        man1.setName("张三");
        man1.setAge(20);
        man1.setHobbies(Arrays.asList("打篮球", "唱歌", "游泳"));
        ManShallow man2 = (ManShallow) man1.clone();
        System.out.println(man1.getHobbies());
        System.out.println(man2.getHobbies());
        System.out.println("对象地址比较:" + (man1 == man2));
        System.out.println("引用对象地址比较:" + (man1.getHobbies() == man2.getHobbies()));
        //深克隆
        ManDeep manA = new ManDeep();
        manA.setName("张三");
        manA.setAge(20);
        manA.setHobbies(Arrays.asList("打篮球", "唱歌", "游泳"));
        ManDeep manB = (ManDeep) manA.clone();
        manB.setHobbies(Arrays.asList("跳舞", "游泳"));
        System.out.println(manA.getHobbies());
        System.out.println(manB.getHobbies());
        System.out.println("对象地址比较:" + (manA == manB));
        System.out.println("引用对象地址比较:" + (manA.getHobbies() == manB.getHobbies()));
    }
}
