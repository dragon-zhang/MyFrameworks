package MyMybatis.factory;

import MyMybatis.pojo.Configuration;
import MyMybatis.pojo.MappedStatement;
import MyMybatis.session.SqlSession;
import MyMybatis.session.impl.DefaultSqlSession;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author SuccessZhang
 */
public class SqlSessionFactory {

    private final Configuration config = new Configuration();

    public SqlSessionFactory(Properties properties) {
        //1.加载配置文件
        config.setJdbcDriver(properties.getProperty("jdbc.driver"));
        config.setJdbcUrl(properties.getProperty("jdbc.url"));
        config.setJdbcUserName(properties.getProperty("jdbc.username"));
        config.setJdbcPassword(properties.getProperty("jdbc.password"));
        //2.接口映射
        loadMappersInfo(properties.getProperty("prefix"));
    }

    public SqlSession openSession() {
        //3.通过SqlSessionFactory创建SqlSession
        return new DefaultSqlSession(config);
    }

    public SqlSessionFactory() {
        //1.加载配置文件
        loadConfig();
        //2.接口映射
        loadMappersInfo(null);
    }

    private void loadMappersInfo(String prefix) {
        //获取指定文件夹信息
        File directory;
        if (prefix == null) {
            directory = new File("D:\\MyFrameworks\\out\\production\\resources\\mapper");
        } else {
            directory = new File(prefix + "\\mapper");
        }
        if (directory.isDirectory()) {
            File[] mappers = directory.listFiles();
            if (mappers == null || mappers.length == 0) {
                throw new RuntimeException("can not find any XXMapper.xml !");
            }
            for (File mapper : mappers) {
                if (mapper.getName().endsWith(".xml")) {
                    loadMapperInfo(mapper);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadMapperInfo(File file) {
        SAXReader reader = new SAXReader();
        //通过read方法将文件转化为dom对象
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (document == null) {
            throw new RuntimeException("transform document failed !");
        }
        //获取根节点<mapper>
        Element root = document.getRootElement();
        //获取命名空间
        String namespace = root.attribute("namespace").getData().toString();
        //获取子节点列表
        List<Element> elements = root.elements();
        for (Element element : elements) {
            String id = element.attribute("id").getData().toString();
            Attribute attribute = element.attribute("resultType");
            String resultType = null;
            if (attribute != null) {
                resultType = attribute.getData().toString();
            }
            String sql = element.getData().toString();
            MappedStatement mappedStatement = new MappedStatement(namespace, id, resultType, null);
            sql = mappedParams(sql, mappedStatement.getParamNames());
            mappedStatement.setSql(sql);
            String sourceId = namespace + "." + id;
            config.getMappedStatements().put(sourceId, mappedStatement);
        }
    }

    private String mappedParams(String sql, List<String> paramNames) {
        if (!sql.contains("#{")) {
            return sql;
        } else {
            String paramName = sql.substring(sql.indexOf("#{") + 2, sql.indexOf("}"));
            paramNames.add(paramName);
            sql = sql.replaceFirst("#\\{" + paramName + "}", "?");
        }
        return mappedParams(sql, paramNames);
    }

    private void loadConfig() {
        try (InputStream inputStream = new FileInputStream("D:\\MyFrameworks\\out\\production\\resources\\application.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            config.setJdbcDriver(properties.getProperty("jdbc.driver"));
            config.setJdbcUrl(properties.getProperty("jdbc.url"));
            config.setJdbcUserName(properties.getProperty("jdbc.username"));
            config.setJdbcPassword(properties.getProperty("jdbc.password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
