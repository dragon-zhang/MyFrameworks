package MySpringMVC.V2.core.proxy.cglib;

import MySpringMVC.V2.core.proxy.ProxyHelper;
import MySpringMVC.V2.core.proxy.source.CodeFile;

/**
 * @author SuccessZhang
 * @date 2020/04/10
 */
@SuppressWarnings("unused")
public class Enhancer {

    /**
     * 手写版cglib动态代理实现
     */
    public static Object create(Class type, MethodInterceptor callback) {
        try {
            //3.生成java源代码文件
            CodeFile codeSource = new CodeFile(type);
            return ProxyHelper.newProxyInstanceByCglib(codeSource, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
