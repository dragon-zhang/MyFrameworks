package DesignPattern.adapter;

/**
 * @author SuccessZhang
 */
public interface NewLoginStandard extends OldLoginStandard {

    boolean faceLogin(String base64img);
}
