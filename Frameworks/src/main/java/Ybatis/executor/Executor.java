package Ybatis.executor;

import Ybatis.pojo.Method;

import java.util.List;

/**
 * @author SuccessZhang
 */
public interface Executor {

    /**
     * 通用查询方法
     *
     * @param method     xml的映射类
     * @param parameters 调用方法的参数
     * @return 指定泛型的列表
     */
    <E> List<E> query(Method method, Object[] parameters);

    /**
     * 通用更新方法
     *
     * @param method     xml的映射类
     * @param parameters 调用方法的参数
     * @return 成功更新数据的数量
     */
    int update(Method method, Object[] parameters);

}
