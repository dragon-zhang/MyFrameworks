package core.v2.controller;

import MySpringMVC.V2.annotation.Autowired;
import MySpringMVC.V2.annotation.Controller;
import MySpringMVC.V2.annotation.RequestMapping;
import MySpringMVC.V2.annotation.RequestParam;
import core.v2.pojo.Type;
import core.v2.pojo.User;
import core.v2.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author SuccessZhang
 */
@SuppressWarnings("unused")
@Controller("testController")
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/id")
    public User id(HttpServletRequest request,
                   HttpServletResponse response,
                   @RequestParam String id) throws IOException {
        //04a2536f-0f4a-11e9-8f01-309c23fd150a
        System.out.println(id);
        User result = testService.queryById(id);
        System.out.println(result);
        return result;
    }

    @RequestMapping("/type")
    public int type(HttpServletRequest request,
                    HttpServletResponse response,
                    @RequestParam String id,
                    @RequestParam Type type) {
        System.out.println(id + "," + type);
        return testService.setType(id, type);
    }

    @RequestMapping("/exception")
    public int exception(HttpServletRequest request,
                         HttpServletResponse response) {
        throw new RuntimeException("故意抛出异常!");
    }

}
