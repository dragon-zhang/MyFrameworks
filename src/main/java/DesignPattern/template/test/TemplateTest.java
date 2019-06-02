package DesignPattern.template.test;

import DesignPattern.template.CabbageCook;

/**
 * @author SuccessZhang
 * 业务场景：保证做饭的流程
 * 避免代码重复，符合最少知道原则
 * 但是如果顶层业务有变，所有子类可能都需要改动，不是很好符合开闭原则
 */
public class TemplateTest {
    public static void main(String[] args) {
        CabbageCook cabbageCook = new CabbageCook();
        cabbageCook.cook();
    }
}
