package MyMybatis.proxy;

import MyMybatis.annotation.Param;
import MyMybatis.pojo.MappedStatement;
import MyMybatis.session.SqlSession;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author SuccessZhang
 */
@AllArgsConstructor
public class MappedProxy implements InvocationHandler {

    private SqlSession session;

    private Map<String, MappedStatement> mappedStatements;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String location = method.getDeclaringClass().getName() + "." + method.getName();
        Object[] correctArgs = new Object[args.length];
        List<String> names = mappedStatements.get(location).getParamNames();
        Parameter[] params = method.getParameters();
        for (int i = 0; i < names.size(); i++) {
            int index = -1;
            String name = null;
            for (int j = 0; j < params.length; j++) {
                String paramName = params[j].getName();
                if (params[j].isAnnotationPresent(Param.class)) {
                    paramName = params[j].getAnnotation(Param.class).value().trim();
                }
                if (names.get(i).equals(paramName)) {
                    index = j;
                } else {
                    name = names.get(i);
                }
            }
            if (index != -1) {
                correctArgs[i] = args[index];
            } else {
                throw new RuntimeException("sql param:" + name + " not find !");
            }
        }
        //5.动态代理对象回调SqlSession中的方法
        Class<?> returnType = method.getReturnType();
        //如果返回类型是Collection的子类
        String methodName = method.getName();
        if (methodName.startsWith("query")) {
            if (Collection.class.isAssignableFrom(returnType)) {
                return session.selectList(location, correctArgs);
            } else {
                return session.selectOne(location, correctArgs);
            }
        } else if (methodName.startsWith("update") | methodName.startsWith("set")) {
            return session.update(location, correctArgs);
        } else {
            throw new RuntimeException("the nomenclature of methods is not standardized !");
        }
    }
}
