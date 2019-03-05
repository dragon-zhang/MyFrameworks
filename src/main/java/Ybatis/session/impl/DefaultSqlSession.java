package Ybatis.session.impl;


import Ybatis.executor.Executor;
import Ybatis.executor.impl.DefaultExecutor;
import Ybatis.pojo.Configuration;
import Ybatis.pojo.Method;
import Ybatis.proxy.InterfaceProxy;
import Ybatis.session.SqlSession;

import java.util.List;

/**
 * @author SuccessZhang
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration config;

    private Executor executor;

    private InterfaceProxy proxy;

    public DefaultSqlSession(Configuration config) {
        this.config = config;
        executor = new DefaultExecutor(config);
        proxy = new InterfaceProxy<>();
    }

    @Override
    public <T> T selectOne(String className, String methodName, Object... parameters) {
        //6.SqlSession将方法转发给Executor
        List<T> result = executor.query(Method.changeToMethod(config.getMappers().get(className).get(methodName)), parameters);
        if (result == null) {
            return null;
        } else if (result.size() == 0) {
            return null;
        } else if (result.size() == 1) {
            return result.get(0);
        } else {
            throw new RuntimeException("too many results !");
        }
    }

    @Override
    public <E> List<E> selectList(String className, String methodName, Object... parameters) {
        //6.SqlSession将方法转发给Executor
        return executor.query(Method.changeToMethod(config.getMappers().get(className).get(methodName)), parameters);
    }

    @Override
    public int update(String className, String methodName, Object... parameters) {
        //6.SqlSession将方法转发给Executor
        return executor.update(Method.changeToMethod(config.getMappers().get(className).get(methodName)), parameters);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getMapper(Class<T> type) {
        proxy.setMapperInterface(type);
        //4.通过SqlSession获取mapper接口的动态代理对象
        return (T) proxy.newInstance(this, config.getMappers());
    }
}
