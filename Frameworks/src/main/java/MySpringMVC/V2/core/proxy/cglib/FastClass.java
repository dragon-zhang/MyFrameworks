package MySpringMVC.V2.core.proxy.cglib;

import MySpringMVC.V2.core.proxy.ProxyHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author SuccessZhang
 * @date 2020/05/12
 */
public interface FastClass {

    default Object invoke(Method superMethod, Object[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> superClass = superMethod.getDeclaringClass();
        Object instance = ProxyHelper.getClassInstance(superClass);
        if (instance == null) {
            instance = superClass.newInstance();
            ProxyHelper.putClassInstance(superClass, instance);
        }
        return invoke(getIndex(superMethod.getName(), superMethod.getParameterTypes()), instance, args);
    }

    default Object invoke(Object obj, Method superMethod, Object[] args) throws InvocationTargetException {
        return invoke(getIndex(superMethod.getName(), superMethod.getParameterTypes()), obj, args);
    }

    /**
     * Return the index of the matching method. The index may be used
     * later to invoke the method with less overhead. If more than one
     * method matches (i.e. they differ by return type only), one is
     * chosen arbitrarily.
     *
     * @param name           the method name
     * @param parameterTypes the parameter array
     * @return the index, or <code>-1</code> if none is found.
     * @see #invoke(int, Object, Object[])
     */
    int getIndex(String name, Class[] parameterTypes);

    /**
     * Invoke the method with the specified index.
     *
     * @param index the method index
     * @param obj   the object the underlying method is invoked from
     * @param args  the arguments used for the method call
     * @throws java.lang.reflect.InvocationTargetException if the underlying method throws an exception
     * @see #getIndex(String, Class[])
     */
    Object invoke(int index, Object obj, Object[] args) throws InvocationTargetException;

}
