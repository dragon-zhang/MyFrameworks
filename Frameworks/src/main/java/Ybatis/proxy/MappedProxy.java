package Ybatis.proxy;

import Ybatis.annotation.Param;
import Ybatis.pojo.SQL;
import Ybatis.session.SqlSession;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author SuccessZhang
 */
@AllArgsConstructor
public class MappedProxy implements InvocationHandler {

    private SqlSession session;

    private Map<String, Map<String, Ybatis.pojo.Method>> mappers;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Object[] correctArgs = new Object[args.length];
        Ybatis.pojo.Method myMethod = Ybatis.pojo.Method.changeToMethod(mappers.get(className).get(methodName));
        List<String> names = myMethod.getParamNames();
        Set<String> notNull = myMethod.getParamNotNull();
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
                //判空及空值处理
                if (correctArgs[i] == null) {
                    if (notNull.contains(names.get(i))) {
                        SQL sql = myMethod.getSql();
                        //替换where后面的判空
                        sql.setOrigin(sql.getOrigin().replace(" and " + sql.getWhere().getSentence().get(i), ""));
                        myMethod.setSql(sql);
                        mappers.get(className).put(methodName, myMethod);
                    }
                }
            } else {
                throw new RuntimeException("sql param:" + name + " not find !");
            }
        }
        //5.动态代理对象回调SqlSession中的方法
        Class<?> returnType = method.getReturnType();
        //如果返回类型是Collection的子类
        if (methodName.startsWith("query")) {
            if (Collection.class.isAssignableFrom(returnType)) {
                return session.selectList(className, methodName, correctArgs);
            } else {
                return session.selectOne(className, methodName, correctArgs);
            }
        } else if (methodName.startsWith("update") | methodName.startsWith("set")) {
            return session.update(className, methodName, correctArgs);
        } else {
            throw new RuntimeException("the nomenclature of methods is not standardized !");
        }
    }
}
