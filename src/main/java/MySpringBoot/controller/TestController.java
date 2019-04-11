package MySpringBoot.controller;

import MySpringMVC.V1.annotation.Controller;
import MySpringMVC.V1.annotation.RequestMapping;
import MySpringMVC.V1.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author SuccessZhang
 */
@SuppressWarnings("unused")
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/test")
    public void type(HttpServletRequest request,
                     HttpServletResponse response,
                     @RequestParam String id,
                     @RequestParam String name) {
        System.out.println(id + "," + name);
    }

}
