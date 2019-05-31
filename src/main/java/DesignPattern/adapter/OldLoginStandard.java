package DesignPattern.adapter;

/**
 * @author SuccessZhang
 * 业务场景：已有原先的登录接口及实现类，现在需要扩展人脸识别登录，并且兼容原来的接口
 */
public interface OldLoginStandard {

    boolean login(String userName, String password);

    boolean thirdPartyLogin(Object data);
}
