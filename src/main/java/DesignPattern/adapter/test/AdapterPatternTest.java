package DesignPattern.adapter.test;

import DesignPattern.adapter.NewLoginStandard;
import DesignPattern.adapter.impl.NewLoginStandardImpl;

/**
 * @author SuccessZhang
 */
public class AdapterPatternTest {
    public static void main(String[] args) {
        NewLoginStandard loginStandard = new NewLoginStandardImpl();
        loginStandard.login("userName", "password");
        loginStandard.faceLogin("base64img");
    }
}
