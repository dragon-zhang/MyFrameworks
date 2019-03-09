package MySpringBoot.controller;

import MySpringMVC.annotation.Controller;
import MySpringMVC.annotation.RequestMapping;
import MySpringMVC.annotation.RequestParam;

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
