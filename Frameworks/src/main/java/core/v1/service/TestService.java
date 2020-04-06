package core.v1.service;

import core.v1.pojo.User;

/**
 * @author SuccessZhang
 */
public interface TestService {
    /**
     * 根据id查询用户
     *
     * @param id id
     * @return 用户信息
     */
    User queryById(String id);

    /**
     * 根据id更新用户类型
     *
     * @param id   id
     * @param type 用户类型
     * @return 成功更新的数量
     */
    int setType(String id, String type);
}
