package core.service.impl;

import MySpringMVC.V1.annotation.Autowired;
import MySpringMVC.V1.annotation.Service;
import core.dao.UserMapper;
import core.pojo.User;
import core.service.TestService;

/**
 * @author SuccessZhang
 */

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    @SuppressWarnings("unused")
    private UserMapper userMapper;

    @Override
    public User queryById(String id) {
        return userMapper.queryById(id);
    }

    @Override
    public int setType(String id, String type) {
        return userMapper.setType(id, type);
    }
}
