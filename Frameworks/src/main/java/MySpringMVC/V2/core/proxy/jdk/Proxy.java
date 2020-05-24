package MySpringMVC.V2.core.proxy.jdk;

import MySpringMVC.V2.core.proxy.ProxyHelper;
import MySpringMVC.V2.core.proxy.source.CodeFile;

/**
 * @author SuccessZhang
 * @date 2020/04/10
 */
@SuppressWarnings("unused")
public class Proxy {

    protected final InvocationHandler h;

    protected Proxy(InvocationHandler h) {
        this.h = h;
    }

    /**
     * 手写版jdk动态代理实现
     */
    public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h) {
        try {
            //3.生成java源代码
            CodeFile codeSource = new CodeFile(interfaces);
            return ProxyHelper.newProxyInstanceByJdk(codeSource, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
