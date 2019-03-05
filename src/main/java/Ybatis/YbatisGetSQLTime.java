package Ybatis;

import Ybatis.pojo.Configuration;
import Ybatis.pojo.Method;
import Ybatis.pojo.SQL;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author SuccessZhang
 */
public class YbatisGetSQLTime {

    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\MyFrameworks\\src\\main\\resources\\mapper\\mappers.yml");
            File file = new File("D:\\MyFrameworks\\src\\main\\resources\\mapper\\UserMapper.xml");
            Yaml yaml = new Yaml();
            Configuration mappers = yaml.loadAs(fileInputStream, Configuration.class);
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);

            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                Map<String, Map<String, Method>> mapperMap = mappers.getMappers();
                Set<String> namespaces = mapperMap.keySet();
                for (String namespace : namespaces) {
                    Map<String, Method> methods = mapperMap.get(namespace);
                    Set<String> methodIds = methods.keySet();
                    for (String id : methodIds) {
                        SQL sql = methods.get(id).getSql();
                        sql.getOrigin();
                    }
                }
            }
            System.out.println("-------------------------------------");
            long end1 = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                List<Element> elements = document.getRootElement().elements();
                for (Element element : elements) {
                    String sql = element.getData().toString();
                }
            }
            long end2 = System.currentTimeMillis();
            System.out.println("yml文件读取百万级别sql时间:" + (end1 - start));
            System.out.println("xml文件读取百万级别sql时间:" + (end2 - end1));
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

}