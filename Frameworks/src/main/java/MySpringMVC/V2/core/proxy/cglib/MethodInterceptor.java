package MySpringMVC.V2.core.proxy.cglib;

import java.lang.reflect.Method;

/**
 * @author SuccessZhang
 * @date 2020/04/11
 */
@SuppressWarnings("unused")
public interface MethodInterceptor {

    /**
     * All generated proxied methods call this method instead of the original method.
     * The original method may either be invoked by normal reflection using the Method object,
     * or by using the MethodProxy (faster).
     *
     * @param obj    "this", the enhanced object
     * @param method intercepted Method
     * @param args   argument array; primitive types are wrapped
     * @param proxy  used to invoke super (non-intercepted method); may be called
     *               as many times as needed
     * @return any value compatible with the signature of the proxied method. Method returning void will ignore this value.
     * @throws Throwable any exception may be thrown; if so, super method will not be invoked
     * @see MethodProxy
     */
    Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable;
}
