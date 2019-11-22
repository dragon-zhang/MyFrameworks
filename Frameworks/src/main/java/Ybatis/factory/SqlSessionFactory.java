package Ybatis.factory;

import Ybatis.pojo.*;
import Ybatis.session.SqlSession;
import Ybatis.session.impl.DefaultSqlSession;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

/**
 * @author SuccessZhang
 */
public class SqlSessionFactory {

    private Configuration config = new Configuration();

    public SqlSessionFactory(Properties properties) {
        //1.加载配置文件
        Jdbc jdbc = new Jdbc();
        jdbc.setDriver(properties.getProperty("jdbc.driver"));
        jdbc.setUrl(properties.getProperty("jdbc.url"));
        jdbc.setUsername(properties.getProperty("jdbc.username"));
        jdbc.setPassword(properties.getProperty("jdbc.password"));
        config.setJdbc(jdbc);
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
                if ("mappers.yml".equals(mapper.getName())) {
                    loadMapperInfo(mapper);
                }
            }
        }
    }

    private void loadMapperInfo(File file) {
        Yaml yaml = new Yaml();
        Configuration configuration = null;
        try {
            configuration = yaml.loadAs(new FileInputStream(file), Configuration.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (configuration == null) {
            throw new RuntimeException("mappers is null !");
        }
        HashMap<String, Map<String, Method>> mappers = configuration.getMappers();
        Set<String> namespaces = mappers.keySet();
        for (String namespace : namespaces) {
            Map<String, Method> methods = mappers.get(namespace);
            Set<String> methodIds = methods.keySet();
            for (String id : methodIds) {
                Method method = Method.changeToMethod(methods.get(id));
                SQL sql = method.getSql();
                assembleSQL(sql, method.getParamNames(), method.getParamNotNull());
                methods.put(id, method);
            }
            mappers.put(namespace, methods);
        }
        config.setMappers(mappers);
    }

    private void assembleSQL(SQL sql, List<String> paramNames, Set<String> paramNotNull) {
        StringBuilder sb = new StringBuilder();
        sb.append(sql.getType()).append(" ");

        List<String> valueSentence = null;
        if ("update".equals(sql.getType())) {
            sb.append(" ").append(sql.getTable());
            sb.append(" set ");
            Values values = sql.getValues();
            valueSentence = values.getSentence();
        }
        Fields fields = sql.getFields();
        Where where = sql.getWhere();

        List<String> fieldSuppose = fields.getSuppose();
        List<String> fieldSentence = fields.getSentence();
        List<String> whereSuppose = where.getSuppose();
        List<String> whereSentence = where.getSentence();

        int fieldsIfLength = getSize(fieldSuppose);
        int fieldsSentenceLength = getSize(fieldSentence);
        int whereIfLength = getSize(whereSuppose);
        int whereSentenceLength = getSize(whereSentence);
        if (fieldsSentenceLength < fieldsIfLength || whereSentenceLength < whereIfLength) {
            throw new RuntimeException("'suppose' tags are more than 'sentence' tags !");
        }
        for (String sentence : fieldSentence) {
            boolean flag = true;
            for (int i = 0; i < fieldsIfLength && flag; i++) {
                if (fieldSuppose.get(i).contains(sentence)) {
                    //if条件包含字段
                    if (fieldSuppose.get(i).contains("!=null") && sentence.contains("#{")) {
                        //变参sentences[i]!=null
                        String paramName = sentence.substring(sentence.indexOf("#{") + 2, sentence.indexOf("}"));
                        paramNotNull.add(paramName);
                    }
                    flag = false;
                }
                if (valueSentence != null) {
                    sb.append(sentence).append("=").append("?");
                    String paramName = valueSentence.get(i).substring(valueSentence.get(i).indexOf("#{") + 2, valueSentence.get(i).indexOf("}"));
                    paramNames.add(paramName);
                }
            }
            if (flag) {
                //if没有包含该字段、或者根本就没有if
                sb.append(sentence).append(",");
            }
        }

        if ("select".equals(sql.getType())) {
            //select a,b,
            sb.setLength(sb.length() - 1);
            sb.append(" from");
            //select * from user
            sb.append(" ").append(sql.getTable());
        }

        sb.append(" where 1=1 ");
        for (int i = 0; i < whereSentence.size(); i++) {
            String sentence = change(whereSentence.get(i), paramNames);
            if (whereSuppose != null && whereSuppose.get(i).contains("!=null") && whereSentence.get(i).contains("#{")) {
                //变参sentences[i]!=null
                String paramName = whereSentence.get(i).substring(whereSentence.get(i).indexOf("#{") + 2, whereSentence.get(i).indexOf("}"));
                paramNotNull.add(paramName);
            }
            whereSentence.set(i, sentence);
            sb.append(" and ").append(sentence);
        }
        //select * user where 1=1 and id like "%"#{id}"%"
        if (sql.getLimit() != null && !"".equals(sql.getLimit())) {
            sb.append(" limit ").append(sql.getLimit());
        }
        //select * user where id like "%"#{id}"%" limit 1
        sql.setOrigin(sb.toString());
    }

    private String change(String sentence, List<String> paramNames) {
        if (sentence.contains("#{")) {
            String paramName = sentence.substring(sentence.indexOf("#{") + 2, sentence.indexOf("}"));
            paramNames.add(paramName);
            sentence = sentence.replaceFirst("#\\{" + paramName + "}", "?");
        }
        return sentence;
    }

    private int getSize(List<String> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        } else {
            return list.size();
        }
    }

    private void loadConfig() {
        try (InputStream inputStream = new FileInputStream("D:\\MyFrameworks\\out\\production\\resources\\application.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Jdbc jdbc = new Jdbc();
            jdbc.setDriver(properties.getProperty("jdbc.driver"));
            jdbc.setUrl(properties.getProperty("jdbc.url"));
            jdbc.setUsername(properties.getProperty("jdbc.username"));
            jdbc.setPassword(properties.getProperty("jdbc.password"));
            config.setJdbc(jdbc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
