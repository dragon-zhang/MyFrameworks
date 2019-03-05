package MyMybatis.session;

import java.util.List;

/**
 * @author SuccessZhang
 */
public interface SqlSession {

    /**
     * Retrieve a single row mapped from the statement key and parameter.
     *
     * @param <T>        the returned object type
     * @param statement  Unique identifier matching the statement to use.
     * @param parameters Parameters object to pass to the statement.
     * @return Mapped object
     */
    <T> T selectOne(String statement, Object... parameters);

    /**
     * Retrieve a list of mapped objects from the statement key and parameter.
     *
     * @param <E>        the returned list element type
     * @param statement  Unique identifier matching the statement to use.
     * @param parameters Parameter object to pass to the statement.
     * @return List of mapped object
     */
    <E> List<E> selectList(String statement, Object... parameters);

    /**
     * Retrieve a single row mapped from the statement key and parameter.
     *
     * @param statement  Unique identifier matching the statement to use.
     * @param parameters Parameters object to pass to the statement.
     * @return members of mapped object
     */
    int update(String statement, Object... parameters);

    /**
     * Retrieves a mapper.
     *
     * @param <T>  the mapper type
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    <T> T getMapper(Class<T> type);
}
