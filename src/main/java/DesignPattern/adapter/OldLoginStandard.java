package DesignPattern.adapter;

/**
 * @author SuccessZhang
 */
public interface OldLoginStandard {

    boolean login(String userName, String password);

    boolean thirdPartyLogin(Object data);
}
