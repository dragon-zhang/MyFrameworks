package MyMybatis;

import MyMybatis.factory.SqlSessionFactory;
import MyMybatis.session.SqlSession;
import core.dao.UserMapper;
import core.pojo.User;

/**
 * @author SuccessZhang
 */
public class MyMybatisTestDemo {

    public static void main(String[] args) {
        SqlSessionFactory factory = new SqlSessionFactory();
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            user = userMapper.queryById("04a2536f-0f4a-11e9-8f01-309c23fd150a");
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(user);
    }

}