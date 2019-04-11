package core.controller;

import MySpringMVC.V1.annotation.Autowired;
import MySpringMVC.V1.annotation.Controller;
import MySpringMVC.V1.annotation.RequestMapping;
import MySpringMVC.V1.annotation.RequestParam;
import core.pojo.User;
import core.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author SuccessZhang
 */
@SuppressWarnings("unused")
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/id")
    public void id(HttpServletRequest request,
                   HttpServletResponse response,
                   @RequestParam String id) throws IOException {
        //04a2536f-0f4a-11e9-8f01-309c23fd150a
        System.out.println(id);
        User result = testService.queryById(id);
        System.out.println(result);
        response.getWriter().write(result.toString());
    }

    @RequestMapping("/type")
    public int type(HttpServletRequest request,
                    HttpServletResponse response,
                    @RequestParam String id,
                    @RequestParam String type) {
        System.out.println(id + "," + type);
        return testService.setType(id, type);
    }

}
