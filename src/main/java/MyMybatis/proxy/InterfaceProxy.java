package MyMybatis.proxy;

import MyMybatis.pojo.MappedStatement;
import MyMybatis.session.SqlSession;

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
    public T newInstance(SqlSession sqlSession, Map<String, MappedStatement> mappedStatements) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new MappedProxy(sqlSession, mappedStatements));
    }

}