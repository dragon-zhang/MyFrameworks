package Ybatis.session;

import java.util.List;

/**
 * @author SuccessZhang
 */
public interface SqlSession {

    /**
     * Retrieve a single row mapped from the className key and parameter.
     *
     * @param <T>        the returned object type
     * @param className  Unique identifier matching the className to use.
     * @param methodName Unique identifier matching the methodName to use.
     * @param parameters Parameters object to pass to the className.
     * @return Mapped object
     */
    <T> T selectOne(String className, String methodName, Object... parameters);

    /**
     * Retrieve a list of mapped objects from the statement key and parameter.
     *
     * @param <E>        the returned list element type
     * @param className  Unique identifier matching the statement to use.
     * @param methodName Unique identifier matching the methodName to use.
     * @param parameters Parameter object to pass to the statement.
     * @return List of mapped object
     */
    <E> List<E> selectList(String className, String methodName, Object... parameters);

    /**
     * Retrieve a single row mapped from the statement key and parameter.
     *
     * @param className  Unique identifier matching the statement to use.
     * @param methodName Unique identifier matching the methodName to use.
     * @param parameters Parameters object to pass to the statement.
     * @return members of mapped object
     */
    int update(String className, String methodName, Object... parameters);

    /**
     * Retrieves a mapper.
     *
     * @param <T>  the mapper type
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    <T> T getMapper(Class<T> type);
}
