package proxy.impl;

import proxy.Person;

/**
 * @author SuccessZhang
 */
public class Man implements Person {

    private String name;

    @Override
    public String getName() {
        return this.name;
    }

    public Man(String name) {
        this.name = name;
    }

    @Override
    public void findLove() {
        System.out.println(this.name + "想找个肤白貌美大长腿的对象");
    }
}
