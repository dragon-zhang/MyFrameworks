package Ybatis.proxy;

import Ybatis.pojo.Method;
import Ybatis.session.impl.DefaultSqlSession;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author SuccessZhang
 */
public class InterfaceProxy<T> {

    private Class mapperInterface;

    public void setMapperInterface(Class mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class getMapperInterface() {
        return mapperInterface;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(DefaultSqlSession sqlSession, Map<String, Map<String, Method>> mappers) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new MappedProxy(sqlSession, mappers));
    }

}