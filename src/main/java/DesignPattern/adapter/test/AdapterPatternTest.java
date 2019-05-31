package DesignPattern.adapter.test;

import DesignPattern.adapter.NewLoginStandard;
import DesignPattern.adapter.impl.NewLoginStandardImpl;

/**
 * @author SuccessZhang
 * 业务场景：已有原先的登录接口及实现类，现在需要扩展人脸识别登录，并且兼容原来的接口
 */
public class AdapterPatternTest {
    public static void main(String[] args) {
        NewLoginStandard loginStandard = new NewLoginStandardImpl();
        loginStandard.login("userName", "password");
        loginStandard.faceLogin("base64img");
    }
}
