package Ybatis;

import Ybatis.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author SuccessZhang
 */
public class YbatisLoadTime {

    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\MyFrameworks\\src\\main\\resources\\mapper\\mappers.yml");
            FileInputStream file = new FileInputStream("D:\\MyFrameworks\\src\\main\\resources\\mapper\\UserMapper.xml");
            long start = System.currentTimeMillis();
            Yaml yaml = new Yaml();
            Configuration mappers = yaml.loadAs(fileInputStream, Configuration.class);
            System.out.println("-------------------------------------");
            long end1 = System.currentTimeMillis();
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            long end2 = System.currentTimeMillis();
            System.out.println("yml文件的读取时间:" + (end1 - start));
            System.out.println("xml文件的读取时间:" + (end2 - end1));
            System.out.println(mappers);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

}