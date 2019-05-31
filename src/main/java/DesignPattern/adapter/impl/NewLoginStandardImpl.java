package DesignPattern.adapter.impl;

import DesignPattern.adapter.NewLoginStandard;

/**
 * @author SuccessZhang
 * 要点：1.继承老标准；2.实现新标准。
 * 满足开闭原则、最少知道原则，违背依赖倒置原则
 */
public class NewLoginStandardImpl extends OldLoginStandardImpl implements NewLoginStandard {
    @Override
    public boolean faceLogin(String base64) {
        //do something
        return false;
    }
}
