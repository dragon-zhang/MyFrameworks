package MyRPC.impl;

import com.example.disperse.service.TestService;

/**
 * @author SuccessZhang
 */
public class TestServiceImpl implements TestService {
    @Override
    public String hello(String name) {
        return "hello , " + name + " !";
    }
}
