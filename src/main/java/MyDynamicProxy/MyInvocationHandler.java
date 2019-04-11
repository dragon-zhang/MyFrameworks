package MyDynamicProxy;

import java.lang.reflect.Method;

/**
 * @author SuccessZhang
 */
public interface MyInvocationHandler {
    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
