package core.v2.service.impl;

import MySpringMVC.V2.annotation.Lazy;
import MySpringMVC.V2.annotation.Service;
import core.v2.pojo.Type;
import core.v2.pojo.User;
import core.v2.service.TestService;

/**
 * @author SuccessZhang
 */
@SuppressWarnings("unused")
@Lazy(true)
@Service
public class TestServiceImpl implements TestService {

    @Override
    public User queryById(String id) {
        System.out.println("成功查询到了用户");
        User user = new User();
        user.setId(id);
        return user;
    }

    @Override
    public int setType(String id, Type type) {
        System.out.println("将id为" + id + "的用户设置为" + type + "类型成功");
        return 1;
    }
}
