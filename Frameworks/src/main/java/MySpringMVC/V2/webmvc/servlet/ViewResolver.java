package MySpringMVC.V2.webmvc.servlet;

import java.io.File;
import java.util.Locale;

public class ViewResolver {

    private static final String HTML_SUFFIX = ".html";

    private File templateFile;

    private String encoding;

    public ViewResolver(String templatePath, String encoding) {
        this.templateFile = new File(templatePath);
        this.encoding = encoding;
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (viewName == null || "".equals(viewName)) {
            return null;
        }
        viewName = viewName.endsWith(HTML_SUFFIX) ? viewName : viewName + HTML_SUFFIX;
        if (this.templateFile.getPath().contains(viewName)) {
            return new View(this.templateFile, encoding);
        }
        return null;
    }
}
