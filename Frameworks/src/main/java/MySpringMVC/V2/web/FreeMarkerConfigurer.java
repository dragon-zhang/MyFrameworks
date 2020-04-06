package MySpringMVC.V2.web;

import lombok.Data;

@Data
public class FreeMarkerConfigurer {

    private String[] templateLoaderPaths;

    private String defaultEncoding;

    public void setTemplateLoaderPath(String templateLoaderPath) {
        this.templateLoaderPaths = new String[]{templateLoaderPath};
    }
}
