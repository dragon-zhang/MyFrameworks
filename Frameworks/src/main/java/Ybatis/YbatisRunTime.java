package Ybatis;

import Ybatis.factory.SqlSessionFactory;
import Ybatis.session.SqlSession;
import core.dao.UserMapper;
import core.pojo.User;

/**
 * @author SuccessZhang
 */
public class YbatisRunTime {

    public static void main(String[] args) {
        SqlSessionFactory factory = new SqlSessionFactory();
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            //"04a2536f-0f4a-11e9-8f01-309c23fd150a"
            user = userMapper.queryById(null);
        }
        long end = System.currentTimeMillis();
        System.out.println("mybatis run 100,000 times cost time total : 484");
        System.out.println("ybatis run 100,000 times cost time total : " + (end - start));
        System.out.println(user);
    }

}