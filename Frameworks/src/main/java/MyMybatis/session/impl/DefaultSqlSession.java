package MyMybatis.session.impl;

import MyMybatis.executor.Executor;
import MyMybatis.executor.impl.DefaultExecutor;
import MyMybatis.pojo.Configuration;
import MyMybatis.proxy.InterfaceProxy;
import MyMybatis.session.SqlSession;

import java.util.List;

/**
 * @author SuccessZhang
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration config;

    private Executor executor;

    private InterfaceProxy factory;

    public DefaultSqlSession(Configuration config) {
        this.config = config;
        executor = new DefaultExecutor(config);
        factory = new InterfaceProxy<>();
    }

    @Override
    public <T> T selectOne(String statement, Object... parameters) {
        //6.SqlSession将方法转发给Executor
        List<T> result = executor.query(config.getMappedStatements().get(statement), parameters);
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
    public <E> List<E> selectList(String statement, Object... parameters) {
        //6.SqlSession将方法转发给Executor
        return executor.query(config.getMappedStatements().get(statement), parameters);
    }

    @Override
    public int update(String statement, Object... parameters) {
        //6.SqlSession将方法转发给Executor
        return executor.update(config.getMappedStatements().get(statement), parameters);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getMapper(Class<T> type) {
        factory.setMapperInterface(type);
        //4.通过SqlSession获取mapper接口的动态代理对象
        return (T) factory.newInstance(this, config.getMappedStatements());
    }
}
