package MySpringMVC.V2.web;

import javax.servlet.http.HttpServletRequest;

public class UrlPathHelper {
    public static String getRequestUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        return url;
    }
}
