package DesignPattern.adapter.impl;

import DesignPattern.adapter.OldLoginStandard;

/**
 * @author SuccessZhang
 */
public class OldLoginStandardImpl implements OldLoginStandard {
    @Override
    public boolean login(String userName, String password) {
        //do something
        return false;
    }

    @Override
    public boolean thirdPartyLogin(Object data) {
        //do something
        return false;
    }
}
