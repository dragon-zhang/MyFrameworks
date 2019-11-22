package MySpringMVC.V2.aop;

/**
 * @author SuccessZhang
 * 需要jdk动态代理增强的方法需要写成接口并且继承于本接口，
 */
@SuppressWarnings("unused")
public interface AOPMethods {

    void before();

    void aroundBefore();

    void afterReturning(Object result);

    void afterThrowing(Throwable throwable);

    void after();

    void aroundAfter();
}
