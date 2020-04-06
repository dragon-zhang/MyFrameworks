package MySpringMVC.V2.webmvc.servlet;

import MySpringMVC.V2.core.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View {

    private static final String HTML_CONTENT_TYPE = "text/html;charset=" + StandardCharsets.UTF_8.toString();

    private static final String REGEX = "￥\\{[^}]+}";

    private File template;

    private String encoding;

    public View(File template, String encoding) {
        this.template = template;
        this.encoding = encoding;
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuilder sb = new StringBuilder();
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.template, "r");
        String line;
        while ((line = randomAccessFile.readLine()) != null) {
            line = new String(line.getBytes(StandardCharsets.ISO_8859_1), Charset.forName(encoding));
            Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String key = matcher.group().replaceAll("￥\\{|}", "");
                Object value = model.get(key);
                if (value == null) {
                    continue;
                }
                line = matcher.replaceFirst(StringUtils.handleRegexSpecialSymbols(value.toString()));
                matcher = pattern.matcher(line);
            }
            sb.append(line);
        }
        response.setContentType(HTML_CONTENT_TYPE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(sb.toString());
    }
}
