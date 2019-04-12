package MyDynamicProxy;

import java.lang.reflect.Method;

/**
 * @author SuccessZhang
 * 自定义的InvocationHandler
 */
@SuppressWarnings("unused")
public interface MyInvocationHandler {
    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
