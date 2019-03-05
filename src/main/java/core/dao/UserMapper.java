package core.dao;

import MyMybatis.annotation.Param;
import MySpringMVC.annotation.Repository;
import core.pojo.User;

/**
 * @author SuccessZhang
 * @date 2018/12/29
 */
@Repository
public interface UserMapper {

    /**
     * 按照用户id查询
     *
     * @param id id
     * @return 用户信息
     */
    User queryById(@Param("id") String id);

    /**
     * 设置用户类型
     *
     * @param id   用户id
     * @param type 用户类型
     * @return 成功更新的数量
     */
    int setType(@Param("id") String id,
                @Param("type") String type);
}